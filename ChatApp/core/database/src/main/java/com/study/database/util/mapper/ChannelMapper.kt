package com.study.database.util.mapper

import com.study.database.model.ChannelEntity
import com.study.database.model.update.ChannelUpdateEntity

internal fun List<ChannelEntity>.toChannelUpdateEntity(): List<ChannelUpdateEntity> =
    map { channel ->
        ChannelUpdateEntity(id = channel.id, title = channel.title)
    }
