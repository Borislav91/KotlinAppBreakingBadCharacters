package com.breakingbad.borislav.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "Characters")
@Parcelize
data class CharacterEntity(
    @PrimaryKey
    val char_id: Int,
    val img: String,
    val name: String,
    val nickname: String,
    val occupation: String,
    val status: String,
    val appearance: String
): Parcelable
