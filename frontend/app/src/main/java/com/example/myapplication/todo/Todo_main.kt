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
import com.example.myapplication.Friend_main
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
    val api_todo = APIS_todo.create()
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
        List_start()

        //home 화면으로 이동
        home_button.setOnClickListener {
            startActivity(Intent(this, TimerActivity::class.java))
            finish()
        }

        //친구 화면으로 이동
        friend_button.setOnClickListener {
            startActivity(Intent(this, Friend_main::class.java))
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
                    (application as MasterApplication).api_todo.post_todo(params)
                        .enqueue(object : Callback<Post_Todo> {
                            override fun onResponse(call: Call<Post_Todo>, response: Response<Post_Todo>
                            ) {
                                if (response.isSuccessful) {
                                    val planee = response.body()
                                    Log.d("loggg", "post" + planee?.content)
                                }
                            }

                            override fun onFailure(call: Call<Post_Todo>, t: Throwable) {
                                Log.d("loggg", t.message.toString())
                                Log.d("loggg", "post_fail")
                            }
                        })
                }
                .setNegativeButton("cancel") { dialogInterface, i ->
                }
                .show()
        }

        // 체크박스 상태 저장
        mAdapter.setItemClickListener(object : planAdapter.PlanClickListener {
            override fun onClick(view: View, position: Int) {
                var studyid = planList.Planlist[position].studyId
                var status = planList.Planlist[position].status.toString()
                Log.d("state--", "click: " + status)

                if (status == "true") {
                    (application as MasterApplication).api_todo.put_todo(studyid, "false")
                        .enqueue(object : Callback<Put_Todo> {
                            override fun onResponse(call: Call<Put_Todo>, response: Response<Put_Todo>) {
                                planList.Planlist.clear()
                                List_start()
                            }

                            override fun onFailure(call: Call<Put_Todo>, t: Throwable) {
                                Toast.makeText(this@Todo_main, "서버 오류", Toast.LENGTH_SHORT).show()
                            }
                        })
                } else {
                    (application as MasterApplication).api_todo.put_todo(studyid, "true")
                        .enqueue(object : Callback<Put_Todo> {
                            override fun onResponse(call: Call<Put_Todo>, response: Response<Put_Todo>) {
                                planList.Planlist.clear()
                                List_start()
                            }

                            override fun onFailure(call: Call<Put_Todo>, t: Throwable) {
                                Toast.makeText(this@Todo_main, "서버 오류", Toast.LENGTH_SHORT).show()
                            }
                        })
                }
            }
        })

        // todo 리스트 제거
        mAdapter.setTodoClickListener(object : planAdapter.TodoClickListener {
            override fun onClick(view: View, position: Int) {
                studyId = planList.Planlist[position].studyId

                (application as MasterApplication).api_todo.delete_todo(studyId)
                    .enqueue(object : Callback<Delete_Todo> {
                        override fun onResponse(call: Call<Delete_Todo>, response: Response<Delete_Todo>
                        ) {
                            if (response.isSuccessful) {
                                Toast.makeText(this@Todo_main, "삭제되었습니다", Toast.LENGTH_LONG).show()

                                planList.Planlist.clear()
                                List_start()
                            } else {
                                Toast.makeText(
                                    this@Todo_main,
                                    response.body()!!.message,
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }

                        override fun onFailure(call: Call<Delete_Todo>, t: Throwable) {
                            Toast.makeText(this@Todo_main, "서버 오류", Toast.LENGTH_SHORT).show()
                        }
                    })
            }
        })
    }

    // todo 리스트 조회
    fun List_start() {
        (application as MasterApplication).api_todo.get_todo()
            .enqueue(object : retrofit2.Callback<Check_Get_Todo> {
                override fun onResponse(
                    call: Call<Check_Get_Todo>,
                    response: Response<Check_Get_Todo>
                ) {
                    Log.d("loggg", response.toString())


                    if (response.isSuccessful) {
                        for (i in response.body()!!.checkRoomList22.indices) {
                            val content = response.body()!!.checkRoomList22[i].content
                            val status = response.body()!!.checkRoomList22[i].status
                            val studyId = response.body()!!.checkRoomList22[i].studyId
                            Log.d("loggg", content)

                            planList.addPlan(
                                plan(
                                    content,
                                    status,
                                    studyId
                                )
                            )
                        }
                    }
                    list_todo.adapter = mAdapter
                }

                override fun onFailure(call: Call<Check_Get_Todo>, t: Throwable) {
                    Log.d("loggg", t.message.toString())
                    Log.d("loggg", "get_fail")
                }
            })


    }
}
