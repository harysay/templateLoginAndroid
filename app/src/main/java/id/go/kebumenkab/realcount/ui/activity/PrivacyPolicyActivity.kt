package id.go.kebumenkab.realcount.ui.activity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import id.go.kebumenkab.realcount.R
import id.go.kebumenkab.realcount.databinding.ActivityPrivacyPolicyBinding
import id.go.kebumenkab.realcount.databinding.LayoutDialogPrivacyPolicyBinding
import id.go.kebumenkab.realcount.utils.HawkStorage
import id.go.kebumenkab.realcount.ui.activity.LoginActivity
import id.go.kebumenkab.realcount.MainActivity
import id.go.kebumenkab.realcount.utils.StatusBarUtils
import id.go.kebumenkab.realcount.utils.fromHtml

class PrivacyPolicyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPrivacyPolicyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrivacyPolicyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        StatusBarUtils.setStatusBarColor(this)
        setListener()
    }

    private fun setListener() {
        binding.buttonStart.setOnClickListener {
            dialogPrivacyPolicy()
        }
    }

    private fun dialogPrivacyPolicy() {
        val binding: LayoutDialogPrivacyPolicyBinding = LayoutDialogPrivacyPolicyBinding.inflate(LayoutInflater.from(this))
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setView(binding.root)
        val dialog: AlertDialog = builder.create()
        binding.apply {
            labelTitle.text = getString(R.string.privacy_policy)
            labelMessage.text = fromHtml(getString(R.string.desc_privacy_policy))
            buttonYes.text = getString(R.string.agree)
            buttonYes.setOnClickListener {
                goToMainOrLogin()
                dialog.dismiss()
            }
            buttonNo.text = getString(R.string.back)
            buttonNo.setOnClickListener {
                dialog.dismiss()
            }
        }
        dialog.setCancelable(true)
        dialog.show()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    private fun goToMainOrLogin() {
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("privacy_policy_shown", true)
        editor.apply()

        val isLogin = HawkStorage.instance(this).isLogin()

        if (isLogin) {
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