package com.example.myapplication

class plan(var content: String, var isDone : Boolean = false) {

}

class planlist() {
    val planlist = ArrayList<plan>()

    fun addPlan(plan: plan) {
        planlist.add(plan)
    }
}