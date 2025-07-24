package com.example.lirogram

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Context.*
import android.content.Intent
import android.net.Uri
import android.view.ContextThemeWrapper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

class NoteAdapter(private val notes: List<Notes>,private val notesList: MutableList<Notes>,private val onDataChanged: () -> Unit,private val onSave: () -> Unit) : RecyclerView.Adapter<NoteAdapter.ViewHolder>() {
    private lateinit var emtyview: LinearLayout
    private lateinit var recyclerview: RecyclerView


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.title)
        val caption: TextView = view.findViewById(R.id.caption)
        val clickvert2: ImageView = view.findViewById(R.id.clickvert2)
        val linesnote: LinearLayout = view.findViewById(R.id.linesnote)
        val timeclick: TextView = view.findViewById(R.id.timeclick)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.activity_notes, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val note = notesList[position]
        holder.title.text = note.title
        holder.caption.text = note.caption




        try {


            if (note.date.isNotEmpty()) {
                val dateFormat =
                    java.text.SimpleDateFormat("yyyy-MM-HH:mm", java.util.Locale.getDefault())
                val dateString = dateFormat.format(java.util.Date(note.date.toLong()))
                holder.timeclick.text = dateString
            } else {
                holder.timeclick.text = "No Date"
            }
        } catch (e: Exception) {
            holder.timeclick.text = "No Date"
        }

        holder.clickvert2.setOnClickListener { view ->
            dialogwindow(view, position)
        }

        holder.timeclick.visibility = View.VISIBLE

        if (note.caption.length > 45) {

            val shortText = note.caption.substring(0,30) + " ..."
            holder.caption.text = shortText

            holder.linesnote.setOnClickListener{view ->
                val note = notes[position]
                val intent = Intent(holder.itemView.context, MainActivity2::class.java)
                intent.putExtra("note_title",note.title)
                intent.putExtra("note_caption",note.caption)

           if (view.context is Activity) {
               val activity = view.context as Activity
               activity.overridePendingTransition(R.anim.siled_right_in,R.anim.siled_out_left)
            activity.finish()
           }


                holder.itemView.context.startActivity(intent)

            }
        }

    }

    @SuppressLint("MissingInflatedId")
    private fun dialogwindow(anchor: View, position: Int) {
        val inflater = LayoutInflater.from(anchor.context)
        ContextThemeWrapper(anchor.context, R.style.PopupMenuStyle)
        val popupView = inflater.inflate(R.layout.activity_over2, null)

        // ساخت PopupWindow
        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        popupWindow.elevation = 2f
        popupWindow.isFocusable = true
        popupWindow.isOutsideTouchable = true

        val deletenote: LinearLayout = popupView.findViewById(R.id.deletenote)
        val clickadd: LinearLayout = popupView.findViewById(R.id.clickadd)
        val pinnote: LinearLayout = popupView.findViewById(R.id.pinnote)

        pinnote.setOnClickListener{view ->
            Toast.makeText(view.context,"Coming Soon ...", Toast.LENGTH_SHORT).show()
            popupWindow.dismiss()
        }

        clickadd.setOnClickListener{view ->
            showedittitle(view,position)
            popupWindow.dismiss()
        }

        deletenote.setOnClickListener { view ->
            deletenotes(view,position)
            popupWindow.dismiss()
        }

      popupWindow.showAsDropDown(anchor,10,0)

    }


    @SuppressLint("MissingInflatedId")
    private fun showedittitle(view: View, position: Int) {
        val dialog = BottomSheetDialog(view.context)
        val view = dialog.layoutInflater.inflate(R.layout.activity_edit,null)

        val edittitle: EditText = view.findViewById(R.id.edittitle)
        val editcaption: EditText = view.findViewById(R.id.editcaption)
        val sendnote: ImageView = view.findViewById(R.id.sendnote)

        val note = notes[position]
        edittitle.setText(note.title)
        editcaption.setText(note.caption)

        sendnote.setOnClickListener{
            val updatetitle = edittitle.text.toString().trim()
            val updateCaption = editcaption.text.toString().trim()

            if (updatetitle.isNotEmpty() && updateCaption.isNotEmpty()) {
                notesList[position] = Notes(updatetitle,updateCaption)
                notifyItemChanged(position)
                onSave()
                onDataChanged()
                dialog.dismiss()
            }
            }


        editcaption.setOnTouchListener{view,event->
            view.parent.requestDisallowInterceptTouchEvent(true)
            false
        }


        dialog.setContentView(view)
        dialog.show()
    }

    @SuppressLint("MissingInflatedId")
 private fun deletenotes(view: View,position: Int) {
            val dialog = Dialog(view.context)
        dialog.setContentView(R.layout.activity_dialog_delete)

        val sharedPreferences = view.context.getSharedPreferences("theme_prefs",MODE_PRIVATE)
        val color = sharedPreferences.getInt("prefs_key", ContextCompat.getColor(view.context,R.color.colorapp))


        dialog.window?.setBackgroundDrawableResource(R.drawable.bordershape_dialog)


        val deleten: TextView = dialog.findViewById(R.id.deleten)
        val closedialogg: TextView = dialog.findViewById(R.id.closedialogg)

        deleten.setTextColor(color)

        deleten.setOnClickListener{view ->
            deletenote(view, position)
            dialog.dismiss()
        }

        closedialogg.setOnClickListener{viee ->
            dialog.dismiss()
        }
        dialog.setCancelable(true)
        dialog.show()
 }
    fun deletenote(view: View, position: Int) {
        if (position >= 0 && position < notesList.size) {
            val notes = notes[position]
            notesList.removeAt(position)
            notesList.toMutableList().remove(notes)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position,notesList.size)
            onSave()
            onDataChanged()
        }
    }

    override fun getItemCount(): Int = notesList.size


}