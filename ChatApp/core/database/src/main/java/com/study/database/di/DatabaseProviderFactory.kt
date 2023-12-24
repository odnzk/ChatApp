package com.study.database.di

object DatabaseProviderFactory {
    fun create(dependencies: DatabaseDep): DatabaseProvider = DaggerDatabaseComponent.factory()
        .create(dependencies)
        .impl
}
