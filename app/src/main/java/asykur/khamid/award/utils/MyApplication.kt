package asykur.khamid.award.utils

import android.app.Application
import asykur.khamid.award.database.AppDatabase

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        AppDatabase.getAppDatabase(applicationContext)
    }
}