package com.study.profile

import com.study.components.model.UiUserPresenceStatus
import com.study.network.model.response.user.DetailedUserDto
import com.study.network.model.response.user.PresenceDetails
import com.study.network.model.response.user.PresenceStatusDto
import com.study.network.model.response.user.UserPresenceDto
import com.study.network.model.response.user.UserPresenceResponse
import com.study.profile.data.mapper.toDetailedUser
import com.study.profile.data.mapper.toUserPresenceStatus
import com.study.profile.domain.model.UserDetailed
import com.study.profile.presentation.util.mapper.toUiUser
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class UserMapperTest {
    private companion object {
        const val AVATAR_URL = "https://example.com/avatar.jpg"
        const val EMAIL = "user@example.com"
        const val FULL_NAME = "John Doe"
        const val USER_ID = 123
        const val IS_ADMIN = false
        const val IS_ACTIVE = true
        const val IS_BOT = false
        const val IS_GUEST = true
        const val IS_BILLING_ADMIN = false
        const val IS_OWNER = true
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
            isOwner = IS_OWNER,
            isGuest = IS_GUEST,
            isBillingAdmin = IS_BILLING_ADMIN
        )
        val user = detailedUserDto.toDetailedUser()


        assertEquals(AVATAR_URL, user.avatarUrl)
        assertEquals(EMAIL, user.email)
        assertEquals(FULL_NAME, user.name)
        assertEquals(USER_ID, user.id)
        assertEquals(IS_ACTIVE, user.isActive)
        assertEquals(IS_BOT, user.isBot)
        assertEquals(IS_OWNER, user.isOwner)
        assertEquals(IS_GUEST, user.isGuest)
        assertEquals(IS_BILLING_ADMIN, user.isBillingAdmin)
    }

    @Test
    fun `test toUserPresenceStatus when website != null and zulipMobile != null`() {
        val presenceResponse = UserPresenceResponse(
            UserPresenceDto(
                website = PresenceDetails(PresenceStatusDto.IDLE, 1),
                zulipMobile = PresenceDetails(PresenceStatusDto.ACTIVE, 0),
            )
        )

        val userPresence = presenceResponse.toUserPresenceStatus()

        assertEquals(true, userPresence.isIdle)
    }

    @Test
    fun `test toUserPresenceStatus when website != null and zulipMobile = null`() {
        val presenceResponse = UserPresenceResponse(
            UserPresenceDto(
                website = PresenceDetails(PresenceStatusDto.IDLE, 1),
                zulipMobile = null
            )
        )

        val userPresence = presenceResponse.toUserPresenceStatus()

        assertEquals(true, userPresence.isIdle)
    }

    @Test
    fun `test toUserPresenceStatus when website = null and zulipMobile != null`() {
        val presenceResponse = UserPresenceResponse(
            UserPresenceDto(
                website = null,
                zulipMobile = PresenceDetails(PresenceStatusDto.ACTIVE, 0),
            )
        )

        val userPresence = presenceResponse.toUserPresenceStatus()

        assertEquals(false, userPresence.isIdle)
    }

    @Test
    fun `test toUserPresenceStatus when website = null and zulipMobile = null`() {
        val presenceResponse = UserPresenceResponse(
            UserPresenceDto(
                website = null,
                zulipMobile = null,
            )
        )

        val userPresence = presenceResponse.toUserPresenceStatus()

        assertEquals(true, userPresence.isIdle)
    }

    @Test
    fun `test toUiUser`() {
        val userDetailed = UserDetailed(
            id = 1,
            isActive = true,
            name = "John Doe",
            avatarUrl = "https://example.com/avatar.jpg",
            isBot = false,
            email = "john@example.com",
            isOwner = true,
            isAdmin = false,
            isBillingAdmin = true,
            isGuest = false
        )
        val presence = UiUserPresenceStatus.ACTIVE

        val uiUserDetailed = userDetailed.toUiUser(presence)

        assertEquals("John Doe", uiUserDetailed.username)
        assertEquals("https://example.com/avatar.jpg", uiUserDetailed.avatarUrl)
        assertEquals(presence, uiUserDetailed.presence)
        assertEquals(false, uiUserDetailed.isBot)
        assertEquals("john@example.com", uiUserDetailed.email)
        assertEquals(
            listOf(R.string.user_role_owner, R.string.user_role_billing_admin),
            uiUserDetailed.roles
        )
    }

}