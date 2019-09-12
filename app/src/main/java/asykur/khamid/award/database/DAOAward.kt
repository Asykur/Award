package asykur.khamid.award.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import asykur.khamid.award.model.AwardsModel
import asykur.khamid.award.utils.Constant

@Dao
interface DAOAward {
    @Query("SELECT * FROM " + Constant.tableAwards + " LIMIT :limit OFFSET :offset")
    fun getPagingAwards(limit: Int?, offset: Int): List<AwardsModel>

    @Query("SELECT * FROM " + Constant.tableAwards)
    fun getAllAwards(): List<AwardsModel>

    @Query("SELECT * FROM " + Constant.tableAwards + " WHERE TYPE IN(:type) AND POINT BETWEEN :valA AND :valB")
    fun getAwardsByTypePoint(type: Array<String>?, valA: Int, valB: Int): List<AwardsModel>

    @Query("SELECT * FROM " + Constant.tableAwards + " WHERE TYPE IN(:type)")
    fun getAwardsByType(type: Array<String>?): List<AwardsModel>

    @Query("SELECT * FROM " + Constant.tableAwards + " WHERE POINT BETWEEN :valA AND :valB")
    fun getAwardsByPoint(valA: Int, valB: Int): List<AwardsModel>

    @Insert(onConflict = OnConflictStrategy.FAIL)
    fun insertNewAwards(data: AwardsModel)
}