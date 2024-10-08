package com.study.chat.common.data.source.local.message.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.study.chat.common.data.source.local.message.entity.ReactionEntity
import com.study.chat.common.data.source.local.message.entity.ReactionEntity.Companion.REACTIONS_TABLE

@Dao
interface ReactionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(reactions: List<ReactionEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(reaction: ReactionEntity)

    @Upsert
    suspend fun upsert(reactions: List<ReactionEntity>)

    @Delete
    suspend fun delete(reaction: ReactionEntity)

    @Query("DELETE FROM $REACTIONS_TABLE")
    suspend fun deleteAll()

    @Transaction
    suspend fun clearAndInsert(reactions: List<ReactionEntity>) {
        deleteAll()
        insert(reactions)
    }

}

