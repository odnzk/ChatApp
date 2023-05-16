package com.study.network.di

import coil.ImageLoader
import com.study.network.ZulipApi

interface NetworkImpl {
    val zulipApi: ZulipApi
    val imageLoader: ImageLoader
}
