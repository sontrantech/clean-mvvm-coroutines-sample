/**
 * Copyright (C) 2019 Son Tran Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sontran.sample

import android.app.Application
import com.sontran.sample.core.di.ApplicationComponent
import com.sontran.sample.core.di.ApplicationModule
import com.sontran.sample.core.di.DaggerApplicationComponent
import com.sontran.sample.core.utils.logger.CrashReportingTree
import com.sontran.sample.core.utils.logger.FakeCrashLibrary
import com.squareup.leakcanary.LeakCanary
import timber.log.Timber

import timber.log.Timber.DebugTree




class AndroidApplication : Application() {

    // Dagger
    val appComponent: ApplicationComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        DaggerApplicationComponent
                .builder()
                .applicationModule(ApplicationModule(this))
                .build()
    }

    override fun onCreate() {
        super.onCreate()
        this.injectMembers()
        this.initializeLeakDetection()
        this.initLogTree()
    }

    private fun initLogTree() {
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        } else {
            Timber.plant(CrashReportingTree())
        }
    }

    private fun injectMembers() = appComponent.inject(this)

    private fun initializeLeakDetection() {
        if (BuildConfig.DEBUG) LeakCanary.install(this)
    }

}
