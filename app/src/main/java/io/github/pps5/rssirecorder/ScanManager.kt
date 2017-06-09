package io.github.pps5.rssirecorder

import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.j256.ormlite.dao.Dao
import java.sql.SQLException
import java.util.*
import kotlin.concurrent.withLock

/**
 * Created by inab on 5/3/17.
 */

class ScanManager(val context: Context) {

    companion object {
        const val TAG = "ScanManager"
    }

    val bluetoothAdapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    val experimentsDao: Dao<Experiments, Int> = DBHelper.getDBHelper(context).getDao(Experiments::class.java)
    val signalsDao: Dao<Signals, Int> = DBHelper.getDBHelper(context).getDao(Signals::class.java)

    var isScanning: Boolean = false
    var currentExperiments: Experiments? = null
    val scanResults: MutableList<Signals> = ArrayList<Signals>()
    val stopHandler: Handler = Handler(Looper.getMainLooper())

    val scanCallback = BluetoothAdapter.LeScanCallback { device, rssi, scanRecord ->
        if (isScanning && SignalUtils.isAplixBeacon(device.address)) {
            addResults(device.address, rssi)
        } else {
            val signalType = SignalUtils.getSignalType(scanRecord)
            val positionId = SignalUtils.getPosition(scanRecord)
            val direction = SignalUtils.getDirection(scanRecord)
            Log.d(TAG, "$signalType, ${SignalUtils.getUuid(scanRecord)}")
            if (signalType != SignalUtils.Companion.SignalType.TYPE_NON_OPERATION) {
                Log.d(TAG, "$signalType, $positionId, $direction")
            }
            when (signalType) {
                SignalUtils.Companion.SignalType.TYPE_START -> startBeaconScan(positionId, direction)
                SignalUtils.Companion.SignalType.TYPE_STOP -> stopBeaconScan(true)
                SignalUtils.Companion.SignalType.TYPE_FLUSH -> insertSignals()
                else -> {
                    /* no-op, i.e. drop a signal from non-deployed/Aplix beacon */
                }
            }
        }
    }

    val soundManager: SoundManager = SoundManager(context)

    init {
        startLeScan()
    }

    fun addResults(mac: String, rssi: Int) {
        val signal = Signals()
        signal.receiveDate = Date()
        signal.mac = mac
        signal.rssi = rssi
        signal.experiments = currentExperiments
        scanResults.add(signal)
    }

    fun insertExperiments(positionId: Int, direction: Int, startDate: Date): Experiments? {
        /**
         * Creates and inserts new Experiments object to SQLite database.
         *
         * @param positionId new position number
         * @param direction new direction number
         * @param startDate Date object which indicates start recording
         * @return a new Experiments object
         */
        val lock = java.util.concurrent.locks.ReentrantLock()
        lock.withLock {
            if (positionId != currentExperiments?.positionId
                    || direction != currentExperiments?.direction) {
                val ex = Experiments()
                ex.positionId = positionId
                ex.direction = direction
                ex.startDate = startDate

                try {
                    experimentsDao.create(ex)
                    Log.d(TAG, "${ex.id}")
                    return ex
                } catch (e: SQLException) {
                    Log.d(TAG, "insert experiments error")
                    showToast("Already exists in DB (position: $positionId,  direction: $direction)")
                    soundManager.playErrorSound()
                    return null
                }
            } else {
                return currentExperiments
            }
        }
    }

    fun deleteExperiments() {
        try {
            experimentsDao.delete(currentExperiments)
        } catch (e: SQLException) {
            Log.d(TAG, "Delete exception")
        }
    }

    fun insertSignals() {
        /**
         * Creates and inserts new Signals objects to SQLite database.
         */
        val lock = java.util.concurrent.locks.ReentrantLock()
        lock.withLock {
            if (scanResults.isNotEmpty() && !isScanning) {
                Log.d(TAG, "size: ${scanResults.size}")
                soundManager.playFlushSound()
                try {
                    signalsDao.create(scanResults)
                    scanResults.clear()
                    soundManager.playFinishSound()
                } catch (e: SQLException) {
                    soundManager.playErrorSound()
                    showToast("Insert Error")
                }
            }
        }
    }

    fun showToast(text: String) {
        Handler(Looper.getMainLooper()).post {
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
        }
    }

    @Suppress("deprecation")
    fun startLeScan() {
        /**
         * Starts LeScan until [stopLeScan] is called
         * @see stopLeScan
         */
        bluetoothAdapter.startLeScan(scanCallback)
    }

    @Suppress("deprecation")
    fun stopLeScan() {
        /**
         * Stops LeScan.
         * @see startLeScan
         */
        bluetoothAdapter.stopLeScan(scanCallback)
    }

    fun startBeaconScan(positionId: Int, direction: Int) {
        /**
         * Starts scanning advertise packets until [stopBeaconScan] is called and create [stopHandler] callback.
         * [startLeScan] should be called before this method.
         * @param positionId current scanning position
         * @param direction current scanning direction
         * @see stopBeaconScan
         */

        val lock = java.util.concurrent.locks.ReentrantLock()

        lock.withLock {
            // to avoid multiple scan
            if (!isScanning) {
                Log.d(TAG, "started at: ${Date()}")
                try {
                    currentExperiments = insertExperiments(positionId, direction, Date())!!
                } catch (e: NullPointerException) {
                    Log.d(TAG, "currentExperiments is not set")
                    soundManager.playErrorSound()
                    return
                }
                isScanning = true
                soundManager.playStartSound()
                // because of difficulty to synchronize the internal clocks between
                // the two devices, each device records 2 minutes and 1 more seconds
                stopHandler.postDelayed({
                    stopBeaconScan(false)
                },  120_000 + 1_000 /* milliseconds */)
            }
        }
    }

    fun stopBeaconScan(isSuspended: Boolean) {
        /**
         * Stops scanning advertise packets and removes [stopHandler] callback.
         * @see startBeaconScan
         */
        val lock = java.util.concurrent.locks.ReentrantLock()
        lock.withLock {
            if (isScanning) {  // to avoid multiple scan
                Log.d(TAG, "stopped at: ${Date()}")
                isScanning = false
                soundManager.playStopSound()
                stopHandler.removeCallbacksAndMessages(null)
                Log.d(TAG, "isSuspended: $isSuspended")

                if (isSuspended) {
                    deleteExperiments()
                } else {
                    insertSignals()
                }
            }
        }
    }

    @Suppress("unused")
    fun finalize() {
        /**
         * Stops LeScan.
         * NOTE: This method is not always called when this instance is deleted.
         */
        stopLeScan()
    }
}