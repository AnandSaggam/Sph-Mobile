package com.dlminfosoft.sphmobile.di

import androidx.room.Room
import com.dlminfosoft.sphmobile.BuildConfig
import com.dlminfosoft.sphmobile.database.SphMobileDatabase
import com.dlminfosoft.sphmobile.repository.RepositoryImpl
import com.dlminfosoft.sphmobile.utility.Constants
import com.dlminfosoft.sphmobile.utility.LocalizationInfoProvider
import com.dlminfosoft.sphmobile.utility.NetManager
import com.dlminfosoft.sphmobile.viewmodel.MainViewModelImpl
import com.dlminfosoft.sphmobile.webservice.IApiServiceMethods
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Creating a list of dependent object
 */
val dataModule = module {
    single<Retrofit> {
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BuildConfig.BASE_URL)
            .build()
    }
    factory { get<Retrofit>().create(IApiServiceMethods::class.java) }
    single {
        Room.databaseBuilder(get(), SphMobileDatabase::class.java, Constants.DATABASE_NAME)
            .build()
    }
    single { NetManager(get()) }
    single { get<SphMobileDatabase>().getYearlyRecordDao() }
    single { LocalizationInfoProvider(get()) }
    single { RepositoryImpl(get(), get(), get(),get()) }
    viewModel { MainViewModelImpl(get()) }
}