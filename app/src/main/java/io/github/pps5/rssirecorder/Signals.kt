package io.github.pps5.rssirecorder

import com.j256.ormlite.field.DataType
import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable
import java.util.Date

/**
 * Created by inab on 4/25/17.
 */

@DatabaseTable(tableName = "Signals")
class Signals(id: Int, startDate: Date, rssi: Int, experiments: Experiments) {

    @DatabaseField(columnName = "id", generatedId = true, dataType = DataType.INTEGER, canBeNull = false)
    @Suppress("unused")
    var id: Int = id

    @DatabaseField(columnName = "receive_date", dataType = DataType.DATE_STRING, canBeNull = false)
    @Suppress("unused")
    var receiveDate: Date = startDate

    @DatabaseField(columnName = "rssi", dataType = DataType.INTEGER, canBeNull = false)
    @Suppress("unused")
    var rssi: Int = rssi

    @DatabaseField(columnName = "experiments_id", dataType = DataType.INTEGER, canBeNull = false, foreign = true)
    @Suppress("unused")
    var experiments: Experiments = experiments
}