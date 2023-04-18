package com.study.components.extensions

import vivid.money.elmslie.android.base.ElmActivity
import vivid.money.elmslie.android.base.ElmFragment
import vivid.money.elmslie.android.storeholder.LifecycleAwareStoreHolder
import vivid.money.elmslie.core.store.Store
import vivid.money.elmslie.core.store.dsl_reducer.DslReducer
import vivid.money.elmslie.coroutines.Actor
import vivid.money.elmslie.coroutines.ElmStoreCompat

fun <Event : Any, Effect : Any, State : Any, Command : Any> ElmFragment<Event, Effect, State>.createStoreHolder(
    state: State,
    reducer: DslReducer<Event, State, Effect, Command>,
    actor: Actor<Command, out Event>

): LifecycleAwareStoreHolder<Event, Effect, State> {
    return LifecycleAwareStoreHolder(lifecycle) { ElmStoreCompat(state, reducer, actor) }
}

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
