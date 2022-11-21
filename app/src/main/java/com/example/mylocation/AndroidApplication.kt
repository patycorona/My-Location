package com.example.mylocation

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AndroidApplication :Application() {
    companion object{
        var checkedItem = 0
    }
}