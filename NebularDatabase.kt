package com.novarixis.nebular.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.novarixis.nebular.data.db.dao.ConversationDao
import com.novarixis.nebular.data.db.dao.SettingsDao
import com.novarixis.nebular.data.db.entity.ConversationEntity
import com.novarixis.nebular.data.db.entity.MemoryEntity
import com.novarixis.nebular.data.db.entity.MessageEntity
import com.novarixis.nebular.data.db.entity.SavedItemEntity
import com.novarixis.nebular.data.db.entity.SettingsEntity
import com.novarixis.nebular.data.db.entity.UserEntity

@Database(
    entities = [
        ConversationEntity::class,
        MessageEntity::class,
        UserEntity::class,
        SavedItemEntity::class,
        MemoryEntity::class,
        SettingsEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class NebularDatabase : RoomDatabase() {
    abstract fun conversationDao(): ConversationDao
    abstract fun settingsDao(): SettingsDao

    companion object {
        private const val DATABASE_NAME = "nebular_db"

        @Volatile
        private var instance: NebularDatabase? = null

        fun getInstance(context: Context): NebularDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(context.applicationContext, NebularDatabase::class.java, DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { instance = it }
            }
        }
    }
}
