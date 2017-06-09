package io.github.pps5.rssirecorder

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper
import com.j256.ormlite.support.ConnectionSource
import com.j256.ormlite.table.TableUtils
import java.sql.SQLException
import java.util.*

/**
 * Created by inab on 4/25/17.
 */

class DBHelper(val context: Context)
    : OrmLiteSqliteOpenHelper(context, "$DATABASE_PATH/$DATABASE_FILE_NAME", null, DATABASE_VERSION) {

    companion object {
        val DATABASE_PATH: String = Environment.getExternalStorageDirectory().absolutePath
        const val DATABASE_FILE_NAME = "2017-05-03.sqlite3"
        const val DATABASE_VERSION = 1
        var dbHelper: DBHelper? = null

        fun getDBHelper(context: Context): DBHelper {
            if (dbHelper == null) {
                dbHelper = DBHelper(context)
            }
            return dbHelper as DBHelper
        }
    }

    override fun onCreate(p0: SQLiteDatabase?, p1: ConnectionSource?) {
        p0?.execSQL("PRAGMA foreign_keys=ON;")
        createTable(p0, p1, "Experiments", Experiments::class.java)
        createTable(p0, p1, "Signals", Signals::class.java)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: ConnectionSource?, p2: Int, p3: Int) {
        // no-op
    }

    fun <T> createTable(p0: SQLiteDatabase?, p1: ConnectionSource?,
                        tableName: String, clazz: Class<T>) {
        try {
            TableUtils.createTableIfNotExists(p1, clazz)
        } catch (e: SQLException) {
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(context,
                        "Cannot create table '$tableName' to $DATABASE_FILE_NAME",
                        Toast.LENGTH_SHORT
                )
            }
        }
    }
}