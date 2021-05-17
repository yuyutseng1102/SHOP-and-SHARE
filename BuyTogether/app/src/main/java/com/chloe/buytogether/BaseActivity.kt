package com.chloe.buytogether

import android.graphics.Color
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.DisplayCutout
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

open class BaseActivity : AppCompatActivity(), CoroutineScope {

    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupStatusBar()
    }

    /**
     * Get cutout height from [DisplayCutout]
     * @notice if device has cutout, the status bar height will be same as cutout height.
     */
    suspend fun getCutoutHeight(): Int {
        return withContext(Dispatchers.IO) {
            when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.P -> {

                    window?.let {
                        val displayCutout: DisplayCutout? = it.decorView.rootWindowInsets.displayCutout
                        Log.d("Chloe","displayCutout?.safeInsetTop=${displayCutout?.safeInsetTop}")
                        Log.d("Chloe","displayCutout?.safeInsetBottom=${displayCutout?.safeInsetBottom}")
                        Log.d("Chloe","displayCutout?.safeInsetLeft=${displayCutout?.safeInsetLeft}")
                        Log.d("Chloe","displayCutout?.safeInsetRight=${displayCutout?.safeInsetRight}")

                        val rects: List<Rect>? = displayCutout?.boundingRects
                        Log.d("Chloe","rects?.size=${rects?.size}")
                        Log.d("Chloe","rects=$rects")

                        displayCutout?.safeInsetTop ?: 0
                    } ?: 0
                }
                else -> 0
            }
        }
    }

    /**
     * Set up status bar to transparent
     * @notice this method have to be used before setContentView.
     */
    private fun setupStatusBar() {
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        val window = window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.TRANSPARENT // calculateStatusColor(Color.WHITE, (int) alphaValue)
    }
}