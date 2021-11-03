package com.example.myapplication.mvvm_todo.view

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.mvvm_todo.R
import com.example.myapplication.mvvm_todo.database.TodoModel
import java.text.SimpleDateFormat
import java.util.*

class TodoListAdapter(val deleteItemClick: (TodoModel) -> Unit) :
    RecyclerView.Adapter<TodoListAdapter.TodoViewHolder>() {
    private var todoItems: List<TodoModel> = listOf()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TodoListAdapter.TodoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        val viewHolder = TodoViewHolder(view)
        return viewHolder
    }

    override fun onBindViewHolder(holder: TodoListAdapter.TodoViewHolder, position: Int) {
        val todoModel = todoItems[position]
        todoModel.seq = position + 1
        val todoViewHolder = holder as TodoViewHolder
        todoViewHolder.onBind(todoModel)
    }

    override fun getItemCount(): Int {
        Log.d("MainActivity", "todoItem getItemCount !!: " + todoItems.size)
        return todoItems.size
    }


    /* 데이터베이스가 변경될 때마다 호출 */
    fun setTodoItems(todoItems: List<TodoModel>) {
        this.todoItems = todoItems
        Log.d("MainActivity", "todoItem setTodoItems !!:" + todoItems.size)
        notifyDataSetChanged()
    }

    /*
    * 뷰홀더는 리스트를 스크롤하는 동안 뷰를 생성하고 다시 뷰의 구성요소를 찾는 행위를 반복하면서 생기는
    * 성능저하를 방지하기 위해 미리 저장 해 놓고 빠르게 접근하기 위하여 사용하는 객체
    */
    inner class TodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tv_seq = itemView.findViewById<TextView>(R.id.tv_seq)
        private val tv_title = itemView.findViewById<TextView>(R.id.tv_title)
        private val tv_content = itemView.findViewById<TextView>(R.id.tv_content)
        private val tv_date = itemView.findViewById<TextView>(R.id.tv_date)
        private val iv_delete = itemView.findViewById<ImageView>(R.id.iv_delete)

        fun onBind(todoModel: TodoModel) {

            tv_seq.text = todoModel.seq.toString()
            tv_title.text = todoModel.title
            tv_content.text = todoModel.title
            tv_date.text = todoModel.createDate.convertDateToString("yyyy.MM.dd HH:mm")

            iv_delete.setOnClickListener {
                deleteItemClick(todoModel)
            }
        }
    }

    fun Long.convertDateToString(format: String): String {
        val simpleDateFormat = SimpleDateFormat(format)
        return simpleDateFormat.format(Date(this))
    }


}