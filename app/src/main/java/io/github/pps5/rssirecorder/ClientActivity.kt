package io.github.pps5.rssirecorder

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class ClientActivity : AppCompatActivity() {

    companion object {
        const val TAG: String = "ClientActivity"
        var isScaning: Boolean = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client)
    }


}
