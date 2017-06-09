package io.github.pps5.rssirecorder

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.Adapter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by inab on 5/2/17.
 */

class ExperimentsListAdapter(context: Context,
                             val data: List<Experiments>) : Adapter<ExperimentsListAdapter.ViewHolder>() {

    var inflater: LayoutInflater = LayoutInflater.from(context)


    companion object {
        const val DATE_FORMAT = "yyy-MM-dd HH:mm:ss:SS"
        val sdf: SimpleDateFormat = SimpleDateFormat(DATE_FORMAT, Locale.JAPAN)
    }

    override fun getItemCount(): Int {
        Log.d("dbg", "size${data.size}")
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tableName.text = "${data[position].id}"
        holder.startDate.text = "Started at: ${sdf.format(data[position].startDate)}"
        holder.positionId.text = "Position: ${data[position].positionId}"
        holder.direction.text = "Direction: ${data[position].direction}"

    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        Log.d("dbg", "oncreateViewHolder")
        return ViewHolder(inflater.inflate(R.layout.item_table, parent, false))
    }

    open class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tableName: TextView = itemView.findViewById(R.id.table_name) as TextView
        val startDate: TextView = itemView.findViewById(R.id.start_date) as TextView
        val positionId: TextView = itemView.findViewById(R.id.position_id) as TextView
        val direction: TextView = itemView.findViewById(R.id.direction) as TextView
    }
}
