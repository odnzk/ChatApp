package com.study.components.util

import com.study.common.extension.fastLazy
import vivid.money.elmslie.android.storeholder.StoreHolder
import vivid.money.elmslie.core.store.Store

/**
 * A holder class for avoiding memory leaks while using Dagger with Elmslie.
 * It is necessary to store this class in the Dagger scope and, when the scope is going to be destroyed,
 * clear the store:
 *
 * ```
 * DaggerStoreHolder.store.stop()
 * ```
 *
 * @param storeProvider A provider function for creating a new [Store] instance.
 * @param Event The type of event that the store processes.
 * @param Effect The type of side effect that the store can produce.
 * @param State The type of state that the store manages.
 */
class DaggerStoreHolder<Event : Any, Effect : Any, State : Any>(storeProvider: () -> Store<Event, Effect, State>) :
    StoreHolder<Event, Effect, State> {

    override var isStarted = false

    override val store by fastLazy { storeProvider().start().also { isStarted = true } }
}
