package id.go.kebumenkab.realcount

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import id.go.kebumenkab.realcount.databinding.ActivityMainBinding
import id.go.kebumenkab.realcount.databinding.LayoutDialogWarningBinding
import id.go.kebumenkab.realcount.ui.fragment.HomePilBup
import id.go.kebumenkab.realcount.ui.fragment.HomePilGub
import id.go.kebumenkab.realcount.utils.StatusBarUtils

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var appUpdateManager: AppUpdateManager
    private var backPressedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(HomePilBup())
        binding.bottomNavigationView.setOnItemSelectedListener {

            when(it.itemId){

                R.id.menu_pilbup -> replaceFragment(HomePilBup())
                R.id.menu_pilgub -> replaceFragment(HomePilGub())

                else ->{



                }

            }

            true

        }

        init()
//        setContentView(R.layout.activity_main)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
    }

    private fun replaceFragment(fragment : Fragment){

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout,fragment)
        fragmentTransaction.commit()


    }

    private fun init() {
        checkForUpdate()
        pressAgainToExit()
        StatusBarUtils.setStatusBarColor(this)
//        setBottomNavView()
//        setListener()
//        setPref()
//        setFeature()
//        setObserver()
//        getNotification()
//        setAnimation()
    }

    private fun checkForUpdate() {
        appUpdateManager = AppUpdateManagerFactory.create(this)
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo

        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE &&
                appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
            ) {
                dialogUpdate(appUpdateInfo)
            }
        }
    }
    private fun dialogUpdate(appUpdateInfo: AppUpdateInfo) {
        val binding: LayoutDialogWarningBinding = LayoutDialogWarningBinding.inflate(layoutInflater)
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setView(binding.root)
        val dialog: AlertDialog = builder.create()
        binding.apply {
            labelTitle.text = getString(R.string.title_update)
            labelMessage.text = getString(R.string.message_update)
            buttonNo.text = getString(R.string.next_time)
            buttonNo.setOnClickListener {
                dialog.dismiss()
            }
            buttonYes.text = getString(R.string.update)
            buttonYes.setOnClickListener {
                startUpdateFlow(appUpdateInfo)
                dialog.dismiss()
            }
        }
        dialog.setCancelable(true)
        dialog.show()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }
    private fun startUpdateFlow(appUpdateInfo: AppUpdateInfo) {
        appUpdateManager.startUpdateFlowForResult(
            appUpdateInfo,
            AppUpdateType.IMMEDIATE,
            this,
            REQUEST_CODE_UPDATE
        )
    }

    private fun pressAgainToExit() {
        onBackPressedDispatcher.addCallback(this, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val currentTime = System.currentTimeMillis()
                val timeDifference = currentTime - backPressedTime

                if (timeDifference < 2000) { // Exit if the back button is pressed within 2 seconds
                    finishAffinity() // Close all activities and exit the app
                } else {
                    backPressedTime = currentTime
                    Toast.makeText(this@MainActivity, "Tekan sekali lagi untuk keluar", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

//    private fun setBottomNavView() {
//        val navView: BottomNavigationView = binding.bottomNavView
//        binding.bottomNavView.background = null
//
//        val navController = findNavController(R.id.nav_host_fragment_activity_main)
//        navView.setupWithNavController(navController)
//    }

    companion object {
        private const val REQUEST_CODE_UPDATE = 123
    }
}