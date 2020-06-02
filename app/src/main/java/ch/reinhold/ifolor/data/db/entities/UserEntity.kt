package ch.reinhold.ifolor.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
class UserEntity(
    @PrimaryKey
    val email: String,
    val name: String,
    val birthday: Long
)
