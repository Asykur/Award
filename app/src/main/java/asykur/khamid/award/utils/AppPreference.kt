package asykur.khamid.award.utils

import android.content.Context
import android.content.SharedPreferences

class AppPreference{

    companion object{
        const val PREF_NAME = "PREF_AWARD"
        const val isLogin = "isLogin"


        fun setPreference(context: Context) : SharedPreferences.Editor{
            return context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE).edit()
        }

        fun getPreference(context: Context): SharedPreferences{
            return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        }

    }



}