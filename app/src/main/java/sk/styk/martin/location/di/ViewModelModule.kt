//package sk.styk.martin.location.di
//
//import android.arch.lifecycle.ViewModelProvider
//import dagger.Binds
//import android.arch.lifecycle.ViewModel
//import dagger.Module
//import dagger.multibindings.IntoMap
//import sk.styk.martin.location.ui.map.MapViewModel
//
//
//@Module
//internal abstract class ViewModelModule {
//    @Binds
//    @IntoMap
//    @ViewModelKey(MapViewModel::class)
//    internal abstract fun bindMapViewModel(homeViewModel: MapViewModel): ViewModel
//
//    @Binds
//    internal abstract fun bindViewModelFactory(factory: TvMazeViewModelFactory): ViewModelProvider.Factory
//}