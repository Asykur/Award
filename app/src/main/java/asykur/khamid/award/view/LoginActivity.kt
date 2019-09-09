package asykur.khamid.award.view

import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import asykur.khamid.award.R
import asykur.khamid.award.database.AppDatabase
import asykur.khamid.award.model.EmailModel
import asykur.khamid.award.utils.AppPreference
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btnSignIn.setOnClickListener {
            validateEmail()
        }
    }

    private fun validateEmail() {
        val email = etEMailAddress.text.toString()
        if (!TextUtils.isEmpty(email) && email.contains("@") && email.contains(".")){
            checkExistingEmail(email)
        }
    }

    private fun checkExistingEmail(email: String) {
        val database = AppDatabase.getAppDatabase(this)
        val isExist = database.getEMailDAO().getSpecificEmail(email)
        if (isExist){
            startActivity(intentFor<MainActivity>())
            finish()
            toast(getString(R.string.success_login))
        }else{
            val sb = Snackbar.make(etEMailAddress,getString(R.string.wording_add_email),Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(R.string.add_email)) {
                    doAsync {
                        database.getEMailDAO().insertNewEmail(EmailModel(null,email))
                        AppPreference.setPreference(applicationContext).putBoolean(AppPreference.isLogin,true).commit()
                        runOnUiThread {
                            toast(getString(R.string.success_add_email))
                        }
                    }
                }.setActionTextColor(Color.GREEN)
            val view = sb.view
            view.setBackgroundColor(Color.GRAY)
            sb.show()
        }
    }
}
