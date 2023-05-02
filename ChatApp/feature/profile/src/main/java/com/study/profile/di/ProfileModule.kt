package com.study.profile.di

import com.study.common.di.FeatureScope
import com.study.profile.presentation.elm.ProfileActor
import com.study.profile.presentation.elm.ProfileEffect
import com.study.profile.presentation.elm.ProfileEvent
import com.study.profile.presentation.elm.ProfileReducer
import com.study.profile.presentation.elm.ProfileState
import dagger.Module
import dagger.Provides
import vivid.money.elmslie.core.store.Store
import vivid.money.elmslie.coroutines.ElmStoreCompat

@Module
internal class ProfileModule {

    @Provides
    @FeatureScope
    fun providesStore(
        reducer: ProfileReducer,
        actor: ProfileActor
    ): Store<ProfileEvent, ProfileEffect, ProfileState> {
        return ElmStoreCompat(ProfileState(), reducer, actor)
    }
}
