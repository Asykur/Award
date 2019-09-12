package asykur.khamid.award.view

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import asykur.khamid.award.R
import asykur.khamid.award.database.AppDatabase
import asykur.khamid.award.model.EmailModel
import asykur.khamid.award.utils.AppPreference
import asykur.khamid.award.utils.GeneralDialog
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast

class LoginActivity : AppCompatActivity() {
    private lateinit var progress: GeneralDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        progress = GeneralDialog(getString(R.string.logging_in))

        btnSignIn.setOnClickListener {
            validateEmail()
        }
    }

    private fun validateEmail() {
        val email = etEMailAddress.text.toString()
        if (TextUtils.isEmpty(email)) {
            errorSnackBar(getString(R.string.empty_email))
        } else if (!email.contains("@") || !email.contains(".")) {
            errorSnackBar(getString(R.string.invalid_email))
        } else {
            progress.show(this.supportFragmentManager, null)
            Handler().postDelayed({
                checkExistingEmail(email)
            }, 2000)
        }
    }

    private fun errorSnackBar(message: String) {
        Snackbar.make(etEMailAddress, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun checkExistingEmail(email: String) {
        val database = AppDatabase.getAppDatabase(this)
        val isExist = database.getEMailDAO().getSpecificEmail(email)
        if (isExist) {
            startActivity(intentFor<MainActivity>())
            finish()
            progress.dismissAllowingStateLoss()
            AppPreference.setPreference(applicationContext).putBoolean(AppPreference.isLogin, true)
                .commit()

        } else {
            progress.dismissAllowingStateLoss()
            val sb = Snackbar.make(
                etEMailAddress,
                getString(R.string.wording_add_email),
                Snackbar.LENGTH_INDEFINITE
            )
                .setAction(getString(R.string.add_email)) {
                    doAsync {
                        database.getEMailDAO().insertNewEmail(EmailModel(null, email))
                        runOnUiThread {
                            toast(getString(R.string.success_add_email))
                        }
                    }
                }.setActionTextColor(Color.GREEN)
            val view = sb.view
            view.setBackgroundColor(resources.getColor(R.color.darkGrey))
            sb.show()
        }
    }
}
