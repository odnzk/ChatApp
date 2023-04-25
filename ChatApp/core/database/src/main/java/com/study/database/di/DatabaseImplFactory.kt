package com.study.database.di

object DatabaseImplFactory {
    fun create(dependencies: DatabaseDep): DatabaseImpl = DaggerDatabaseComponent.factory()
        .create(dependencies)
        .impl
}
