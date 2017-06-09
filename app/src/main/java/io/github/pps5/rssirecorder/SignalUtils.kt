package io.github.pps5.rssirecorder

import android.util.Log
import org.apache.commons.codec.binary.Hex


/**
 * Created by inab on 5/3/17.
 */

class SignalUtils {

    companion object {
        const val TAG = "SignalUtils"
        const val START_SIGNAL = "00000000000000000000000000000001"
        const val STOP_SIGNAL =  "00000000000000000000000000000002"
        const val FLUSH_SIGNAL = "00000000000000000000000000000003"

        enum class SignalType(name: String) {
            TYPE_START("Start"), TYPE_STOP("Stop"), TYPE_FLUSH("Flush"), TYPE_NON_OPERATION("no-op")
        }

        fun getSignalType(scanRecord: ByteArray): SignalType {
            val uuid = getUuid(scanRecord)
            when (uuid) {
                START_SIGNAL -> return SignalType.TYPE_START
                STOP_SIGNAL -> return SignalType.TYPE_STOP
                FLUSH_SIGNAL -> return SignalType.TYPE_FLUSH
                else -> return SignalType.TYPE_NON_OPERATION
            }
        }

        fun isAplixBeacon(mac: String): Boolean {
            return mac.startsWith("00:1C:4D:40")
        }

        fun getPosition(scanRecord: ByteArray): Int {
            /**
             * Get position from Major (position at 25-26 bytes) of the advertising packet.
             * See Proximity Beacon Specification published by apple.
             *
             * @param scanRecord ByteArray from LeScanCallback
             * @return position id sent from the server application
             * */
            return Integer.parseInt(String(Hex.encodeHex(scanRecord.slice(25..26).toByteArray())), 16)
        }

        fun getDirection(scanRecord: ByteArray): Int {
            /**
             * Get direction from Minor (position at 27-28 bytes) of the advertising packet.
             * See Proximity Beacon Specification published by apple.
             *
             * @param scanRecord ByteArray from LeScanCallback
             * @return direction number sent from the server application
             */
            return Integer.parseInt(String(Hex.encodeHex(scanRecord.slice(27..28).toByteArray())), 16)
        }

        fun getUuid(scanRecord: ByteArray): String {
            return String(Hex.encodeHex(scanRecord.slice(9..24).toByteArray()))
        }
    }
}