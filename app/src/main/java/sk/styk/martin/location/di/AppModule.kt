package sk.styk.martin.location.di

import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.support.v7.preference.PreferenceManager
import java.util.concurrent.Executor
import java.util.concurrent.Executors


@Module
class AppModule {

    @Provides
    @Singleton
    fun provideContext(application: Application): Context {
        return application
    }

    @Provides
    @Singleton
    fun providesPreferenceUtils(application: Application): SharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(application)

    @Provides
    @Singleton
    fun provideBackgroundExecutor(): Executor = Executors.newFixedThreadPool(3)

}
