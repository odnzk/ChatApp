package com.study.profile.di

import com.study.common.di.FeatureScope
import com.study.components.di.SingletoneStoreHolder
import com.study.profile.presentation.elm.ProfileActor
import com.study.profile.presentation.elm.ProfileEffect
import com.study.profile.presentation.elm.ProfileEvent
import com.study.profile.presentation.elm.ProfileReducer
import com.study.profile.presentation.elm.ProfileState
import dagger.Module
import dagger.Provides
import vivid.money.elmslie.android.storeholder.StoreHolder
import vivid.money.elmslie.coroutines.ElmStoreCompat

@Module
internal class ProfileModule {

    @Provides
    @FeatureScope
    fun providesStore(
        reducer: ProfileReducer,
        actor: ProfileActor
    ): StoreHolder<ProfileEvent, ProfileEffect, ProfileState> =
        SingletoneStoreHolder { ElmStoreCompat(ProfileState(), reducer, actor) }
}
