package io.github.pps5.rssirecorder

import android.content.Context
import android.media.AudioManager
import android.media.SoundPool
import android.support.annotation.IntDef
import android.util.Log

/**
 * Created by inab on 5/4/17.
 */

class SoundManager(context: Context) {

    companion object {
        const val SOUND_START = 0
        const val SOUND_STOP = 1
        const val SOUND_FLUSH = 2
        const val SOUND_FLUSH_FINISH = 3
        const val SOUND_ERROR = 4
    }

    @Suppress("deprecation")
    val soundPool: SoundPool = SoundPool(5, AudioManager.STREAM_NOTIFICATION, 0)
    val soundIds: Array<Int> = arrayOf(0, 0, 0, 0, 0)

    init {
        soundIds[SOUND_START] = soundPool.load(context, R.raw.start, 0)
        soundIds[SOUND_STOP] = soundPool.load(context, R.raw.stop, 0)
        soundIds[SOUND_FLUSH] = soundPool.load(context, R.raw.flush, 0)
        soundIds[SOUND_FLUSH_FINISH] = soundPool.load(context, R.raw.flush_finish, 0)
        soundIds[SOUND_ERROR] = soundPool.load(context, R.raw.error, 0)
    }

    fun playStartSound() {
        soundPool.play(soundIds[SOUND_START], 1.0f, 1.0f, 1, 0, 1.0f)
    }

    fun playStopSound() {
        soundPool.play(soundIds[SOUND_STOP], 1.0f, 1.0f, 1, 0, 1.0f)
    }

    fun playFlushSound() {
        soundPool.play(soundIds[SOUND_FLUSH], 1.0f, 1.0f, 1, 0, 1.0f)
    }

    fun playFinishSound() {
        soundPool.play(soundIds[SOUND_FLUSH_FINISH], 1.0f, 1.0f, 1, 0, 1.0f)
    }

    fun playErrorSound() {
        soundPool.play(soundIds[SOUND_ERROR], 1.0f, 1.0f, 1, 0, 1.0f)
    }
}