package com.example.lirogram

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.media.Image
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsetsController
import android.widget.Adapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import com.example.lirogram.LanguageHelper
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import android.content.SharedPreferences
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Looper
import android.text.Editable
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextWatcher
import android.text.style.DynamicDrawableSpan
import android.text.style.ImageSpan
import android.view.KeyEvent
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.Button
import androidx.constraintlayout.widget.Placeholder
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.graphics.drawable.toDrawable
import androidx.core.os.postDelayed
import androidx.core.text.set
import com.airbnb.lottie.Lottie
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.LottieCompositionFactory
import com.airbnb.lottie.LottieDrawable
import com.airbnb.lottie.LottieTask
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.transition.Transition
import com.example.lirogram.R.drawable.toast_boder
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
import java.util.logging.Handler

class MainActivity : AppCompatActivity() {
    private lateinit var adapter: NoteAdapter
    private lateinit var clickvert: ImageView
    private lateinit var behavior: BottomSheetBehavior<LinearLayout>
    private lateinit var titlename: TextView
    protected lateinit var dialog : BottomSheetDialog
    private lateinit var emtyview: LinearLayout
    private lateinit var recyclerview: RecyclerView
    private lateinit var language: ImageView
    private lateinit var lienarhome: LinearLayout
    private var notesList = mutableListOf<Notes>()

    private val PREFS_NAME = "theme_prefs"
    private val PREFS_KEY = "prefs_key"

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)



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


        clickvert = findViewById(R.id.clickvert)
        emtyview = findViewById(R.id.emtyview)
        titlename = findViewById(R.id.titlename)
        lienarhome = findViewById(R.id.lienarhome)
        recyclerview = findViewById(R.id.recyclerview)
         language = findViewById(R.id.language)

        language.setOnClickListener{view ->
            bottomsheetlang(view)
        }




        val sharedPreferences = getSharedPreferences(PREFS_NAME,MODE_PRIVATE)
        val color = sharedPreferences.getInt(PREFS_KEY, ContextCompat.getColor(this,R.color.colorapp))
        lienarhome.setBackgroundColor(color)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = color
        }






        notesList.addAll(loadNotes())
        adapter = NoteAdapter(notesList,notesList,::saveNotes,::updateUI)
        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.adapter = adapter
        updateUI()



        clickvert.setOnClickListener { view ->
            PopupWindow(view)
        }

    }



    // set language persian and english for app ...


    @SuppressLint("MissingInflatedId")
    private fun bottomsheetlang(view: View) {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.activity_lang,null)

        val linesen: LinearLayout = view.findViewById(R.id.linesen)
        val linesfa: LinearLayout = view.findViewById(R.id.linesfa)
        val linesrus: LinearLayout = view.findViewById(R.id.linesrus)
        val borderline: LinearLayout = view.findViewById(R.id.borderline)
        val linenew: LinearLayout = view.findViewById(R.id.linenew)




        val sharedPreferences = getSharedPreferences("theme_prefs",MODE_PRIVATE)
        val color = sharedPreferences.getInt("prefs_key", ContextCompat.getColor(this,R.color.colorapp))


        val border = borderline.background as GradientDrawable
        val border2 = linenew.background as GradientDrawable

        border2.setColor(color)

        border.setColor(color)


        linesrus.setOnClickListener{view ->
            changeLanguage("rus")
        }

        linesen.setOnClickListener{view ->
         changeLanguage("en")
        }

        linesfa.setOnClickListener{view ->
           changeLanguage("fa")
        }



        dialog.setContentView(view)
        dialog.show()

    }

    private fun changeLanguage(lang: String) {
        LanguageHelper.saveLanguage(this, lang)
        val intent = intent
        finish()
        startActivity(intent)
    }
    @SuppressLint("MissingInflatedId", "ResourceType")
    private fun showtoastsheet(message: String) {
        val view = LayoutInflater.from(this).inflate(R.layout.activity_toast, null)

        val sharedPreferences = getSharedPreferences("theme_prefs",MODE_PRIVATE)
            val color = sharedPreferences.getInt("prefs_key", ContextCompat.getColor(this,R.color.colorapp))

        val toastlines: LinearLayout = view.findViewById(R.id.toastlines)
            val border = toastlines.background as GradientDrawable

        border.setColor(color)

        val text: TextView = view.findViewById(R.id.text)
        text.text = message

        val animation = AnimationUtils.loadAnimation(this,R.anim.siled_right_in)
        view.startAnimation(animation)

        val toast = Toast(applicationContext)
        toast.duration = Toast.LENGTH_SHORT
        toast.view = view
        toast.show()
    }



    @SuppressLint("MissingInflatedId")
    private fun PopupWindow(anchor: View) {
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView = inflater.inflate(R.layout.activity_over1, null)

        val popupWindow = PopupWindow(
            popupView,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT, true
        )

        popupWindow.elevation = 5f
        popupWindow.isOutsideTouchable = true
        popupWindow.isFocusable = true

        val clickadd: LinearLayout = popupView.findViewById(R.id.clickadd)
        val clickabout: LinearLayout = popupView.findViewById(R.id.clickabout)
        val clickcolor: LinearLayout = popupView.findViewById(R.id.clickcolor)

        clickcolor.setOnClickListener { view ->
            bottomsheettheme(view)
            popupWindow.dismiss()
        }





        clickabout.setOnClickListener { view ->
            bottomsheetdialog2(view)
            popupWindow.dismiss()
        }


        clickadd.setOnClickListener { view ->
            bottomsheetdialog(view)
            popupWindow.dismiss()
        }

        popupWindow.showAsDropDown(anchor, 0, 10)

    }

    override fun attachBaseContext(newBase: Context) {

        val config = Configuration(newBase.resources.configuration)
        config.fontScale = 1.0f

        val lang = LanguageHelper.getSavedLanguage(newBase)
        val newContext = LanguageHelper.setLocale(newBase, lang)
        super.attachBaseContext(newContext)
    }

    @SuppressLint("InflateParams", "MissingInflatedId")
    private fun bottomsheettheme(view: View) {
        var dialog = BottomSheetDialog(this)
        val view = dialog.layoutInflater.inflate(R.layout.activity_delete,null)

        val colorapp: LinearLayout = view.findViewById(R.id.colorapp)
        val colorapp2: LinearLayout = view.findViewById(R.id.colorapp2)
        val colorapp3: LinearLayout = view.findViewById(R.id.colorapp3)
        val colorapp4: LinearLayout = view.findViewById(R.id.colorapp4)
        val colorapp5: LinearLayout = view.findViewById(R.id.colorapp5)
        val colorapp6: LinearLayout = view.findViewById(R.id.colorapp6)
        val colorapp7: LinearLayout = view.findViewById(R.id.colorapp7)
        val colorapp8: LinearLayout = view.findViewById(R.id.colorapp8)
        val scrollline: LinearLayout = view.findViewById(R.id.scrollline)

        val borderline: LinearLayout = view.findViewById(R.id.borderline)

        val sharedPreferences = getSharedPreferences("theme_prefs",MODE_PRIVATE)
        val color = sharedPreferences.getInt("prefs_key", ContextCompat.getColor(this,R.color.colorapp))

        val border = borderline.background as GradientDrawable

        border.setColor(color)



        scrollline.setOnTouchListener{view,event->
            view.parent.requestDisallowInterceptTouchEvent(true)
            false
        }





        colorapp.setOnClickListener{view ->
           saveThemeColor(ContextCompat.getColor(this,R.color.colorapp))
            val animation = AnimationUtils.loadAnimation(this,R.anim.siled_in_right)
            val animation2 = AnimationUtils.loadAnimation(this,R.anim.siled_left_in)
            titlename.startAnimation(animation2)
            clickvert.startAnimation(animation)
            language.startAnimation(animation)
            dialog.dismiss()
        }

        colorapp2.setOnClickListener{view ->
  saveThemeColor(ContextCompat.getColor(this,R.color.colorapp2))
            val animation = AnimationUtils.loadAnimation(this,R.anim.siled_in_right)
            val animation2 = AnimationUtils.loadAnimation(this,R.anim.siled_left_in)
            titlename.startAnimation(animation2)
            clickvert.startAnimation(animation)
            language.startAnimation(animation)
            dialog.dismiss()
        }

        colorapp3.setOnClickListener{view ->
            saveThemeColor(ContextCompat.getColor(this,R.color.colorapp3))
            val animation = AnimationUtils.loadAnimation(this,R.anim.siled_in_right)
            val animation2 = AnimationUtils.loadAnimation(this,R.anim.siled_left_in)
            titlename.startAnimation(animation2)
            clickvert.startAnimation(animation)
            language.startAnimation(animation)
            dialog.dismiss()
        }

        colorapp4.setOnClickListener{view ->
            saveThemeColor(ContextCompat.getColor(this,R.color.colorapp4))
            val animation = AnimationUtils.loadAnimation(this,R.anim.siled_in_right)
            val animation2 = AnimationUtils.loadAnimation(this,R.anim.siled_left_in)
            titlename.startAnimation(animation2)
            clickvert.startAnimation(animation)
            language.startAnimation(animation)
            dialog.dismiss()
        }

        colorapp5.setOnClickListener{view ->
            saveThemeColor(ContextCompat.getColor(this,R.color.colorapp5))
            val animation = AnimationUtils.loadAnimation(this,R.anim.siled_in_right)
            val animation2 = AnimationUtils.loadAnimation(this,R.anim.siled_left_in)
            titlename.startAnimation(animation2)
            clickvert.startAnimation(animation)
            language.startAnimation(animation)
            dialog.dismiss()
        }

        colorapp6.setOnClickListener{view ->
            saveThemeColor(ContextCompat.getColor(this,R.color.colorapp6))
            val animation = AnimationUtils.loadAnimation(this,R.anim.siled_in_right)
            val animation2 = AnimationUtils.loadAnimation(this,R.anim.siled_left_in)
            titlename.startAnimation(animation2)
            clickvert.startAnimation(animation)
            language.startAnimation(animation)
            dialog.dismiss()
        }

        colorapp7.setOnClickListener{view ->
            saveThemeColor(ContextCompat.getColor(this,R.color.colorapp7))
            val animation = AnimationUtils.loadAnimation(this,R.anim.siled_in_right)
            val animation2 = AnimationUtils.loadAnimation(this,R.anim.siled_left_in)
            titlename.startAnimation(animation2)
            clickvert.startAnimation(animation)
            language.startAnimation(animation)
            dialog.dismiss()
        }

        colorapp8.setOnClickListener{view ->
            saveThemeColor(ContextCompat.getColor(this,R.color.colorapp8))
            val animation = AnimationUtils.loadAnimation(this,R.anim.siled_in_right)
            val animation2 = AnimationUtils.loadAnimation(this,R.anim.siled_left_in)
            titlename.startAnimation(animation2)
            clickvert.startAnimation(animation)
            language.startAnimation(animation)
            dialog.dismiss()
        }

        dialog.setContentView(view)
        dialog.show()
    }

    private fun saveThemeColor(color: Int) {
        val sharedPreferences = getSharedPreferences(PREFS_NAME,MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt(PREFS_KEY, color)
        editor.apply()
        lienarhome.setBackgroundColor(color)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = color
        }

        findViewById<LinearLayout>(R.id.linearlayout)?.setBackgroundColor(color)

    }




    @SuppressLint("MissingInflatedId", "InflateParams")
    private fun bottomsheetdialog(view: View) {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.activity_dialog, null)

        val edittitle: EditText = view.findViewById(R.id.edittitle)
        val editcaption: EditText = view.findViewById(R.id.editcaption)
        val sendnote: ImageView = view.findViewById(R.id.sendnote)




        editcaption.setOnTouchListener { view, event ->
            view.parent.requestDisallowInterceptTouchEvent(true)
            false
        }





        sendnote.setOnClickListener {
            val title = edittitle.text.toString().trim()
            val caption = editcaption.text.toString().trim()

            val Context = LanguageHelper.setLocale(this, LanguageHelper.getSavedLanguage(this))
            val message = Context.getString(R.string.toast)
            val message2 = Context.getString(R.string.toast2)

            if (title.isNotEmpty() && caption.isNotEmpty()) {
                val newNote = Notes(title, caption, System.currentTimeMillis().toString())
                notesList.add(newNote)
                adapter.notifyItemInserted(notesList.size - 1)
                adapter.notifyDataSetChanged()
                saveNotes()
                updateUI()
                edittitle.text.clear()
                editcaption.text.clear()
                showtoastsheet(message)
                updateUI()
                dialog.dismiss()
            } else {
                showtoastsheet(message2)
            }

        }


        dialog.setContentView(view)
        dialog.show()
    }


    private fun bottomsheetdialog2(view: View) {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.activity_about_dialog,null)

        val closedialog: LinearLayout = view.findViewById(R.id.closedialog)

            val border = closedialog.background as GradientDrawable
        val sharedPreferences = getSharedPreferences("theme_prefs",MODE_PRIVATE)
        val color = sharedPreferences.getInt("prefs_key", ContextCompat.getColor(this,R.color.colorapp))
        border.setColor(color)

        closedialog.setOnClickListener{view ->
            dialog.dismiss()
        }

        dialog.setContentView(view)
        dialog.show()
    }


    private fun saveNotes() {
        val sharedPreferences = getSharedPreferences("notes_pref",MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(notesList)
        editor.putString("notes_key",json)
        editor.apply()
    }

    private fun loadNotes(): MutableList<Notes> {
        val sharedPreferences = getSharedPreferences("notes_pref", MODE_PRIVATE)
        val json = sharedPreferences.getString("notes_key", null)
        val type = object : TypeToken<MutableList<Notes>>() {}.type
       return  if (json != null) {
                Gson().fromJson(json, type)
            } else {
                mutableListOf()
            }
    }



    @SuppressLint("SuspiciousIndentation")
    private fun updateUI() {
        if (notesList.isNotEmpty()) {
            emtyview.visibility = View.GONE
            recyclerview.visibility = View.VISIBLE
            val animation = AnimationUtils.loadAnimation(this,R.anim.siled_in_right)

            recyclerview.startAnimation(animation)
        } else {

            emtyview.visibility = View.VISIBLE
            recyclerview.visibility = View.GONE

        val animtion = AnimationUtils.loadAnimation(this,R.anim.siled_in_right)
            emtyview.startAnimation(animtion)

        }
    }

}