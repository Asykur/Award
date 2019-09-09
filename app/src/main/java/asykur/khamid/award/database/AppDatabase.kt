package asykur.khamid.award.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import asykur.khamid.award.model.EmailModel
import asykur.khamid.award.utils.Constant

@Database(entities = arrayOf(EmailModel::class),version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun getEMailDAO() : DAOEmail

    companion object{
        var instanceDB: AppDatabase? = null

        fun getAppDatabase(context: Context): AppDatabase{
            if (instanceDB == null){
                instanceDB = Room.databaseBuilder(context.applicationContext,AppDatabase::class.java,Constant.dbName)
                    .allowMainThreadQueries()
                    .build()
            }
            return instanceDB as AppDatabase
        }
    }

    fun destroyInstanceDB(){
        instanceDB = null
    }
}