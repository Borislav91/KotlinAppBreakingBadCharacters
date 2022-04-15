package com.breakingbad.borislav.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.breakingbad.borislav.data.CharacterEntity

@Database(entities = [CharacterEntity::class], version = 1)
abstract class CharactersDatabase : RoomDatabase() {
    abstract fun characterDao(): CharacterDao

    companion object {
        private var DATABASE: CharactersDatabase? = null
        fun getDatabase(context: Context): CharactersDatabase {
            if (DATABASE == null) {
                DATABASE = Room.databaseBuilder(
                    context.applicationContext,
                    CharactersDatabase::class.java, "BreakingBadDatabase"
                ).allowMainThreadQueries().build()
            }
            return DATABASE!!

        }
    }
}