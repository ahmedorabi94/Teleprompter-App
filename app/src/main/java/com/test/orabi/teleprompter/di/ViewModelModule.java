package com.test.orabi.teleprompter.di;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.test.orabi.teleprompter.viewmodel.AddTextViewModel;
import com.test.orabi.teleprompter.viewmodel.DisplayVidoeViewModel;
import com.test.orabi.teleprompter.viewmodel.MainViewModel;
import com.test.orabi.teleprompter.viewmodel.TeleViewModelFactory;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel.class)
    abstract ViewModel bindMainViewModel(MainViewModel mainViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(AddTextViewModel.class)
    abstract ViewModel bindAddTextViewModel(AddTextViewModel addTextViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(DisplayVidoeViewModel.class)
    abstract ViewModel bindDisplayVidoeViewModel(DisplayVidoeViewModel displayVidoeViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(TeleViewModelFactory factory);
}
