package kz.edu.kmf.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kz.edu.kmf.data.model.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addUser(event: User)

    @Query("SELECT * FROM user_table")
    fun readAllData(): LiveData<List<User>>

}