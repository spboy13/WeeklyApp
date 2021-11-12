package com.example.weeklyapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weeklyapp.adapter.TaskItemAdapter
import com.example.weeklyapp.data.DatabaseHandler
import com.example.weeklyapp.databinding.ActivityMainBinding
import com.example.weeklyapp.model.TaskModelClass

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enables ViewBinding to easily access views in ActivityMain.
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Listens to the FAB to start the CreateTask activity.
        binding.fab.setOnClickListener {
            val intent = Intent(this, CreateTaskActivity::class.java)
            startActivity(intent)
        }

        // Enables left-side navigation in home screen.
        setSupportActionBar(binding.contentMain.toolbar)
        val toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.contentMain.toolbar,
            R.string.open,
            R.string.close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.isDrawerIndicatorEnabled = true
        toggle.syncState()

        // Shows the task list when the application is opened.
        setupListOfDataIntoRecyclerView()

    }

    /**
     * Gets the tasks in the database.
     */
    private fun getItemsList(): ArrayList<TaskModelClass> {
        val databaseHandler = DatabaseHandler(this)
        return databaseHandler.viewData()
    }

    /**
     * Connects TaskItemAdapter and gives it the task list to set up the recycler view.
     */
    private fun setupListOfDataIntoRecyclerView() {

        binding.contentMain.rvTaskList.visibility = View.VISIBLE

        // Set the LayoutManager that this RecyclerView will use.
        binding.contentMain.rvTaskList.layoutManager = LinearLayoutManager(this)
        // Adapter class is initialized and list is passed in the param.
        val taskItemAdapter = TaskItemAdapter(this, getItemsList())
        // adapter instance is set to the recyclerview to inflate the items.
        binding.contentMain.rvTaskList.adapter = taskItemAdapter
    }

    override fun onResume() {
        super.onResume()
        setupListOfDataIntoRecyclerView()
    }

}