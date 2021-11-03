package com.example.myapplication.mvvm_todo.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [TodoModel::class], version = 1)
abstract class TodoDatabase : RoomDatabase() {

    abstract fun todoDao(): TodoDao

    //데이터베이스의 인스턴스를 싱글턴으로 사용하기 위해 companion object 안에 만들어준다.
    companion object {
        private var INSTANCE: TodoDatabase? = null

        fun getInstance(context: Context): TodoDatabase? {
            if (INSTANCE == null) {
                synchronized(TodoDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        TodoDatabase::class.java,
                        "tb_todo"
                    ).fallbackToDestructiveMigration() //데이터베이스가 갱신될 때 기존의 테이블을 버리고 새로 사용하도록 설정
                        .build()
                }
            }
            return INSTANCE
        }
    }
}