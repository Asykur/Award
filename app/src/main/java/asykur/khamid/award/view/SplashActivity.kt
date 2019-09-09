package asykur.khamid.award.view

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import asykur.khamid.award.R
import asykur.khamid.award.utils.AppPreference
import org.jetbrains.anko.intentFor

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        if (AppPreference.getPreference(applicationContext).getBoolean(AppPreference.isLogin,false)){
            Handler().postDelayed({
                startActivity(intentFor<MainActivity>())
                finish()
            },2000)
        }else{
            Handler().postDelayed({
                startActivity(intentFor<LoginActivity>())
                finish()
            },2000)
        }
    }

}
