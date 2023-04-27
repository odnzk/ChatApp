package com.study.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.study.database.entity.ReactionEntity

@Dao
interface ReactionDao {

    @Transaction
    suspend fun clearAndInsert(reactions: List<ReactionEntity>) {
        deleteAll()
        insert(reactions)
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(reactions: List<ReactionEntity>)

    @Upsert
    suspend fun upsert(reactions: List<ReactionEntity>)

    @Transaction
    @Query("DELETE FROM reactions")
    suspend fun deleteAll()
}

