package io.github.pps5.rssirecorder

import com.j256.ormlite.field.DataType
import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable
import java.util.Date

/**
 * Created by inab on 4/25/17.
 */

@DatabaseTable(tableName = "signals")
class Signals(@DatabaseField(columnName = "id", generatedId = true, dataType = DataType.INTEGER, canBeNull = false)
              @Suppress("unused") var id: Int = 0,
              @DatabaseField(columnName = "receive_date", dataType = DataType.DATE_STRING, canBeNull = false)
              @Suppress("unused") var receiveDate: Date = Date(),
              @DatabaseField(columnName = "mac", dataType = DataType.STRING, canBeNull = false)
              @Suppress("unused") var mac: String = "",
              @DatabaseField(columnName = "rssi", dataType = DataType.INTEGER, canBeNull = false)
              @Suppress("unused") var rssi: Int = 0,
              @DatabaseField(columnName = "experiments_id", canBeNull = false, foreign = true, columnDefinition = "integer references experiments_info(id)")
              @Suppress("unused") var experiments: Experiments? = null)