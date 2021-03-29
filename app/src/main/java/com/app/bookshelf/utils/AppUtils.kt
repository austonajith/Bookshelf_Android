package com.app.bookshelf.utils

import androidx.appcompat.app.AppCompatDelegate
import com.app.bookshelf.R

object AppUtils {

    fun getRandomColors(): Int {
        val colorList = arrayListOf(
            R.color.smooth_blue,
            R.color.smooth_darkblue,
            R.color.smooth_green,
            R.color.smooth_orange,
            R.color.smooth_pink,
            R.color.smooth_rose,
            R.color.smooth_violet
        )
        return colorList.random()
    }

    fun getRandomPattern():Int{
        val patternList = arrayListOf(R.drawable.pattern1, R.drawable.pattern2, R.drawable.pattern3)
        return patternList.random()
    }

    fun disableDarkMode() {
       // AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

}