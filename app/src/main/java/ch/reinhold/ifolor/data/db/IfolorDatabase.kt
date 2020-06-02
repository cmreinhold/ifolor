package ch.reinhold.ifolor.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ch.reinhold.ifolor.data.db.entities.UserEntity

@Database(
    entities = [UserEntity::class],
    version = 1,
    exportSchema = false
)
abstract class IfolorDatabase : RoomDatabase() {
    abstract fun dao(): IfolorDao
}
