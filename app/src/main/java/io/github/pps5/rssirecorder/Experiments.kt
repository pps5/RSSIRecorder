package io.github.pps5.rssirecorder

import com.j256.ormlite.field.DataType
import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable
import java.util.*

/**
 * Created by inab on 4/25/17.
 */

@DatabaseTable(tableName = "experiments_info")
class Experiments(id: Int, startDate: Date, positionId: Int) {

    @DatabaseField(columnName = "id", generatedId = true, canBeNull = false)
    @Suppress("unused")
    var id: Int = id

    @DatabaseField(columnName = "start_date", dataType = DataType.DATE_STRING, canBeNull = false)
    @Suppress("unused")
    var startDate: Date = startDate

    @DatabaseField(columnName = "position_id", dataType = DataType.INTEGER, canBeNull = false)
    @Suppress("unused")
    var positionId: Int = positionId
}