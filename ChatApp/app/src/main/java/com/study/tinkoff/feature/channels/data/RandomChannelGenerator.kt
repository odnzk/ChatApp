package com.study.tinkoff.feature.channels.data

import com.study.domain.model.Channel
import com.study.domain.model.StreamPostPolicy
import kotlin.random.Random


class RandomChannelGenerator {

    fun generate(count: Int): List<Channel> {
        val resList: MutableList<Channel> = mutableListOf()
        for (i in 0 until count) {
            resList.add(
                Channel(
                    i,
                    randomChannelTitles.random(),
                    randomChannelDescriptions.random(),
                    Random.nextBoolean(),
                    StreamPostPolicy.ANY_USERS
                )
            )
        }
        return resList
    }

    companion object {
        private val randomChannelTitles =
            listOf("#general", "#testing", "#development", "#java", "#kotlin")
        private val randomChannelDescriptions =
            listOf(
                "#general description",
                "#testing description",
                "#development description",
                "#java description",
                "#kotlin description"
            )
    }
}
