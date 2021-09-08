package com.example.myapplication.todo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.MypageActivity
import com.example.myapplication.MasterApplication
import com.example.myapplication.R
import com.example.myapplication.TimerActivity
import kotlinx.android.synthetic.main.activity_todo_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate

var cnt: Int = 0
//달성률을 위한 카운트

class Todo_main : AppCompatActivity() {
    val planList: planlist = planlist()
    var mAdapter = planAdapter(planList)
    var studyId: Long = 400

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_main)

        val date: LocalDate = LocalDate.now()
        val todaydate: String = date.toString()
        today_date.setText(todaydate)

        val manager = LinearLayoutManager(this)
        manager.reverseLayout = false
        manager.stackFromEnd = false
        list_todo.layoutManager = manager

        list_todo.adapter = mAdapter

        //home 화면으로 이동
        home_button.setOnClickListener {
            startActivity(Intent(this, TimerActivity::class.java))
            finish()
        }

        //친구 화면으로 이동
        friend_button.setOnClickListener {
            startActivity(Intent(this, MypageActivity::class.java))
            finish()
        }

        // todo 리스트 추가
        add_button.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            val dialogView = layoutInflater.inflate(R.layout.add_dialong_view, null)
            val dialogtext = dialogView.findViewById<EditText>(R.id.edit_content)

            builder.setView(dialogView)
                .setPositiveButton("add") { dialogInterface, i ->
                    val text = dialogtext.text.toString()
                    planList.addPlan(plan(text))

                    val params = plan(text)
                }
                .setNegativeButton("cancel") { dialogInterface, i ->
                }
                .show()
        }

        // todo 리스트 제거
        mAdapter.setTodoClickListener(object : planAdapter.TodoClickListener {
            override fun onClick(view: View, position: Int) {
                studyId = planList.Planlist[position].studyId

            }
        })
    }
}