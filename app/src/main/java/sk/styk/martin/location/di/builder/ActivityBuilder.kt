package sk.styk.martin.location.di.builder

import dagger.Module
import dagger.android.ContributesAndroidInjector
import sk.styk.martin.location.di.AppModule
import sk.styk.martin.location.di.DbModule
import sk.styk.martin.location.di.scope.ActivityScope
import sk.styk.martin.location.di.scope.FragmentScope
import sk.styk.martin.location.ui.locationdetailsheet.LocationDetailBottomSheet
import sk.styk.martin.location.ui.main.MainActivity
import sk.styk.martin.location.ui.map.MapFragment

@Module
abstract class ActivityBuilder {

    @ActivityScope
    @ContributesAndroidInjector(modules = [AppModule::class, DbModule::class])
    abstract fun bindMainActivity(): MainActivity

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun bindMapFragment(): MapFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun bindLocationDetailBottomSheet(): LocationDetailBottomSheet

}