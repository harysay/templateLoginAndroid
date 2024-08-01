package id.go.kebumenkab.realcount.ui.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import id.go.kebumenkab.realcount.databinding.ActivitySplashBinding
import id.go.kebumenkab.realcount.utils.HawkStorage
import id.go.kebumenkab.realcount.utils.StatusBarUtils
import id.go.kebumenkab.realcount.MainActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        afterDelayGoToLogin()
        StatusBarUtils.setStatusBarColor(this)
    }

    private fun afterDelayGoToLogin() {
        Handler(Looper.getMainLooper()).postDelayed({
            checkIsLogin()
        },2000)
    }

    private fun checkIsLogin() {
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val privacyPolicyShown = sharedPreferences.getBoolean("privacy_policy_shown", false)

        val isLogin = HawkStorage.instance(this).isLogin()

        if (!privacyPolicyShown) {
            val intent = Intent(this, PrivacyPolicyActivity::class.java)
            startActivity(intent)
            finish()
        } else if (isLogin) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}