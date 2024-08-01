package id.go.kebumenkab.realcount.ui.activity

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import id.go.kebumenkab.realcount.MainActivity
import id.go.kebumenkab.realcount.R
import id.go.kebumenkab.realcount.databinding.ActivityLoginBinding
import id.go.kebumenkab.realcount.databinding.LayoutDialogLoginFailedBinding
import id.go.kebumenkab.realcount.utils.HawkStorage
import id.go.kebumenkab.realcount.utils.Result
import id.go.kebumenkab.realcount.utils.Constants
import id.go.kebumenkab.realcount.utils.dialogNoConnection
import id.go.kebumenkab.realcount.utils.hideDialog
import id.go.kebumenkab.realcount.utils.isNetworkAvailable
import id.go.kebumenkab.realcount.utils.showProgressDialog
import id.go.kebumenkab.realcount.utils.versionApp
import id.go.kebumenkab.realcount.viewmodel.LoginViewModel
import id.go.kebumenkab.realcount.utils.StatusBarUtils

class LoginActivity : AppCompatActivity() {
    private var appVersion: String = Constants.APP_VERSION
    private var nip: String = ""
    private var password: String = ""
    private val loginViewModel by viewModels<LoginViewModel>()

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        StatusBarUtils.setStatusBarColor(this)
        setObserver()
        setListener()
        setVersionApp()
        setRememberMe()
//        binding.apply {
//            editUsername.setText("198709122010011006")
//            editPassword.setText("kebumen123!")
//        }
    }

    private fun setListener() {
        binding.apply {
            editNip.addTextChangedListener(textWatcherNip)
            editPassword.addTextChangedListener(textWatcherPassword)

            buttonLogin.setOnClickListener {
                if (isNetworkAvailable(this@LoginActivity)) {
                    loginViewModel.login(Constants.BASE_URL_LOGIN, appVersion, nip, password)
                } else {
                    dialogNoConnection(this@LoginActivity)
                }
            }
        }
    }

    private fun setObserver() {
        loginViewModel.login.observe(this) {
            if (it != null) {
                when (it) {
                    is Result.Loading -> {
                        setLoading(true)
                    }
                    is Result.Success -> {
                        setLoading(false)

                        val status = it.data.status
                        val message = it.data.message
                        if (status == "success") {
                            val user = it.data
                            HawkStorage.instance(this@LoginActivity).setUser(user)
                            saveRememberMe()
                            goToMain()

                            // save value username & password to shared prefs
                            val prefs = getSharedPreferences(PREFS_NAME_LOGIN, MODE_PRIVATE)
                            val editor = prefs.edit()
                            editor.putString(KEY_NIP, nip)
                            editor.putString(KEY_PASSWORD, password)
                            editor.apply()
                        } else {
                            val binding: LayoutDialogLoginFailedBinding = LayoutDialogLoginFailedBinding.inflate(layoutInflater)
                            val builder: AlertDialog.Builder = AlertDialog.Builder(layoutInflater.context)
                            builder.setView(binding.root)
                            val dialog: AlertDialog = builder.create()
                            binding.apply {
                                labelMessage.text = message
                                buttonOk.setOnClickListener {
                                    dialog.dismiss()
                                }
                            }
                            dialog.setCancelable(true)
                            dialog.show()
                            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                        }
                    }
                    is Result.Error -> {
                        setLoading(false)
                        val errorMessage = it.error
                        val binding: LayoutDialogLoginFailedBinding = LayoutDialogLoginFailedBinding.inflate(layoutInflater)
                        val builder: AlertDialog.Builder = AlertDialog.Builder(layoutInflater.context)
                        builder.setView(binding.root)
                        val dialog: AlertDialog = builder.create()
                        binding.apply {
                            ivAlert.setImageResource(R.drawable.ic_warning)
                            labelMessage.text = errorMessage
                            buttonOk.text = getString(R.string.try_again)
                            buttonOk.setOnClickListener {
                                dialog.dismiss()
                            }
                        }
                        dialog.setCancelable(true)
                        dialog.show()
                        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    }

//                    else -> {}
                }
            }
        }
    }

    private val textWatcherNip: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            binding.apply {
                validateInput()
            }
        }

        override fun afterTextChanged(p0: Editable?) {

        }
    }

    private val textWatcherPassword: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            binding.apply {
                validateInput()
            }
        }

        override fun afterTextChanged(p0: Editable?) {

        }
    }

    private fun validateInput() {
        binding.apply {
            nip = editNip.text.toString().trim()
            password = editPassword.text.toString()

            buttonLogin.isEnabled = nip.isNotEmpty() == true && password.isNotEmpty() == true
        }
    }

    private fun saveRememberMe() {
        val prefs = getSharedPreferences(PREFS_NAME_REMEMBER, MODE_PRIVATE)
        val editor = prefs.edit()

        binding.apply {
            if (checkBoxRememberMe.isChecked && editNip.text!!.isNotEmpty()) {
                editor.putString(KEY_NIP, editNip.text.toString())
                editor.putBoolean(KEY_REMEMBER_ME, true)
            } else {
                editor.remove(KEY_NIP)
                editor.remove(KEY_REMEMBER_ME)
            }
        }

        editor.apply()
    }

    private fun setRememberMe() {
        val prefs = getSharedPreferences(PREFS_NAME_REMEMBER, MODE_PRIVATE)
        val rememberMe = prefs.getBoolean(KEY_REMEMBER_ME, false)

        if (rememberMe) {
            val nip = prefs.getString(KEY_NIP, null)
            binding.apply {
                editNip.setText(nip)
                checkBoxRememberMe.isChecked = true
            }
        }
    }

    private fun setVersionApp()  {
        versionApp(binding.textVersionApp)
    }

    private fun goToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun setLoading(isLoading: Boolean) {
        if (isLoading) {
            showProgressDialog(this)
        } else {
            hideDialog()
        }
    }

    companion object {
        private const val PREFS_NAME_REMEMBER = "rememberMePrefsFile"
        private const val PREFS_NAME_LOGIN = "loginPrefsFile"
        private const val KEY_NIP = "nip"
        private const val KEY_PASSWORD = "password"
        private const val KEY_REMEMBER_ME = "remember_me"
    }
}