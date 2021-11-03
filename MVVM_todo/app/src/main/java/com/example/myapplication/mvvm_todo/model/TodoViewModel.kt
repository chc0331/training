package com.example.myapplication.mvvm_todo.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.myapplication.mvvm_todo.database.TodoModel
import com.example.myapplication.mvvm_todo.repository.TodoRepository

class TodoViewModel(application: Application) : AndroidViewModel(application) {

    private val todoRepository = TodoRepository(application)
    private var todoItems =
        todoRepository.getTodoListAll() //액티비티에서 ViewModel의 todoItems 리스트를 observe하고 리스트를 갱신

    fun getTodoListAll(): LiveData<List<TodoModel>> {
        return todoItems
    }

    fun insert(todoModel: TodoModel) {
        todoRepository.insert(todoModel)
    }

    fun delete(todoModel: TodoModel) {
        todoRepository.delete(todoModel)
    }

}