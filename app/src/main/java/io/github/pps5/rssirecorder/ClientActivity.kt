package io.github.pps5.rssirecorder

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager

class ClientActivity : AppCompatActivity() {

    lateinit var scanManager: ScanManager

    companion object {
        const val TAG: String = "ClientActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        scanManager = ScanManager(applicationContext)
    }

    override fun onDestroy() {
        super.onDestroy()
        scanManager.stopLeScan()
    }
}
