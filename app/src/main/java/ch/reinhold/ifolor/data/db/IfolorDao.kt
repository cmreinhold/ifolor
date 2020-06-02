package ch.reinhold.ifolor.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ch.reinhold.ifolor.data.db.entities.UserEntity


/**
 * Dao implementation that allows us to query and operate on the user table of the Ifolor database.
 */
@Dao
interface IfolorDao {
    @Query("SELECT * FROM user_table WHERE email IS :email LIMIT 1")
    fun getUser(email: String): UserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(entity: UserEntity)
}



