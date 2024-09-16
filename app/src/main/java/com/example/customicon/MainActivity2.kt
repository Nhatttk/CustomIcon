package com.example.customicon

import android.content.Context
import android.content.Intent
import android.content.Intent.ShortcutIconResource
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.content.res.Resources
import android.graphics.drawable.Icon
import android.os.Bundle
import android.util.Log
import android.os.Build
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity2 : AppCompatActivity() {
    private val testPackageName = "com.example.customicon"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main2)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Lấy danh sách các gói ứng dụng đã cài
//        val installedApps = getInstalledApps()
//
//        // Hiển thị tên gói của các ứng dụng đã cài qua Log
//        installedApps.forEach { packageName ->
//            Log.d("InstalledApp", "Package Name: $packageName")
//        }
//        val testPackageName :  String = "com.android.chrome"
        findViewById<View>(R.id.button2).setOnClickListener {
            Log.d("click", "click r")
            addShortcut(
                testPackageName
            )
        }
    }

    private fun getInstalledApps(): List<String> {
        val packageManager: PackageManager = packageManager
        // Lấy danh sách tất cả các ứng dụng đã cài (bao gồm hệ thống và người dùng)
        val packages: List<PackageInfo> = packageManager.getInstalledPackages(0)

        // Tạo một danh sách chứa tên gói (package name) của các ứng dụng
        return packages.map { it.packageName }
    }

    private fun addShortcut(packageName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val shortcutManager = getSystemService(Context.SHORTCUT_SERVICE) as ShortcutManager
            val packageManager = packageManager
            try {
                val applicationInfo: ApplicationInfo = packageManager.getApplicationInfo(packageName, 0)
                val launchIntent: Intent? = packageManager.getLaunchIntentForPackage(packageName)

                if (launchIntent != null) {
                    val shortcut = ShortcutInfo.Builder(this, "shortcut_${packageName}")
                        .setShortLabel("abc")
                        .setLongLabel("abc")
                        .setIcon(Icon.createWithResource(this,R.drawable.img))
                        .setIntent(launchIntent)
                        .build()

                    shortcutManager.requestPinShortcut(shortcut, null)
                } else {
                    Log.e("Shortcut", "Launch Intent is null for package: $packageName")
                }
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
                Log.e("Shortcut", "Package not found: $packageName")
            }
        } else {
            Log.e("Shortcut", "Android version not supported for shortcuts")
        }
    }

    private fun getShortcutNameFromPackage(packageName: String): CharSequence? {
        val packageManager = packageManager
        return try {
            val applicationInfo: ApplicationInfo = packageManager.getApplicationInfo(packageName, 0)
            val launchIntent: Intent? = packageManager.getLaunchIntentForPackage(packageName)
            if (launchIntent != null) {
                val labelRes: Int = packageManager.getActivityInfo(launchIntent.component!!, 0).labelRes
                if (labelRes != 0) packageManager.getResourcesForApplication(applicationInfo).getString(labelRes)
                else packageManager.getApplicationLabel(applicationInfo)
            } else {
                packageManager.getApplicationLabel(applicationInfo)
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            null
        }
    }
}