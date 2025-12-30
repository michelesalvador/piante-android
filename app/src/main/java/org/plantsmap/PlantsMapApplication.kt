package org.plantsmap

import android.app.Application
import org.plantsmap.model.AppRepository
import org.plantsmap.model.UserDataStore

class PlantsMapApplication : Application() {
    lateinit var appRepository: AppRepository

    override fun onCreate() {
        super.onCreate()
        val userDataStore = UserDataStore(this)
        appRepository = AppRepository(userDataStore)
    }
}