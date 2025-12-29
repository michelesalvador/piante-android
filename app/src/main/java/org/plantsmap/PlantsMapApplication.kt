package org.plantsmap

import android.app.Application
import org.plantsmap.model.AppRepository

class PlantsMapApplication : Application() {
    lateinit var appRepository: AppRepository

    override fun onCreate() {
        super.onCreate()
        appRepository = AppRepository()
    }
}