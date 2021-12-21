package com.example.myapplication.rxjavaroom.persistencee

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Flowable

@Dao
interface UserDao {

    /*
    * Get a user by id.
    *
    * @return the user from the table with a specific id.
    * */
    @Query("SELECT * FROM Users WHERE userid = :id")
    fun getUserById(id: String): Flowable<User>


    /*
    * Insert a user in the database. If the user already exists, replace it.
    * */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: User): Completable


    /*
    * Delete all users
    * */
    @Query("DELETE FROM Users")
    fun deleteAllUsers()

}