package com.test.orabi.teleprompter.di;


import com.test.orabi.teleprompter.view.AddTextActivity;
import com.test.orabi.teleprompter.view.DisplayVideoActivity;
import com.test.orabi.teleprompter.view.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class MainActivityModule {
    @ContributesAndroidInjector(modules = FragmentBuildersModule.class)
    abstract MainActivity contributeMainActivity();

    @ContributesAndroidInjector(modules = FragmentBuildersModule.class)
    abstract AddTextActivity contributeAddTextActivity();

    @ContributesAndroidInjector(modules = FragmentBuildersModule.class)
    abstract DisplayVideoActivity contributeDisplayVideoActivity();
}
