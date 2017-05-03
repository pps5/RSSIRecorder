package io.github.pps5.rssirecorder

import com.j256.ormlite.field.DataType
import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable
import java.util.*

/**
 * Created by inab on 4/25/17.
 */

@DatabaseTable(tableName = "experiments_info")
class Experiments(@DatabaseField(columnName = "id", generatedId = true, canBeNull = false)
                  @Suppress("unused") var id: Int = 0,
                  @DatabaseField(columnName = "start_date", dataType = DataType.DATE_STRING, canBeNull = false)
                  @Suppress("unused") var startDate: Date = Date(),
                  @DatabaseField(columnName = "position_id", dataType = DataType.INTEGER, canBeNull = false, unique = true, uniqueCombo = true)
                  @Suppress("unused") var positionId: Int = 0,
                  @DatabaseField(columnName = "direction", dataType = DataType.INTEGER, canBeNull = false, unique = true, uniqueCombo = true)
                  @Suppress("unused") var direction: Int = -1)
