package com.example.customicon

import AppInfo
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.customicon.adapter.IconAdapter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStreamReader
class MainActivity2 : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main2)

        // Lấy tham chiếu đến LinearLayout mà chúng ta đã tạo trong XML
        val buttonContainer = findViewById<LinearLayout>(R.id.button_container)
        val iconListView: ListView = findViewById(R.id.icon_list_view)

        // Đọc file JSON từ thư mục raw
        val inputStream = resources.openRawResource(R.raw.listapp)
        val reader = InputStreamReader(inputStream)

        // Sử dụng Gson để parse file JSON
        val gson = Gson()
        val type = object : TypeToken<List<AppInfo>>() {}.type
        val appInfoList: List<AppInfo> = gson.fromJson(reader, type)

        // Kiểm tra dữ liệu đã được đọc
        for (app in appInfoList) {
            val adapter = IconAdapter(this, app.listIcon)
            val button = Button(this)
            button.text = app.appName
            button.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )

            // Thêm sự kiện onClick cho mỗi nút
            button.setOnClickListener {
                iconListView.visibility = View.VISIBLE
                iconListView.adapter = adapter
//                addShortcut(
//                    app.packageName, app.uriName, app.appName
//                )
            }

            // Thêm nút vào container
            buttonContainer.addView(button)
            println("App Name: ${app.appName}, Package Name: ${app.packageName}, URI: ${app.uriName}")
        }

    }


    private fun addShortcut(packageName: String, uriName: String, appName: String) {
        val shortcutManager = getSystemService(Context.SHORTCUT_SERVICE) as ShortcutManager
//            val packageManager = packageManager
        try {
            val appIntent = packageManager.getLaunchIntentForPackage(packageName)?.apply {
//                component = ComponentName(packageName, "org.telegram.ui.LaunchActivity") // Tên Activity của Telegram
                action = Intent.ACTION_MAIN
                addCategory(Intent.CATEGORY_LAUNCHER)
            }
            if (appIntent != null) {
                val shortcut = ShortcutInfo.Builder(this, "shortcut_${packageName}")
                    .setShortLabel(appName)
                    .setLongLabel(appName)
                    .setIcon(Icon.createWithResource(this, R.drawable.img))
                    .setIntent(appIntent)
                    .build()


                shortcutManager.requestPinShortcut(shortcut, null)
            } else {
                // Nếu Telegram chưa được cài đặt, điều hướng người dùng tới Google Play Store
                val playStoreIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=org.telegram.messenger"))
                startActivity(playStoreIntent)
            }


        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            Log.e("Shortcut", "Package not found: $packageName")
        }
    }
}