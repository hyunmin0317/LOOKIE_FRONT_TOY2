package com.example.myapplication.friend


class list(var name: String, var rate: String, var time: String)

class friendList {
    val FriendList = ArrayList<list>()

    fun addFriend(list: list) {
        FriendList.add(list)
    }
}