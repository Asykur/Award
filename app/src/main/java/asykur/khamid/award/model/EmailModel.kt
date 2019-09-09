package asykur.khamid.award.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import asykur.khamid.award.utils.Constant


@Entity(tableName = Constant.tableName)
data class EmailModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    @ColumnInfo(name = "EMAIL")
    val email: String
)