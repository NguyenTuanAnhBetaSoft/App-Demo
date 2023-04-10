package com.betasoft.appdemo

import android.R
import android.app.Application
import android.net.Uri.Builder
import androidx.viewbinding.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump
import timber.log.Timber


@HiltAndroidApp
class App: Application() {
    override fun onCreate() {
        super.onCreate()
        if(BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        setupFonts()
    }

    private fun setupFonts() {
        ViewPump.init(
            ViewPump.builder()
                .addInterceptor(
                    CalligraphyInterceptor(
                        CalligraphyConfig.Builder()
                            .setDefaultFontPath("fonts/ProximaNovaRegular.otf")
//                            .setFontAttrId(R.attr.fontPath)
                            .build()
                    )
                ).build()
        )
    }
}