package com.study.database.di

import dagger.Component
import javax.inject.Singleton


@Component(
    dependencies = [DatabaseDep::class],
    modules = [DatabaseModule::class]
)
@Singleton
internal interface DatabaseComponent {
    @Component.Factory
    interface Factory {
        fun create(dep: DatabaseDep): DatabaseComponent
    }

    val impl: DatabaseImpl
}
