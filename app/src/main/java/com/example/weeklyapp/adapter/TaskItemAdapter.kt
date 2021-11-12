package com.example.weeklyapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weeklyapp.R
import com.example.weeklyapp.databinding.ListTaskItemBinding
import com.example.weeklyapp.model.TaskModelClass

class TaskItemAdapter(private val context: Context, private val dataset: ArrayList<TaskModelClass>)  :
    RecyclerView.Adapter<TaskItemAdapter.ViewHolder>() {

    class ViewHolder(itemBinding: ListTaskItemBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        val taskTitle: TextView = itemBinding.itemTaskTitle
        val difficulty: TextView = itemBinding.itemDifficulty
        val recurring: TextView = itemBinding.itemRecurring
        val priority: TextView = itemBinding.itemPriority
        val dayAllotment: TextView = itemBinding.itemDayAllotment
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val adapterLayout =
            ListTaskItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: TaskModelClass = dataset[position]

        holder.taskTitle.text = item.task_title
        holder.difficulty.text = item.difficulty

        if (item.day_allotment.values.any { it == 1 }) {
            val listOfDays = mutableListOf<String>()
            for (day in item.day_allotment.filter { it.value == 1 }.keys) {
                listOfDays.add(day.slice(0..2))
            }
            holder.dayAllotment.text = listOfDays.joinToString(" ● ", "● ")
        } else {
            holder.dayAllotment.text = context.resources.getString(R.string.none)
        }

        if (item.priority == 1) {
            holder.priority.text = context.resources.getString(R.string.prioritize)
        } else {
            holder.priority.text = context.resources.getString(R.string.do_not_prioritize)
        }

        if (item.recurring == 1) {
            holder.recurring.text = context.resources.getString(R.string.recurring)
        } else {
            holder.recurring.text = context.resources.getString(R.string.not_recurring)
        }

    }

    override fun getItemCount() = dataset.size

}