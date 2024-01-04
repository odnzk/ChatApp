package com.study.users

import com.study.components.model.UiUserPresenceStatus
import com.study.network.model.response.user.AllUserPresenceDto
import com.study.network.model.response.user.DetailedUserDto
import com.study.network.model.response.user.PresenceDetails
import com.study.network.model.response.user.PresenceStatusDto
import com.study.network.model.response.user.UserPresenceDto
import com.study.users.data.mapper.toUser
import com.study.users.data.mapper.toUserPresenceList
import com.study.users.domain.model.User
import com.study.users.domain.model.UserPresence
import com.study.users.presentation.util.mapper.toUiUser
import com.study.users.presentation.util.mapper.toUiUsers
import junit.framework.TestCase.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
internal class UserMapperTest {

    private companion object {
        const val AVATAR_URL = "https://example.com/avatar.jpg"
        const val EMAIL = "user@example.com"
        const val FULL_NAME = "John Doe"
        const val USER_ID = 123
        const val IS_ADMIN = false
        const val IS_ACTIVE = true
        const val IS_BOT = false
    }

    @Test
    fun `test detailedUserDto to User mapping`() {
        val detailedUserDto = DetailedUserDto(
            avatarUrl = AVATAR_URL,
            email = EMAIL,
            fullName = FULL_NAME,
            userId = USER_ID,
            isAdmin = IS_ADMIN,
            isActive = IS_ACTIVE,
            isBot = IS_BOT,
            isOwner = false,
            isGuest = false,
            isBillingAdmin = false
        )
        val user = detailedUserDto.toUser()


        assertEquals(AVATAR_URL, user.avatarUrl)
        assertEquals(EMAIL, user.email)
        assertEquals(FULL_NAME, user.name)
        assertEquals(USER_ID, user.id)
        assertEquals(IS_ACTIVE, user.isActive)
        assertEquals(IS_BOT, user.isBot)
    }

    @Test
    fun `test User to UiUser mapping`() {
        val user = User(
            id = USER_ID,
            name = FULL_NAME,
            email = EMAIL,
            avatarUrl = AVATAR_URL,
            isActive = IS_ACTIVE,
            isBot = IS_BOT
        )
        val presence = UiUserPresenceStatus.OFFLINE

        val uiUser = user.toUiUser(presence)

        assertEquals(USER_ID, uiUser.id)
        assertEquals(FULL_NAME, uiUser.name)
        assertEquals(EMAIL, uiUser.email)
        assertEquals(AVATAR_URL, uiUser.avatarUrl)
        assertEquals(IS_BOT, uiUser.isBot)
    }

    @Test
    fun `test toUserPresenceList mapping with presences not null`() {
        val allUserPresenceDto = AllUserPresenceDto(
            presences = mapOf(
                "user1@example.com" to UserPresenceDto(
                    website = PresenceDetails(PresenceStatusDto.ACTIVE, 1),
                    zulipMobile = PresenceDetails(PresenceStatusDto.IDLE, 0)
                ),
                "user2@example.com" to UserPresenceDto(
                    website = PresenceDetails(PresenceStatusDto.IDLE, 1),
                    zulipMobile = PresenceDetails(PresenceStatusDto.ACTIVE, 0)
                ),
                "user3@example.com" to UserPresenceDto(
                    website = null,
                    zulipMobile = PresenceDetails(PresenceStatusDto.ACTIVE, 0)
                ),
                "user4@example.com" to UserPresenceDto(
                    website = null,
                    zulipMobile = null
                )
            )
        )

        val userPresenceList = allUserPresenceDto.toUserPresenceList()

        assertEquals(4, userPresenceList.size)
        val firstUserPresence = userPresenceList[0]
        assertEquals("user1@example.com", firstUserPresence.userEmail)
        assertEquals(true, firstUserPresence.isActive)
        val secondUserPresence = userPresenceList[1]
        assertEquals("user2@example.com", secondUserPresence.userEmail)
        assertEquals(false, secondUserPresence.isActive)
        val thirdUserPresence = userPresenceList[2]
        assertEquals("user3@example.com", thirdUserPresence.userEmail)
        assertEquals(true, thirdUserPresence.isActive)
        val forthUserPresence = userPresenceList[3]
        assertEquals("user4@example.com", forthUserPresence.userEmail)
        assertEquals(false, forthUserPresence.isActive)
    }


    @Test
    fun `test toUserPresenceList mapping with presences null`() {
        val allUserPresenceDto = AllUserPresenceDto(presences = null)

        val userPresenceList = allUserPresenceDto.toUserPresenceList()

        assertEquals(0, userPresenceList.size)
    }

    @Test
    fun `test toUiUsers mapping`() {
        val users = listOf(
            User(
                1,
                name = "John Doe",
                email = "john@example.com",
                avatarUrl = "https://example.com/avatar1.jpg",
                isActive = true,
                isBot = false
            ),
            User(
                2,
                name = "Jane Smith",
                email = "jane@example.com",
                avatarUrl = "https://example.com/avatar2.jpg",
                isActive = false,
                isBot = false
            ),
            User(
                3, name = "Bot1",
                email = "bot1@example.com",
                avatarUrl = "https://example.com/avatar3.jpg",
                isActive = true,
                isBot = true
            )
        )
        val presence = listOf(
            UserPresence("john@example.com", true),
            UserPresence("jane@example.com", false)
        )


        val uiUsers = users.toUiUsers(presence)

        assertEquals(3, uiUsers.size)
        assertEquals(UiUserPresenceStatus.ACTIVE, uiUsers[0].presence)
        assertEquals(UiUserPresenceStatus.OFFLINE, uiUsers[1].presence)
        assertEquals(UiUserPresenceStatus.BOT, uiUsers[2].presence)
    }

}