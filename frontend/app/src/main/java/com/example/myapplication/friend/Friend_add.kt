package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.friend.*
import com.example.myapplication.todo.Todo_main
import kotlinx.android.synthetic.main.friend_add.*
import kotlinx.android.synthetic.main.friend_add_list_item.view.*
import kotlinx.android.synthetic.main.friend_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Friend_add : AppCompatActivity() {
    val api = APIS.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.friend_add)

        // 검색어를 입력할 Input 창
        val friend_search_input : EditText= findViewById<EditText>(R.id.friend_search_input)
        // 검색어 검색 버튼
        val friend_search_button : Button= findViewById<Button>(R.id.friend_search_button)
        // 친구 추가 버튼
        val friend_add_button:Button = findViewById<Button>(R.id.friend_add_button)

        val back_button: ImageView = findViewById<ImageView>(R.id.back_button)

        val checked_user_name = findViewById<View>(R.id.checked_user_name) as TextView
        val checked_user_email = findViewById<View>(R.id.checked_user_email)as TextView


        val friendaddlist: friendaddList = friendaddList()

        var userId: Long = 401

        // 리싸이클러뷰 어댑터 설정
        val mAdapter=Friend_Adapter(friendaddlist)
        val manager = LinearLayoutManager(this)
        manager.reverseLayout = false
        manager.stackFromEnd = false
        friend_add_recyclerView.layoutManager = manager

        mAdapter.setItemClickListener(object : Friend_Adapter.ItemClickListener {
            override fun onClick(view:View, position: Int) {
                // 선택한 유저 닉네임, 이메일 표시
                val name: String = friendaddlist.FriendaddList[position].name
                val email: String = friendaddlist.FriendaddList[position].email
                userId = friendaddlist.FriendaddList[position].userId

                checked_user_name.text = name
                checked_user_email.text = email

                friend_add_recyclerView.layoutManager = manager
            }
        })

        // 친구 검색 기능 구현
        friend_search_button.setOnClickListener {
            // 리스트 초기화 할 필요 있음
            friendaddlist.FriendaddList.clear()

            (application as MasterApplication).api.get_users(friend_search_input.text.toString()).enqueue(object : Callback<CheckGetModel> {
                override fun onResponse(call: Call<CheckGetModel>, response: Response<CheckGetModel>) {
                    Log.d("log", response.toString())

                    // 받은 유저정보 -닉네임, 이메일- 리사이클러뷰로 보이기
                    if(response.isSuccessful){
                        for(i in response.body()!!.checkRoomList.indices){
                            val userid = response.body()!!.checkRoomList[i].userId
                            val name= response.body()!!.checkRoomList[i].username
                            val email= response.body()!!.checkRoomList[i].email
                            Log.d("log", name)
                            Log.d("log", email)

                            // 리스트에 추가
                            friendaddlist.addFriend(
                                addlist(
                                    userid,
                                    name,
                                    email
                                )
                            )
                        }
                    }
                    else{
                        Toast.makeText(this@Friend_add, "존재하지 않는 닉네임입니다", Toast.LENGTH_LONG).show()
                    }


                    // 리사이클러뷰 설정
                    friend_add_recyclerView.adapter = mAdapter

                }
                override fun onFailure(call: Call<CheckGetModel>, t: Throwable) {
                    // 실패
                    Log.d("log",t.message.toString())
                    Log.d("log","fail")
                }
            })
        }


        // 친구 추가 기능 구현
        friend_add_button.setOnClickListener {
            (application as MasterApplication).api.post_users(userId).enqueue(object : Callback<PostModel> {
                override fun onResponse(call: Call<PostModel>, response: Response<PostModel>) {
                    Log.d("log", response.toString())

                    Log.d("log", userId.toString())
                    Log.d("log", checked_user_name.toString())
                    Log.d("log", checked_user_email.toString())

                    if(response.body()!!.code == 200){
                        Toast.makeText(this@Friend_add, response.body()!!.message, Toast.LENGTH_LONG).show()

                        finish()
                    }
                    else if(response.body()!!.code == 400){
                        Toast.makeText(this@Friend_add, response.body()!!.message, Toast.LENGTH_LONG).show()
                    }
                    else if(response.body()!!.code == 401){
                        Toast.makeText(this@Friend_add, response.body()!!.message, Toast.LENGTH_LONG).show()
                    }

                }
                override fun onFailure(call: Call<PostModel>, t: Throwable) {
                    // 실패
                    Log.d("log",t.message.toString())
                    Log.d("log","fail")
                }
            })



            finish()
        }


        back_button.setOnClickListener {
            finish()
        }




    }


    class Friend_Adapter(
        val friend_addList: friendaddList,
    ) : RecyclerView.Adapter<Friend_Adapter.ViewHolder>() {

        private lateinit var itemClickListner: ItemClickListener

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val friend_name: TextView
            val friend_Email: TextView
            val mRootView:LinearLayout

            init {
                friend_name = itemView.findViewById(R.id.friend_name_item)
                friend_Email = itemView.findViewById(R.id.friend_Email_item)
                mRootView = itemView.findViewById(R.id.mRootView)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.friend_add_list_item, parent, false)
            return ViewHolder(view)
        }

        override fun getItemCount(): Int {
            return friend_addList.FriendaddList.size
        }

        // 리사이클러뷰 아이템클릭 리스너
        interface ItemClickListener {
            fun onClick(view: View, position: Int)
        }
        fun setItemClickListener(itemClickListener:ItemClickListener) {
            this.itemClickListner = itemClickListener
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {

            val friend = friend_addList.FriendaddList[position]

            holder.friend_name.text = friend.name
            holder.friend_Email.text = friend.email

            // 리사이클러뷰 아이템클릭 리스너
            holder.mRootView.setOnClickListener {
                itemClickListner.onClick(it, position)
            }


        }
    }
}