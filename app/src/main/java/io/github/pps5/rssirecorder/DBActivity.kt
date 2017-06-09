package io.github.pps5.rssirecorder

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.TextView
import com.j256.ormlite.dao.Dao
import java.util.*


class DBActivity : AppCompatActivity() {

    companion object {
        const val TAG = "DBActivity"
    }

    lateinit var tableData: List<Experiments>
    lateinit var experimentsDAO: Dao<Experiments, Int>
    lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_db)
        experimentsDAO = DBHelper.getDBHelper(this).getDao(Experiments::class.java)
        swipeRefreshLayout = findViewById(R.id.swipe_layout) as SwipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener {
            tableData = experimentsDAO.queryForAll()
            updateViews()
            swipeRefreshLayout.isRefreshing = false
        }
        initViews()
    }

    fun initViews() {
        tableData = experimentsDAO.queryForAll()
        val recycler: RecyclerView = findViewById(R.id.table_recycler) as RecyclerView
        val adapter = ExperimentsListAdapter(applicationContext, tableData)
        recycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recycler.adapter = adapter
        updateViews()
    }

    fun updateViews() {
        val recycler: RecyclerView = findViewById(R.id.table_recycler) as RecyclerView
        val textNoTables: TextView = findViewById(R.id.text_notables) as TextView
        if (tableData.isEmpty()) {
            textNoTables.visibility = View.VISIBLE
            recycler.visibility = View.GONE
        } else {
            textNoTables.visibility = View.GONE
            recycler.visibility = View.VISIBLE
            recycler.adapter.notifyDataSetChanged()
        }
    }

}

