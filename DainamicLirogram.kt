package com.example.daunamic

import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.setPadding
import com.google.android.material.bottomsheet.BottomSheetDialog

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val linearlayout = LinearLayout(this)
        linearlayout.orientation = LinearLayout.VERTICAL
        linearlayout.setPadding(8,8,8,8)
        linearlayout.setBackgroundColor(Color.BLACK)
        linearlayout.fitsSystemWindows = true
        linearlayout.gravity = Gravity.CENTER
        linearlayout.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        val textview1 = TextView(this)
        textview1.text = getString(R.string.app_name)
        textview1.setTextColor(Color.WHITE)
        textview1.setTypeface(null, Typeface.BOLD)
        textview1.gravity = Gravity.CENTER
        textview1.textSize = 15f

        textview1.setOnClickListener{view ->
            Toast.makeText(this,getString(R.string.welcome), Toast.LENGTH_SHORT).show()
        }

        linearlayout.addView(textview1)

        val button = Button(this)
        button.text = "Click me!"
        button.textSize = 12f
        button.gravity = Gravity.CENTER
        button.setBackgroundColor(Color.parseColor("#000000"))

        textview1.setOnClickListener{view ->
            val bottom = BottomSheetDialog(this)

            val layout1 = LinearLayout(this)
            layout1.gravity = Gravity.CENTER
            layout1.setPadding(24,24,24,24)
            layout1.setBackgroundColor( Color.parseColor("#eeeeee"))
            layout1.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT,
            )

            val edittext = EditText(this)

            val parms = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT,
            )

                    val drawable = GradientDrawable()
            drawable.cornerRadius = 35f
            drawable.setColor(Color.WHITE)

            edittext.elevation = 5f
            parms.setMargins(24,24,24,24)
            edittext.layoutParams = parms
            edittext.background = drawable
            edittext.hint = "Please typing ..."
                edittext.gravity = Gravity.CENTER
            edittext.setBackgroundColor(Color.WHITE)
            edittext.textSize = 12f
            edittext.setPadding(24,24,24,24)
            layout1.addView(edittext)


            bottom.setContentView(layout1)
            bottom.show()
        }


        setContentView(linearlayout)

    }
}
