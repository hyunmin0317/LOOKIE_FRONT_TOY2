package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import kotlinx.android.synthetic.main.activity_timer.*
import kotlinx.android.synthetic.main.activity_todo_main.*
import kotlinx.android.synthetic.main.activity_todo_main.home
import kotlinx.android.synthetic.main.activity_todo_main.mypage
import kotlinx.android.synthetic.main.activity_todo_main.todo
import java.time.LocalDate

var cnt: Int = 0
//달성률을 위한 카운트

class Todo_main : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_main)

        val date: LocalDate = LocalDate.now()
        val todaydate: String = date.toString()
        today_date.setText(todaydate)

        val planList: planlist = planlist()

        add_button.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            val dialogView = layoutInflater.inflate(R.layout.add_dialong_view, null)
            val dialogtext = dialogView.findViewById<EditText>(R.id.edit_content)

            builder.setView(dialogView)
                .setPositiveButton("add") { dialogInterface, i ->
                    planList.addPlan(
                        plan(
                            dialogtext.text.toString()
                        )
                    )
                }
                .setNegativeButton("cancel") { dialogInterface, i ->
                }
                .show()
        }

        list_todo.adapter =
            planAdapter(planList, LayoutInflater.from(this), activity = this)
        val manager = LinearLayoutManager(this)
        manager.reverseLayout = false
        manager.stackFromEnd = false
        list_todo.layoutManager = manager

        home.setOnClickListener { startActivity(Intent(this@Todo_main, TimerActivity::class.java)) }
        mypage.setOnClickListener { startActivity(Intent(this@Todo_main, MypageActivity::class.java)) }
    }

}


class planAdapter(
    val plan_list: planlist,
    val inflater: LayoutInflater,
    val activity: Activity,
) : RecyclerView.Adapter<planAdapter.ViewHolder>() {

    inner class ViewHolder(todo_view: View) : RecyclerView.ViewHolder(todo_view) {
        val planContent: TextView
        val todoIsDone: CheckBox

        init {
            planContent = todo_view.findViewById(R.id.plan_content)
            todoIsDone = todo_view.findViewById(R.id.check_box)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.todo_view, parent, false)
        return ViewHolder(view)
    }

    public override fun getItemCount(): Int {
        val sum: Int = plan_list.planlist.size
        Log.d("sum", "" + sum)

        return plan_list.planlist.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val todo = plan_list.planlist[position]

        holder.planContent.text = todo.content

        holder.todoIsDone.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                cnt++
                Log.d("state", "" + cnt)

            } else {
                cnt--
                Log.d("state--", "" + cnt)
            }
        }
    }

}
