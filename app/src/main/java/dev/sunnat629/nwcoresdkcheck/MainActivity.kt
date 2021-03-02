package dev.sunnat629.nwcoresdkcheck

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.nativewaves.coresdk.exceptions.CustomException
import com.nativewaves.coresdk.models.program.NwProgram
import com.nativewaves.coresdk.ui.base.AlertDialogListener
import com.nativewaves.coresdk.ui.base.BaseActivity
import com.nativewaves.sportsWorldSdk.NwSportsWorldListener
import com.nativewaves.sportsWorldSdk.SportsWorldSdk
import com.nativewaves.sportsWorldSdk.SportsWorldSdkImpl
import dev.sunnat629.nwcoresdkcheck.databinding.ActivityMainBinding

class MainActivity : BaseActivity() {

    private val sdk: SportsWorldSdk = SportsWorldSdkImpl.Builder(this).build()

    private var isFirst: Boolean = true

    private val binding: ActivityMainBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_main)
    }

    private val sportsWorldListener = object : NwSportsWorldListener {
        override fun onInitialized() {
            Log.d(LOG_TAG, "onInitialized")
        }

        override fun onSportsWorldLoading(programId: String?) {
            Log.d(LOG_TAG, "onSportsWorldLoading. ProgramId = $programId")
        }

        override fun onSportsWorldLoaded(sportsWorld: NwProgram?) {
            Log.d(LOG_TAG, "onSportsWorldLoaded.")
           if (isFirst){
               sdk.startNativeWavesExperience(this@MainActivity)
               isFirst = false
           }
        }

        override fun onError(ex: CustomException?) {
            showAlertDialog(ex = ex, listener = object : AlertDialogListener {
                override fun clickOnCancelListener(dialog: DialogInterface?) {
                    dialog?.dismiss()
                    Log.i(LOG_TAG, "clickOnCancelListener")
                }

                override fun clickNegativeButton(dialog: DialogInterface?) {
                    dialog?.dismiss()
                    Log.i(LOG_TAG, "clickNegativeButton")
                }

                override fun clickPositiveButton(dialog: DialogInterface?) {
                    dialog?.dismiss()
                    Log.i(LOG_TAG, "clickPositiveButton")
                }
            })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sdk.initialize(sportsWorldListener)

        val appId: String? = sdk.coreSdk.appId
        binding.lifecycleOwner = this
        binding.display.text = appId

        Log.e("ASDF", "appId: $appId")
        Log.e("ASDF", "appId: ${sdk.getAppConfig().toString()}")

        binding.loadProgram.setOnClickListener {
            sdk.loadProgram("02248df0cefe4375b2619a7a7d830475")
        }
    }

    companion object {
        private val LOG_TAG = MainActivity::class.java.simpleName
    }
}