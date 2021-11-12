package com.example.weeklyapp.data

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.example.weeklyapp.model.TaskModelClass

class DatabaseHandler(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "TaskDataBase"

        private const val TABLE_TASKS = "TaskTable"

        private const val KEY_ID = "_id"
        private const val KEY_TASK_TITLE = "task_title"
        private const val KEY_DIFFICULTY = "difficulty"
        private const val KEY_RECURRING = "recurring"
        private const val KEY_PRIORITY = "prioritize"
        private const val KEY_MON = "monday"
        private const val KEY_TUE = "tuesday"
        private const val KEY_WED = "wednesday"
        private const val KEY_THURS = "thursday"
        private const val KEY_FRI = "friday"
        private const val KEY_SAT = "saturday"
        private const val KEY_SUN = "sunday"

    }

    /**
     * Required function.
     * Creates a TaskTable for storing the tasks.
     */
    override fun onCreate(db: SQLiteDatabase?) {
        val createTask =
            ("CREATE TABLE $TABLE_TASKS($KEY_ID INTEGER PRIMARY KEY, " +
                    "$KEY_TASK_TITLE TEXT, " +
                    "$KEY_DIFFICULTY TEXT, " +
                    "$KEY_RECURRING INTEGER, " +
                    "$KEY_PRIORITY INTEGER, " +
                    "$KEY_MON INTEGER, " +
                    "$KEY_TUE INTEGER, " +
                    "$KEY_WED INTEGER, " +
                    "$KEY_THURS INTEGER, " +
                    "$KEY_FRI INTEGER, " +
                    "$KEY_SAT INTEGER, " +
                    "$KEY_SUN INTEGER)")
        db?.execSQL(createTask)
    }

    /**
     * Required function.
     * Drops table if one already exists.
     */
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_TASKS")
        onCreate(db)
    }

    /**
     * Inserts data into the TaskTable.
     */
    fun insertData(emp: TaskModelClass): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        // Prepares to insert the data by putting it into a class instance of ContentValues().
        contentValues.put(KEY_TASK_TITLE, emp.task_title)
        contentValues.put(KEY_DIFFICULTY, emp.difficulty)
        contentValues.put(KEY_RECURRING, emp.recurring)
        contentValues.put(KEY_PRIORITY, emp.priority)

        for (key in emp.day_allotment.keys) {
            when (key) {
                "Monday" -> contentValues.put(KEY_MON, emp.day_allotment[key])
                "Tuesday" -> contentValues.put(KEY_TUE, emp.day_allotment[key])
                "Wednesday" -> contentValues.put(KEY_WED, emp.day_allotment[key])
                "Thursday" -> contentValues.put(KEY_THURS, emp.day_allotment[key])
                "Friday" -> contentValues.put(KEY_FRI, emp.day_allotment[key])
                "Saturday" -> contentValues.put(KEY_SAT, emp.day_allotment[key])
                "Sunday" -> contentValues.put(KEY_SUN, emp.day_allotment[key])
            }
        }
        // Inserts a single task into the TableTask.
        val success = db.insert(TABLE_TASKS, null, contentValues)
        db.close() // Important to close the connection.

        return success // Returns a Boolean.
    }

    @SuppressLint("Range")
            /**
             *  Returns the list of tasks from the TaskTable.
             */
    fun viewData(): ArrayList<TaskModelClass> {
        val empList: ArrayList<TaskModelClass> = ArrayList()
        // Query to select all the records from the table.
        val selectQuery = "SELECT * FROM $TABLE_TASKS"
        val db = this.readableDatabase
        // Cursor is used to read the record one by one and add them to data model class.
        val cursor: Cursor?
        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id: Int
        var title: String
        var diff: String
        var recur: Int
        var prio: Int
        var mon: Int
        var tue: Int
        var wed: Int
        var thurs: Int
        var fri: Int
        var sat: Int
        var sun: Int
        // Reads the details of each row, put them into a TaskModel instance, and into a ArrayList.
        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                title = cursor.getString(cursor.getColumnIndex(KEY_TASK_TITLE))
                diff = cursor.getString(cursor.getColumnIndex(KEY_DIFFICULTY))
                recur = cursor.getInt(cursor.getColumnIndex(KEY_RECURRING))
                prio = cursor.getInt(cursor.getColumnIndex(KEY_PRIORITY))
                mon = cursor.getInt(cursor.getColumnIndex(KEY_MON))
                tue = cursor.getInt(cursor.getColumnIndex(KEY_TUE))
                wed = cursor.getInt(cursor.getColumnIndex(KEY_WED))
                thurs = cursor.getInt(cursor.getColumnIndex(KEY_THURS))
                fri = cursor.getInt(cursor.getColumnIndex(KEY_FRI))
                sat = cursor.getInt(cursor.getColumnIndex(KEY_SAT))
                sun = cursor.getInt(cursor.getColumnIndex(KEY_SUN))

                val week = mutableMapOf(
                    "Monday" to mon,
                    "Tuesday" to tue,
                    "Wednesday" to wed,
                    "Thursday" to thurs,
                    "Friday" to fri,
                    "Saturday" to sat,
                    "Sunday" to sun,
                )

                val emp = TaskModelClass(
                    id = id,
                    task_title = title,
                    difficulty = diff,
                    recurring = recur,
                    priority = prio,
                    day_allotment = week
                )
                empList.add(emp)
            } while (cursor.moveToNext())

        }

        cursor.close()
        return empList

    }

}