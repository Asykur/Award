package asykur.khamid.award.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import asykur.khamid.award.model.EmailModel
import asykur.khamid.award.utils.Constant

@Dao
interface DAOEmail {
    @Query ("SELECT * FROM "+Constant.tableName)
    fun getAllEmail():List<EmailModel>

    @Query("SELECT * FROM "+Constant.tableName+" WHERE EMAIL LIKE :email")
    fun getSpecificEmail(email:  String): Boolean

    @Insert(onConflict = OnConflictStrategy.FAIL)
    fun insertNewEmail(email: EmailModel)
}