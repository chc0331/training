package com.example.myapplication.mvvm_todo.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TodoDao {
    @Query("SELECT * FROM tb_todo ORDER BY SEQ ASC")
    fun getTodoListAll(): LiveData<List<TodoModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(todo: TodoModel)

    @Delete
    fun delete(todo:TodoModel)
}