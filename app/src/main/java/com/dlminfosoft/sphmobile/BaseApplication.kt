package com.dlminfosoft.sphmobile

import android.app.Application
import androidx.room.Room
import com.dlminfosoft.sphmobile.database.SphMobileDatabase
import com.dlminfosoft.sphmobile.repository.Repository
import com.dlminfosoft.sphmobile.utility.Constants
import com.dlminfosoft.sphmobile.viewmodel.MainViewModel
import com.dlminfosoft.sphmobile.webservice.IApiServiceMethods
import org.koin.android.ext.android.startKoin
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/*
* This is main application class, load first when app started
*/
class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin(
            this@BaseApplication, listOf(dataModule)
        )
    }

    /*
    * Creating a list of dependent object
    */
    private val dataModule = module {
        single<Retrofit> {
            Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Constants.BASE_URL)
                .build()
        }
        factory { get<Retrofit>().create(IApiServiceMethods::class.java) }
        single {
            Room.databaseBuilder(get(), SphMobileDatabase::class.java, Constants.DATABASE_NAME)
                .build()
        }
        single { get<SphMobileDatabase>().getYearlyRecordDao() }
        single { Repository(get(), get()) }
        viewModel { MainViewModel(get()) }
    }
}