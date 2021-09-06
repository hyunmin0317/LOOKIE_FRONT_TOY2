package com.example.myapplication.todo

import java.io.Serializable

class plan(var content: String, var status : Boolean = false, var studyId: Long = 0) : Serializable{
    override fun toString(): String {
        return content
    }
}

class planlist() {
    val Planlist = ArrayList<plan>()

    fun addPlan(plan: plan) {
        Planlist.add(plan)
    }

    fun lastindex(): Int {
        return Planlist.lastIndex
    }
}

