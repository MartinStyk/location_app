package sk.styk.martin.location.di.builder

import dagger.Module
import dagger.android.ContributesAndroidInjector
import sk.styk.martin.location.di.scope.ServiceScope
import sk.styk.martin.location.service.LocationServiceModule
import sk.styk.martin.location.service.LocationUpdatesService

@Module
abstract class ServiceBuilder {

    @ServiceScope
    @ContributesAndroidInjector(modules = [LocationServiceModule::class])
    abstract fun bindLocationService(): LocationUpdatesService

}