package com.study.tinkoff.feature.users.data

import com.study.domain.model.User
import kotlin.random.Random

class RandomUsersGenerator {
    fun generateRandomUsers(userCount: Int): List<User> {
        val resList = mutableListOf<User>()
        for (i in 1..userCount) {
            resList.add(
                User(
                    id = i,
                    name = names.random(),
                    email = emails.random(),
                    avatarUrl = "",
                    is_active = Random.nextBoolean()
                )
            )
        }
        return resList
    }

    companion object {
        private val emails = listOf(
            "darrel@company.com",
            "kiara@company.com",
            "john@company.com",
            "cassie@company.com",
            "mike@company.com",
            "maddy@company.com"
        )
        private val names =
            listOf("Darrel", "Kiara", "Cassie", "Mike", "John", "Maddy", "Lexi", "Nate")
    }
}
