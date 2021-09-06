package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.friend.*
import com.example.myapplication.todo.Todo_main
import kotlinx.android.synthetic.main.friend_add.*
import kotlinx.android.synthetic.main.friend_list_item.*
import kotlinx.android.synthetic.main.friend_main.*

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Friend_main : AppCompatActivity() {

    val api = APIS.create()
    var userid : Long = 401
    val friendListview: friendListView = friendListView()
    var mAdapter= ViewAdapter(friendListview)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.friend_main)


        val friend_add_button: ImageView = findViewById<ImageView>(R.id.friend_add_button)

        //친구 추가 버튼 누르면 추가화면으로 이동
        friend_add_button.setOnClickListener {
            val intent = Intent(this, Friend_add::class.java)
            startActivity(intent)
        }

        //todo list 화면으로 이동
        todolist_button.setOnClickListener{
            startActivity(Intent(this, Todo_main::class.java))
            finish()
        }
        //타이머 화면으로 이동
        home_button.setOnClickListener{
            startActivity(Intent(this, TimerActivity::class.java))
            finish()
        }

        // 리싸이클러뷰 어댑터 설정
        mAdapter= ViewAdapter(friendListview)
        val manager = LinearLayoutManager(this)
        manager.reverseLayout = false
        manager.stackFromEnd = false
        friend_list_recyclerView.layoutManager = manager

        friend_list_recyclerView.adapter = mAdapter


        // 휴지통 버튼 클릭시
        mAdapter.setItemClickListener(object : ViewAdapter.ItemClickListener {

            override fun onClick(view:View, position: Int) {

                userid = friendListview.FriendListview[position].userId

                // 선택한 친구 목록 삭제
                (application as MasterApplication).api.delete_users(userid).enqueue(object : Callback<DeleteModel> {
                    override fun onResponse(call: Call<DeleteModel>, response: Response<DeleteModel>) {
                        Log.d("log", response.toString())

                        if(response.body()!!.code == 200){
                            Toast.makeText(this@Friend_main, "삭제되었습니다", Toast.LENGTH_LONG).show()

                            // 리사이클러뷰 초기화 할 필요 있음
                            friendListview.FriendListview.clear()
                            List_Start()
                        }
                        else if(response.body()!!.code == 400){
                            Toast.makeText(this@Friend_main, response.body()!!.message, Toast.LENGTH_LONG).show()
                        }


                    }
                    override fun onFailure(call: Call<DeleteModel>, t: Throwable) {
                        // 실패
                        Log.d("log",t.message.toString())
                        Log.d("log","fail")
                    }
                })

            }
        })


        // 리사이클러뷰 설정
        friend_list_recyclerView.adapter = mAdapter

        List_Start()
    }

    override fun onRestart() {
        super.onRestart()
        setContentView(R.layout.friend_main)

        friendListview.FriendListview.clear()

        val friend_add_button: ImageView = findViewById<ImageView>(R.id.friend_add_button)

        //친구 추가 버튼 누르면 추가화면으로 이동
        friend_add_button.setOnClickListener {
            val intent = Intent(this, Friend_add::class.java)
            startActivity(intent)
        }

        //todo list 화면으로 이동
        todolist_button.setOnClickListener{
            startActivity(Intent(this, Todo_main::class.java))
            finish()
        }
        //타이머 화면으로 이동
        home_button.setOnClickListener{
            startActivity(Intent(this, TimerActivity::class.java))
            finish()
        }

        // 리싸이클러뷰 어댑터 설정
        mAdapter= ViewAdapter(friendListview)
        val manager = LinearLayoutManager(this)
        manager.reverseLayout = false
        manager.stackFromEnd = false
        friend_list_recyclerView.layoutManager = manager

        friend_list_recyclerView.adapter = mAdapter


        // 휴지통 버튼 클릭시
        mAdapter.setItemClickListener(object : ViewAdapter.ItemClickListener {

            override fun onClick(view:View, position: Int) {

                userid = friendListview.FriendListview[position].userId

                // 선택한 친구 목록 삭제
                (application as MasterApplication).api.delete_users(userid).enqueue(object : Callback<DeleteModel> {
                    override fun onResponse(call: Call<DeleteModel>, response: Response<DeleteModel>) {
                        Log.d("log", response.toString())

                        if(response.body()!!.code == 200){
                            Toast.makeText(this@Friend_main, "삭제되었습니다", Toast.LENGTH_LONG).show()

                            // 리사이클러뷰 초기화 할 필요 있음
                            friendListview.FriendListview.clear()
                            List_Start()
                        }
                        else if(response.body()!!.code == 400){
                            Toast.makeText(this@Friend_main, response.body()!!.message, Toast.LENGTH_LONG).show()
                        }


                    }
                    override fun onFailure(call: Call<DeleteModel>, t: Throwable) {
                        // 실패
                        Log.d("log",t.message.toString())
                        Log.d("log","fail")
                    }
                })

            }
        })


        // 리사이클러뷰 설정
        friend_list_recyclerView.adapter = mAdapter

        List_Start()
    }

    fun List_Start() {
        // 친구 목록 데이터 받기
        (application as MasterApplication).api.get2_users().enqueue(object : Callback<CheckGetModel2> {
            override fun onResponse(call: Call<CheckGetModel2>, response: Response<CheckGetModel2>) {
                Log.d("log", response.toString())

                // 받은 유저정보 -닉네임, 달성률, 누적시간- 리사이클러뷰로 보이기
                if(response.isSuccessful){
                    for(i in response.body()!!.checkRoomList2.indices){
                        userid = response.body()!!.checkRoomList2[i].userId
                        val name= response.body()!!.checkRoomList2[i].username
                        val achievementRate= response.body()!!.checkRoomList2[i].achievementRate
                        val accumulatedTime= response.body()!!.checkRoomList2[i].accumulatedTime
                        val studyStatus= response.body()!!.checkRoomList2[i].studyStatus

                        Log.d("log", name)
                        Log.d("log", achievementRate.toString())
                        Log.d("log", accumulatedTime)


                        // 리스트에 추가
                        friendListview.viewFriend(
                            friendview(
                                userid,
                                name,
                                achievementRate,
                                accumulatedTime,
                                studyStatus
                            )
                        )


                    }
                }

                // 리사이클러뷰 설정
                friend_list_recyclerView.adapter = mAdapter
            }
            override fun onFailure(call: Call<CheckGetModel2>, t: Throwable) {
                // 실패
                Log.d("log",t.message.toString())
                Log.d("log","fail")
            }
        })
    }

    class ViewAdapter(
        val friend_List: friendListView,
    ) : RecyclerView.Adapter<ViewAdapter.ViewHolder>() {

        private lateinit var itemClickListner: ItemClickListener

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val friend_name: TextView
            val friend_achievement_rate: TextView
            val friend_Cumulative_time: TextView
            var friend_delete: ImageView
            var logo_icon: ImageView

            init {
                friend_name = itemView.findViewById(R.id.friend_name)
                friend_achievement_rate = itemView.findViewById(R.id.friend_achievement_rate)
                friend_Cumulative_time = itemView.findViewById(R.id.friend_Cumulative_time)
                friend_delete = itemView.findViewById(R.id.friend_delete)
                logo_icon = itemView.findViewById(R.id.logo_icon)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.friend_list_item, parent, false)
            return ViewHolder(view)
        }

        override fun getItemCount(): Int {
            return friend_List.FriendListview.size
        }

        // 리사이클러뷰 아이템클릭 리스너
        interface ItemClickListener {
            fun onClick(view: View, position: Int)
        }
        fun setItemClickListener(itemClickListener:ItemClickListener) {
            this.itemClickListner = itemClickListener
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {

            val friend = friend_List.FriendListview[position]

            holder.friend_name.text = friend.name
            holder.friend_achievement_rate.text = friend.achievementRate.toString()
            holder.friend_Cumulative_time.text = friend.accumulatedTime

            // 공부중 상태 확인
            if(friend.studyStatus){ //true - 공부중
                holder.friend_Cumulative_time.visibility = View.INVISIBLE
                holder.logo_icon.visibility = View.VISIBLE
            }
            else{ // false - 공부중 아님
                holder.friend_Cumulative_time.visibility = View.VISIBLE
                holder.logo_icon.visibility = View.INVISIBLE
            }


            // 리사이클러뷰 아이템클릭 리스너
            holder.friend_delete.setOnClickListener {
                itemClickListner.onClick(it, position)
            }
        }
    }
}