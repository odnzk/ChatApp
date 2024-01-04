package com.study.channels.di

import com.study.channels.data.source.local.dao.ChannelDao
import com.study.channels.data.source.local.dao.ChannelTopicDao

interface ChannelsDatabase {

    fun channelsDao(): ChannelDao

    fun topicsDao(): ChannelTopicDao
}