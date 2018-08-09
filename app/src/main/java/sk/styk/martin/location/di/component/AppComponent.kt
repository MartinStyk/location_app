package sk.styk.martin.location.di.component

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import sk.styk.martin.location.LocationApp
import sk.styk.martin.location.di.AppModule
import sk.styk.martin.location.di.DbModule
import sk.styk.martin.location.di.builder.ActivityBuilder
import sk.styk.martin.location.di.builder.ServiceBuilder
import javax.inject.Singleton


@Singleton
@Component(modules = [AndroidSupportInjectionModule::class,
    AppModule::class,
    ActivityBuilder::class,
    ServiceBuilder::class,
    DbModule::class])
interface AppComponent : AndroidInjector<LocationApp> {

    fun inject(application: Application)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun app(application: Application): Builder

        fun build(): AppComponent
    }
}
