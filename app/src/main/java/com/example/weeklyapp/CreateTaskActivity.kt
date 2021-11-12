package com.example.weeklyapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.Toast
import com.example.weeklyapp.data.DatabaseHandler
import com.example.weeklyapp.databinding.ActivityCreateTaskBinding
import com.example.weeklyapp.model.TaskModelClass

class CreateTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateTaskBinding
    private lateinit var listAllDays: List<CheckBox>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enables ViewBinding for easier access to views in ActivityCreateTask.
        binding = ActivityCreateTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Sets the checkboxes' on click function to onCheckboxClicked.
        listAllDays = listOf(
            binding.cbMon,
            binding.cbTue,
            binding.cbWed,
            binding.cbThur,
            binding.cbFri,
            binding.cbSat,
            binding.cbSun
        )
        for (day in listAllDays) {
            day.setOnClickListener { onCheckboxClicked(day) }
        }
        binding.cbAllWeek.setOnClickListener { onCheckboxClicked(it) }

        // Enables the bottom navigation to close and save data to database.
        binding.botNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menuCancel -> finish()
                R.id.menuAdd_another -> {
                    if (addTask()) {
                        binding.etTaskTitle.text?.clear()
                        binding.easyOption.isChecked = true
                        binding.notRecurringOption.isChecked = true
                        binding.noPriorityOption.isChecked = true
                        binding.cbAllWeek.isChecked = false
                        onCheckboxClicked(binding.cbAllWeek)
                    }
                }
                R.id.menuSave -> {
                    if (addTask()) {
                        finish()
                    }
                }
            }
            true
        }

    }

    /**
     * Organizes the input and puts it in a TaskModel instance to pass to insertData.
     */
    private fun addTask(): Boolean {
        val taskTitle = binding.etTaskTitle.text.toString()
        val difficulty = when (binding.rgDifficultyOptions.checkedRadioButtonId) {
            R.id.easy_option -> this.resources.getString(R.string.easy)
            R.id.medium_option -> this.resources.getString(R.string.medium)
            else -> this.resources.getString(R.string.hard)
        }
        val recurring = when (binding.rgRecurringOptions.checkedRadioButtonId) {
            R.id.recurring_option -> 1
            else -> 0
        }
        val priority = when (binding.rgPriorityOptions.checkedRadioButtonId) {
            R.id.with_priority_option -> 1
            else -> 0
        }
        val dayAllotment = mutableMapOf<String, Int>()

        for (day in listAllDays) {
            if (day.isChecked) {
                dayAllotment[day.text.toString()] = 1
            } else {
                dayAllotment[day.text.toString()] = 0
            }
        }

        val databaseHandler = DatabaseHandler(this)

        if (taskTitle.isNotEmpty()) { // makes sure there is data to insert
            val status = databaseHandler.insertData(
                TaskModelClass(
                    0,
                    taskTitle,
                    difficulty,
                    recurring,
                    priority,
                    dayAllotment
                )
            ) // creates an EmpModelClass instance and calls addTaskDB to add the task
            if (status > -1) {
                Toast.makeText(applicationContext, "Task saved", Toast.LENGTH_LONG)
                    .show() // shows a toast to confirm insertion
                return true

            }

        } else {
            Toast.makeText(
                applicationContext,
                "Task Title cannot be blank",
                Toast.LENGTH_LONG
            ).show() // shows a toast that title needs to have value

        }
        return false
    }

    /**
     * Enables checking boxes of which days to set the task.
     */
    private fun onCheckboxClicked(view: View) {

        if (view is CheckBox) {
            val checked: Boolean = view.isChecked

            when (view.id) {
                R.id.cbAll_week -> {
                    for (day in listAllDays) {
                        day.isChecked = checked
                    }
                }
                else -> {
                    if (!checked) {
                        binding.cbAllWeek.isChecked = false
                    } else if (listAllDays.all { it.isChecked }) {
                        binding.cbAllWeek.isChecked = true
                    }

                }

            }

        }

    }

}