package br.com.manieri.ipe_branco.dataBase.dao

import androidx.room.*
import br.com.manieri.ipe_branco.model.User

@Dao
interface UserDao {

    @Query("SELECT uid FROM User")
    fun getToken(): String

    @Query("SELECT name FROM User")
    fun getName() : String

    @Query("SELECT * FROM User")
    fun getAll(): List<User>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addUser(user: User)

    @Query("DELETE FROM User")
    fun clearData()
}