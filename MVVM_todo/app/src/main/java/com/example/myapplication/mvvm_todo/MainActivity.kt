package com.example.myapplication.mvvm_todo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.mvvm_todo.database.TodoModel
import com.example.myapplication.mvvm_todo.model.TodoViewModel
import com.example.myapplication.mvvm_todo.view.TodoListAdapter
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    val TAG: String = MainActivity::class.java.name

    private lateinit var todoViewModel: TodoViewModel
    private lateinit var todoListAdapter: TodoListAdapter
    private val todoItems: ArrayList<TodoModel> = ArrayList()

    private val recyclerview_list: RecyclerView by lazy {
        findViewById(R.id.recyclerview_list)
    }

    private val btn_add: Button by lazy {
        findViewById(R.id.btn_add)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "==onCreate")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViewModel()
        initRecyclerView()
        initBtnAdd()
    }


    /*
    * ViewModel 설정
    * View에서 ViewModel을 관찰하여 데이터가 변경될 때 내부적으로 자동으로 알 수 있도록 한다.
    * ViewModel은 View와 ViewModel의 분리로 액티비티가 destroy되었다가 다시 create 되어도 종료되지 않고 가지고 있다.
    * */
    private fun initViewModel() {
        todoViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(application)
                .create(TodoViewModel::class.java)
        todoViewModel.getTodoListAll().observe(this, Observer {
            todoListAdapter.setTodoItems(it)
        })
    }

    /*
    * RecyclerView 설정
    * Recyclerview adapter와 LinearLayoutManager를 만들고 연결
    * */
    private fun initRecyclerView() {
        todoListAdapter =
                TodoListAdapter({ todo -> deleteDialog(todo) })
        recyclerview_list.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = todoListAdapter
        }
    }

    /*
    * btn_add 설정
    * */
    private fun initBtnAdd() {
        btn_add.setOnClickListener {
            showAddTodoDialog()
        }
    }

    /*
    * Todo 리스트를 추가하는 Dialog 띄우기
    *  TodoModel을 생성하여 리스트 add해서 리스트를 갱신
    * */
    private fun showAddTodoDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add, null)
        val et_add_title: EditText by lazy {
            dialogView.findViewById(R.id.et_add_title)
        }
        val et_add_content: EditText by lazy {
            dialogView.findViewById(R.id.et_add_content)
        }

        val builder = AlertDialog.Builder(this)
        val dialog = builder.setTitle("todo 아이템 추가하기").setView(dialogView)
                .setPositiveButton("확인") { dialogInterface, i ->
                    var id: Long? = null
                    val title = et_add_title.text.toString()
                    val content = et_add_content.text.toString()
                    val createdDate = Date().time
                    val todoModel = TodoModel(
                            id,
                            todoListAdapter.getItemCount() + 1,
                            title,
                            content,
                            createdDate
                    )
                    todoViewModel.insert(todoModel)
                }
                .setNegativeButton("취소", null)
                .create()
        dialog.show()
    }

    /*
    * Todo 리스트를 삭제하는 Dialog 띄우기
    * */
    private fun deleteDialog(todoModel: TodoModel) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(todoModel.seq.toString() + " 번 Todo 아이템을 삭제할까요?")
                .setNegativeButton("취소") { _, _ -> }
                .setPositiveButton("확인") { _, _ ->
                    todoViewModel.delete(todoModel)
                }
                .create()
        builder.show()
    }
}