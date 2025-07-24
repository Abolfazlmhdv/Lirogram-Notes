package com.example.lirogram

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsetsController
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.NestedScrollView
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

class MainActivity2 : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main2)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.colorapp)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = 0
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.setSystemBarsAppearance(
                0,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = 0
        }



        val caption = intent.getStringExtra("note_caption") ?: ""
        val title = intent.getStringExtra("note_title") ?: ""
        val captiontext: TextView = findViewById(R.id.captiontext)
        val eleflines: LinearLayout = findViewById(R.id.eleflines)
        val titletext: TextView = findViewById(R.id.titletext)
        val backclick: ImageView = findViewById(R.id.backclick)
        val linearlayout: LinearLayout = findViewById(R.id.linearlayout)

        // set textcolor for messgae note ...


        eleflines.elevation = 2f



           val sharedPreferences = getSharedPreferences("theme_prefs",MODE_PRIVATE)
        val color = sharedPreferences.getInt("prefs_key", ContextCompat.getColor(this,R.color.colorapp))

        linearlayout.setBackgroundColor(color)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = color
        }


        backclick.setOnClickListener{view ->
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
//            fun finish() {
//                   super.finish()
//                   overridePendingTransition(R.anim.siled_in_left,R.anim.siled_out_right)
//               }

            finish()



        }


//        this  is code for activity_over3 and mainactivity2
        val clickvert1: ImageView = findViewById(R.id.clickvert1)

        clickvert1.setOnClickListener{view ->
            popupview(view)

              
        }

        captiontext.text = caption
        titletext.text = title
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.siled_out_left,R.anim.siled_in_right)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("InflateParams")
    private fun popupview(anchor: View ) {
        val view = LayoutInflater.from(this)
        val pupupview = layoutInflater.inflate(R.layout.activity_over3,null)

        val popupWindow = PopupWindow(pupupview,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,true
        )

        popupWindow.elevation = 1f

        val selecttext: LinearLayout = pupupview.findViewById(R.id.selecttext)

        selecttext.setOnTouchListener{view,event->
            view.parent.requestDisallowInterceptTouchEvent(true)
            false
        }

        selecttext.setOnClickListener{view ->
            selectbottomsheet(view)
            popupWindow.dismiss()
        }

        popupWindow.showAsDropDown(anchor,0,10)
    }
    override fun attachBaseContext(newBase: Context) {

        val config = Configuration(newBase.resources.configuration)
        config.fontScale = 1.0f

        val lang = LanguageHelper.getSavedLanguage(newBase)
        val newContext = LanguageHelper.setLocale(newBase, lang)
        super.attachBaseContext(newContext)
    }
    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("InflateParams")
    private fun selectbottomsheet(view: View) {
        val dialog = BottomSheetDialog(this,R.style.AppBottomSheetDialogTheme)
        val dialogView = layoutInflater.inflate(R.layout.activity_selectxt, null)

        val selecttext: TextView = dialogView.findViewById(R.id.selecttext)
        val closetext: ImageView = dialogView.findViewById(R.id.closetext)

        closetext.setOnClickListener {
            dialog.dismiss()
        }

        val caption = intent.getStringExtra("note_caption") ?: ""
        selecttext.text = caption

        dialog.setContentView(view)
        dialog.show()
    }
}
