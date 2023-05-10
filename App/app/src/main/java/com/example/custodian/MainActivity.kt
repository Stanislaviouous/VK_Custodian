package com.example.custodian

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment.getExternalStorageDirectory
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.custodian.data.SFile
import com.example.custodian.exout.OnSFileSelectListener
import com.example.custodian.exout.SFileAdapter
import com.example.custodian.settings.Func
import com.google.android.material.navigation.NavigationView
import java.io.File


// Environment.getExternalStorageDirectory();
class MainActivity : AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener, OnSFileSelectListener,
    PopupMenu.OnMenuItemClickListener {

    lateinit var allPath: TextView
    val listVariableKey = "LIST_VARIABLE"
    val pathVariableKey = "PATH_VARIABLE"

    val funcTool = Func()

    var permissionGranted = false
    var permissions = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.MANAGE_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)

    var path = getExternalStorageDirectory().absolutePath
    var list = ArrayList<SFile>()
    lateinit var recycler: RecyclerView
    lateinit var adapter: SFileAdapter
    var items = arrayOf("Открыть", "Удалить", "Поделиться")
    lateinit var horisontalScroll: HorizontalScrollView

    val map: HashMap<String, Int> = mapOf(
        "По названию" to 0,
        "По возрастанию размера" to 1,
        "По убыванию размера" to 2,
        "По датте" to 3,
        "По типу" to 4
    ) as HashMap<String, Int>

    lateinit var button: ImageButton
    public var file: File = File("")
    public lateinit var textView: TextView

    private lateinit var drawerLayout: DrawerLayout
    lateinit var navView: NavigationView

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        permissionGranted = ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED

        if(!permissionGranted) {
            ActivityCompat.requestPermissions( this, permissions, 200)
        }

        drawerLayout = findViewById(R.id.draw)
        navView = findViewById(R.id.nav_view)

        adapter = SFileAdapter(this, list, this)

        horisontalScroll = findViewById(R.id.horizontalScrollView)

        allPath = findViewById(R.id.all_path)
        allPath.text = path

        navView.setNavigationItemSelectedListener(this)
        button = findViewById(R.id.button)
        button.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        recycler = findViewById(R.id.recycler_view)

        display()

//        var imageButton:ImageView = findViewById(R.id.imageButton)
//        imageButton.setImageResource(funcTool.determineTypeIcon(File(getRootDirectory().absolutePath).listFiles().filter { it.isFile }[0].extension))
//        println(File(getRootDirectory().absolutePath).listFiles().filter { it.isFile }[0].absolutePath)
    }

    // Функции работы с меню сортировки
    @RequiresApi(Build.VERSION_CODES.O)
    fun pageUp(view: View) {
        val p = File(path).parentFile.absoluteFile.toString()
        if (p != File(getExternalStorageDirectory().absolutePath).parentFile.absolutePath.toString()) {
            path = p
            root()
            sortView(0)
            update()
        }
    }
    //

    // Функции работы с меню сортировки
    fun showMenu(view: View) {
        var menu = PopupMenu(this, view)
        menu.setOnMenuItemClickListener(this)
        val inf = menu.menuInflater
        inf.inflate(R.menu.main, menu.menu);
        menu.show()
    }

    override fun onMenuItemClick(item: MenuItem): Boolean {
        if (item != null) {
            sortView(map.get(item.title).toString().toInt())
            Toast.makeText(this, item.title, Toast.LENGTH_LONG).show()
        }
        return false
    }
    //

    // Сохранение и восстановление состояния
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putSerializable(listVariableKey, list)
        outState.putString(pathVariableKey, path)
        update()
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        list = savedInstanceState.getSerializable(listVariableKey) as ArrayList<SFile>
        adapter.notifyDataSetChanged()
        recycler.adapter = adapter
        val str = savedInstanceState.getString(pathVariableKey)
        allPath = findViewById(R.id.all_path)
        allPath.text = str
        sortView(0)
        update()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("NotifyDataSetChanged")
    override fun onRestart() {
        super.onRestart()
        list.clear()
        adapter.notifyDataSetChanged()
        val listOfFiles = ArrayList(funcTool.getAllFilesInDir(path))
        list.addAll(funcTool.cloneFilesToSFiles(listOfFiles))
        adapter.notifyDataSetChanged()
        sortView(0)
        update()
    }
    //

    // Методы вывода, обновления и сортировки элементов списка
    @RequiresApi(Build.VERSION_CODES.O)
    fun display(){
        recycler = findViewById(R.id.recycler_view)
        recycler.layoutManager = LinearLayoutManager(this);
        list = ArrayList<SFile>()
        adapter = SFileAdapter(this, list, this)
        root()
        sortView(0)
        recycler.adapter = adapter
    }

    fun update() {
        allPath.text = path
        horisontalScroll.scrollTo(0, 1)
        adapter.notifyDataSetChanged()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun root(){
        list.clear()
        list.addAll(funcTool.cloneFilesToSFiles(funcTool.getAllFilesInDir(path)))
        update()
    }

    fun sortView(type: Int) {
        var clone : List<SFile> = funcTool.sortSFiles(list, type).toList()
        list.clear()
        update()
        list.addAll(clone)
        update()
    }
    //

//    list.clear()
//
//    list.add(SFile("а", "", 10, 1111111111, R.drawable.icons8__txt_48, "./"))
//    list.add(SFile("и", "", 10, 1111111111, R.drawable.icons8__txt_48, "./"))
//    list.add(SFile("б", "", 10, 1111111111, R.drawable.icons8__txt_48, "./"))
//    list.add(SFile("в", "", 10, 1111111111, R.drawable.icons8__txt_48, "./"))
//    list.add(SFile("с", "", 10, 1111111111, R.drawable.icons8__txt_48, "./"))
//    list.add(SFile("с", "", 10, 1111111111, R.drawable.ic_app, "./"))
//    list.add(SFile("й", "", 10, 1111111111, R.drawable.ic_app, "./"))
//    list.add(SFile("о", "", 10, 1111111111, R.drawable.icons8__txt_48, "./"))
//    list.add(SFile("ш", "", 10, 1111111111, R.drawable.ic_app, "./"))
//    list.add(SFile("к", "", 10, 1111111111, R.drawable.ic_app, "./"))
//    list.add(SFile("е", "", 10, 1111111111, R.drawable.ic_app, "./"))
//    list.add(SFile("о", "", 10, 1111111111, R.drawable.ic_app, "./"))
//    list.add(SFile("л", "", 10, 1111111111, R.drawable.icons8__txt_48, "./"))



    // Функция запроса разрешения
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 200){
            permissionGranted = grantResults[0] == PackageManager.PERMISSION_GRANTED
        }
    }
    //

    // Обработка обычных и долгих нажатий
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onSFilePressed(file: SFile?) {
        if (file != null) {
            if (File(file.link).isDirectory) {
                path = file.link
                root()
                sortView(0)
                update()
            } else {
//                val uri: Uri = Uri.fromFile(File(file.link)).normalizeScheme()
//                val intent = Intent()
//                intent.action = Intent.ACTION_VIEW
//                intent.data = uri
//                this.startActivity(Intent.createChooser(intent, "Открыть файл"))
//                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
//                intent.type = "text/plain" //specify type of intent
//                intent.data = Uri.fromFile(File(file.link))
//                val chooser = Intent.createChooser(intent, "Открыть файл")
//                startActivity(chooser)
                val intent = Intent()
                intent.action = Intent.ACTION_VIEW
                intent.setDataAndType(Uri.fromFile(File(file.link)), "application/vnd.android.package-archive")
                startActivity(intent)
            }
        }
    }

    override fun onSFileLongPressed(file: SFile) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "application/pdf"
        intent.data = Uri.fromFile(File(file.link))
        val chooser = Intent.createChooser(intent, "Поделиться с помощью…")
        startActivity(chooser)
    }


    //


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        navView.setCheckedItem(item.itemId)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }
    override fun onBackPressed() {
        supportFragmentManager.popBackStackImmediate();
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed()
        }
    }


//    override fun onSFilePressed(file: SFile?) {
//        if () {
//            prevAdapter = fileAdapter
//            fileList = ArrayList()
//            fileList.addAll(findFiles(file))
//            fileAdapter = FileAdapter(this, fileList, this)
//            recyclerView.setAdapter(fileAdapter)
//            val new_path: String = file.getAbsolutePath()
//            tv_pathHolder.setText(new_path)
//        } else {
//            if (file != null) {
//                val uri: Uri = Uri.fromFile(File(file.link)).normalizeScheme()
//                val intent = Intent()
//                intent.action = Intent.ACTION_VIEW
//                intent.data = uri
//                this.startActivity(Intent.createChooser(intent, "Открыть файл"))
//            }
//        }
//    }
//
//    override fun onSFileLongPressed(file: SFile?) {
//
//    }



}



// Share
//        val intent = Intent(Intent.ACTION_SEND)
//        intent.type = "text/plain" //specify type of intent
//        val chooser = Intent.createChooser(intent, "Share using…")
//        startActivity(chooser)