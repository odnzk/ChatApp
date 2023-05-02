package com.study.components.extension

import vivid.money.elmslie.android.base.ElmActivity
import vivid.money.elmslie.android.base.ElmFragment
import vivid.money.elmslie.android.storeholder.LifecycleAwareStoreHolder
import vivid.money.elmslie.core.store.Store

fun <Event : Any, Effect : Any, State : Any> ElmFragment<Event, Effect, State>.createStoreHolder(
    store: Store<Event, Effect, State>
): LifecycleAwareStoreHolder<Event, Effect, State> {
    return LifecycleAwareStoreHolder(lifecycle) { store }
}


fun <Event : Any, Effect : Any, State : Any> ElmActivity<Event, Effect, State>.createStoreHolder(
    store: Store<Event, Effect, State>
): LifecycleAwareStoreHolder<Event, Effect, State> {
    return LifecycleAwareStoreHolder(lifecycle) { store }
}
