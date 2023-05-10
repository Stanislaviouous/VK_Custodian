package com.example.custodian.exout

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.custodian.R
import com.example.custodian.data.SFile
import com.example.custodian.settings.Func
import java.util.*
import android.text.format.Formatter;
import android.view.View
import java.io.File
import java.nio.file.Files
import java.text.SimpleDateFormat

class SFileAdapter(context: Context, listOfSFile: ArrayList<SFile>, listener: OnSFileSelectListener) : RecyclerView.Adapter<SFileViewHolder>() {
    private val context: Context
    private val listOfSFile: ArrayList<SFile>
    private val listener: OnSFileSelectListener
    val funcTools = Func()
    var vid = 0

    init {
        this.context = context
        this.listOfSFile = listOfSFile
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SFileViewHolder {
        return SFileViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_sfile, parent, false)
        )
    }

    override fun onBindViewHolder(holder: SFileViewHolder, position: Int) {
        var sfile = listOfSFile[position]
        holder.name.text = File(sfile.link).name
        holder.size.text = if(File(sfile.link).isDirectory) "${sfile.size} файлов |" else "${Formatter.formatShortFileSize(context, sfile.size)} |"
        holder.imageType.setImageResource(sfile.typeOfIcon)
        holder.date.text = "${SimpleDateFormat("dd.MM.YY HH:mm").format(sfile.date).trim()}"

        holder.container.setOnClickListener{
            listener.onSFilePressed(listOfSFile[position])
        }
        holder.container.setOnLongClickListener (View.OnLongClickListener {
            listener.onSFileLongPressed(listOfSFile[position])
            true
        })

    }
    override fun getItemCount(): Int {
        return listOfSFile.size
    }
}


//        if (file[position].isDirectory) {
//            val files = file[position].listFiles()
//            for (singleFile in files) {
//                if (!singleFile.isHidden) {
//                    items += 1
//                }
//            }
//            holder.tvfileSize.setText("$items Files")
//        } else holder.tvfileSize.setText(
//            Formatter.formatShortFileSize(
//                context,
//                file[position].length()
//            )
//        )
//

//        holder.tvName.setSelected(true)
//

