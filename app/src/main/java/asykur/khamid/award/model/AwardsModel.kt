package asykur.khamid.award.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import asykur.khamid.award.utils.Constant

@Entity(tableName = Constant.tableAwards)
data class AwardsModel (
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    @ColumnInfo(name = "TYPE")
    val type: String,
    @ColumnInfo(name = "POINT")
    val point: Int,
    @ColumnInfo(name = "NAME")
    val name: String,
    @ColumnInfo(name = "IMAGE_URL")
    val imageUrl: Int
)