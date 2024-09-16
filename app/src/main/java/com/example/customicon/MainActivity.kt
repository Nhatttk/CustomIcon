package com.example.customicon

import android.content.Intent
import android.content.Intent.ShortcutIconResource
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val testPackageName :  String = "com.android.chrome"
        findViewById<View>(R.id.button).setOnClickListener {
            addShortcut(
                testPackageName
            )
        }
    }

    private fun addShortcut(packageName: String) {
        val packageManager = packageManager
        try {
            val applicationInfo = packageManager.getApplicationInfo(packageName, 0)
            val resourcesForApplication =
                packageManager.getResourcesForApplication(applicationInfo)
            val resourceName = resourcesForApplication.getResourceName(applicationInfo.icon)
            val launchIntentForPackage = packageManager.getLaunchIntentForPackage(packageName)
            val addIntent = Intent("com.android.launcher.action.INSTALL_SHORTCUT")
            addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, getShortcutNameFromPackage(packageName))
            addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, launchIntentForPackage)
            val shortcutIconResource = ShortcutIconResource()
            shortcutIconResource.packageName = packageName
            shortcutIconResource.resourceName = resourceName
            addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, shortcutIconResource)
            addIntent.putExtra("duplicate", false)
            sendBroadcast(addIntent)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
    }

    private fun getShortcutNameFromPackage(packageName: String): CharSequence? {
        val packageManager = packageManager
        try {
            val applicationInfo = packageManager.getApplicationInfo(packageName, 0)
            val resourcesForApplication =
                packageManager.getResourcesForApplication(applicationInfo)
            val launchIntentForPackage = packageManager.getLaunchIntentForPackage(packageName)
            val i =
                packageManager.getActivityInfo(launchIntentForPackage!!.component!!, 0).labelRes
            return if (i != 0) resourcesForApplication.getString(i) else packageManager.getApplicationLabel(
                applicationInfo
            )
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return null
    }
}