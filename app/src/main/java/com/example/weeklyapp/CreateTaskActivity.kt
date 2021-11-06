package com.example.weeklyapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.Toast
import com.example.weeklyapp.databinding.ActivityCreateTaskBinding

class CreateTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateTaskBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // binding views from activity_main
        binding = ActivityCreateTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // enables the bottom navigation to close, and save data to database
        binding.botNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.btnCancel -> finish()
                R.id.btnAdd_another -> {
                    if (addTask()) {
                        binding.etTaskTitle.text?.clear()
                        binding.easyOption.isChecked = true
                        binding.notRecurringOption.isChecked = true
                        binding.noPriorityOption.isChecked = true
                        binding.cbAllWeek.isChecked = false
                        onCheckboxClicked(binding.cbAllWeek)
                    }
                }
                R.id.btnSave -> {
                    if (addTask()) {
                        finish()
                    }
                }
            }
            true
        }

    }

    /**
     * adds a task to the database
     * DB related
     */
    private fun addTask(): Boolean {
        val taskTitle = binding.etTaskTitle.text.toString()

        if (taskTitle.isNotEmpty()) { // makes sure there is data to insert
            Toast.makeText(applicationContext, "Task saved", Toast.LENGTH_LONG)
                .show() // shows a toast to confirm insertion
            binding.etTaskTitle.text.clear() // clears edit text after inserting the data
            return true

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
     * Enables checking boxes of which days to set the task
     * Note: Shorten code using a top-variable holding a collection of checkboxes truth values
     */
    fun onCheckboxClicked(view: View) {

        val listAllDays: List<CheckBox> = listOf(
            binding.cbMon,
            binding.cbTue,
            binding.cbWed,
            binding.cbThur,
            binding.cbFri,
            binding.cbSat,
            binding.cbSun
        )

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