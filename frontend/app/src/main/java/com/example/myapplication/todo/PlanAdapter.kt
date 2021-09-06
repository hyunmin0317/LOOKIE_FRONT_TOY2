package com.example.myapplication.todo

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

class planAdapter(
    val plan_list: planlist
) : RecyclerView.Adapter<planAdapter.ViewHolder>() {

    private lateinit var planClickListner: PlanClickListener
    private lateinit var todoClickListener: TodoClickListener


    inner class ViewHolder(todo_view: View) : RecyclerView.ViewHolder(todo_view) {
        val planContent: TextView
        val todoIsDone: CheckBox
        val todoDelete: Button

        init {
            planContent = todo_view.findViewById(R.id.plan_content)
            todoIsDone = todo_view.findViewById(R.id.check_box)
            todoDelete = todo_view.findViewById(R.id.delete_button)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.todo_view, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return plan_list.Planlist.size
    }

    // 리사이클러뷰 아이템클릭 리스너
    interface PlanClickListener {
        fun onClick(view: View, position: Int)
    }

    fun setItemClickListener(planClickListener: PlanClickListener) {
        this.planClickListner = planClickListener
    }

    interface TodoClickListener{
        fun onClick(view: View, position: Int)
    }

    fun setTodoClickListener(todoClickListener: TodoClickListener) {
        this.todoClickListener = todoClickListener
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val todo = plan_list.Planlist[position]

        holder.planContent.text = todo.content

        holder.todoIsDone.isChecked = plan_list.Planlist[position].status.toString() == "true"


        holder.todoIsDone.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                cnt++
                Log.d("state", "" + cnt)

            } else {
                cnt--
                Log.d("state--", "" + cnt)
            }

        }

        holder.todoDelete.setOnClickListener() {
            todoClickListener.onClick(it, position)
        }

        // 리사이클러뷰 아이템클릭 리스너
        holder.todoIsDone.setOnClickListener() {
            planClickListner.onClick(it, position)
        }
    }
}