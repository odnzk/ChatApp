package com.study.components.extensions

import androidx.lifecycle.Lifecycle
import vivid.money.elmslie.android.base.ElmActivity
import vivid.money.elmslie.android.base.ElmFragment
import vivid.money.elmslie.android.storeholder.LifecycleAwareStoreHolder
import vivid.money.elmslie.coroutines.ElmStoreCompat

fun <Event : Any, Effect : Any, State : Any, Command : Any> ElmFragment<Event, Effect, State>.createStoreHolder(
    store: ElmStoreCompat<Event, State, Effect, Command>
): LifecycleAwareStoreHolder<Event, Effect, State> {
    return LifecycleAwareStoreHolder(lifecycle) { store }
}

fun <Event : Any, Effect : Any, State : Any, Command : Any> createStoreHolder(
    lifecycle: Lifecycle,
    store: ElmStoreCompat<Event, State, Effect, Command>
): LifecycleAwareStoreHolder<Event, Effect, State> {
    return LifecycleAwareStoreHolder(lifecycle) { store }
}


fun <Event : Any, Effect : Any, State : Any, Command : Any> ElmActivity<Event, Effect, State>.createStoreHolder(
    store: ElmStoreCompat<Event, State, Effect, Command>
): LifecycleAwareStoreHolder<Event, Effect, State> {
    return LifecycleAwareStoreHolder(lifecycle) { store }
}
