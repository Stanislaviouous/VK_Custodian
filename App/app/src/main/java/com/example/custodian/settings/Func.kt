package com.example.custodian.settings

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.custodian.R
import com.example.custodian.data.SFile
import java.io.File
import java.nio.file.Files
import java.nio.file.Files.readAttributes
import java.nio.file.Paths
import java.nio.file.attribute.BasicFileAttributes

class Func {
    val UpName = 0
    val UpSize = 1
    val DownSize = 2
    val DownDate = 3
    val UpType = 4



    // Функция определения иконки по типу файла
    fun determineTypeIcon(extension: String): Int {
        return when (extension) {
            "" -> R.drawable.ic_app
            "mp3" ->  R.drawable.icons8___mp3_48
            "txt" ->  R.drawable.icons8__txt_48
            "avi" ->  R.drawable.icons8_avi_48
            "doc", "docx" ->  R.drawable.icons8_doc_48
            "jpg" ->  R.drawable.icons8_jpg_48
            "pdf" ->  R.drawable.icons8_pdf_48
            "png" ->  R.drawable.icons8_png_48
            "xls", "xlsx", "csv" ->  R.drawable.icons8_xls_48
            "xml" ->  R.drawable.icons8_xml__48
            "zip", "rar" ->  R.drawable.icons8_zip_48
            else -> {
                R.drawable.icons8_file_48
            }
        }
    }

    fun determineLen(file: File): Long {
        if (file.isDirectory) {
            if (file.listFiles() != null) {
                return file.listFiles().size.toLong()
            }
            return -1
        }
        return file.length()
    }

    // Отлаженная функия создания обьектов SFile из обычных File
    @RequiresApi(Build.VERSION_CODES.O)
    fun cloneFilesToSFiles(list: List<File>): ArrayList<SFile> {
        var a = ArrayList<SFile>()
        for (i in list) {
            val len = determineLen(i)
            a.add(SFile(
                i.nameWithoutExtension,
                i.extension,
                determineLen(i),
                Files.readAttributes(i.toPath(), BasicFileAttributes::class.java).creationTime().toMillis(),
                (if(File(i.absolutePath).isDirectory) R.drawable.ic_app else determineTypeIcon(i.extension)),
                i.absolutePath))
        }
        return a
    }
    //



    // Отлаженная функция поиска всех файлов в папке и подпапках
    // Способна обрабатывать различные виды ошибок и исключений
    fun getAllFilesInDir(path: String): List<File> {
        return try {
            File(path).listFiles().toList()
        } catch (ex: Throwable) {
            when (ex) {
                is Exception, is Error -> ArrayList<File>()
                else -> ArrayList<File>()
            }
        }
    }
    //

    // Отлаженная функция сортировки
    fun sortSFiles(x: ArrayList<SFile>, type: Int ): List<SFile> {
        return when (type) {
            UpName -> x.sortedBy { it.name }
            UpSize -> (x.sortedBy { it.size }.filter { File(it.link).isDirectory } + x.sortedBy { it.size }.filter { File(it.link).isFile })
            DownSize -> (x.sortedBy { it.size }.reversed().filter { File(it.link).isDirectory } + x.sortedBy { it.size }.reversed().filter { File(it.link).isFile })
            DownDate -> x.sortedBy { it.date }.reversed()
            UpType -> x.sortedBy { it.type }
            else -> { ArrayList<SFile>() }
        }
    }
    //

}




//    // Функция определения иконки по типу файла
//    fun determineTypeIcon(extension: String): String {
//        return when (extension) {
//            "mp3" -> "./res/drawable/icons8___mp3_48.png"
//            "txt" -> "./res/drawable/icons8__txt_48.png"
//            "avi" -> "./res/drawable/icons8_avi_48.png"
//            "doc", "docx" -> "./res/drawable/icons8_doc_48"
//            "jpg" -> "./res/drawable/icons8_jpg_48.png"
//            "pdf" -> "./res/drawable/icons8_pdf_48.png"
//            "png" -> "./res/drawable/icons8_png_48.png"
//            "xls", "xlsx", "csv" -> "./res/drawable/icons8_xls_48.png"
//            "xml" -> "./res/drawable/icons8_xml__48.png"
//            "zip", "rar" -> "./res/drawable/icons8_zip_48.png"
//            else -> {
//                ".res/drawable/icons8_file_48.png"
//            }
//        }
//    }
//