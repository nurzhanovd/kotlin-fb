package com.example.fblab.ui

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ListView
import com.example.fblab.R
import com.example.fblab.adapter.TaskAdapter
import com.example.fblab.auth.Statics
import com.example.fblab.models.Task
import com.example.fblab.models.TaskListener
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), TaskListener {

    var db: FirebaseDatabase = FirebaseDatabase.getInstance()
    var dbRef: DatabaseReference? = db.reference

    lateinit var toDoItemList: MutableList<Task>

    lateinit var adapter: TaskAdapter
    private var listViewItems: ListView? = null


    var itemListener: ValueEventListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            addDataToList(dataSnapshot)
        }

        override fun onCancelled(databaseError: DatabaseError) {
            Log.w("MainActivity", "loadItem:onCancelled", databaseError.toException())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initUI()
        initFields()

    }

    private fun initUI() {
        fab.setOnClickListener {
            addNewItemDialog()
        }

    }

    private fun initFields() {
        toDoItemList = mutableListOf<Task>()
        adapter = TaskAdapter(this, toDoItemList)
        listViewItems = findViewById<View>(R.id.items_list) as ListView
        listViewItems!!.adapter = adapter

        dbRef?.orderByKey()?.addValueEventListener(itemListener)
    }

    fun findTaskById(id: String): DatabaseReference? {
        return dbRef?.child(Statics.FIREBASE_TASK)?.child(id)
    }

    override fun onTaskDelete(itemObjectId: String) {
        val taskRef = findTaskById(itemObjectId)
        taskRef?.removeValue()
    }

    override fun toggleTaskState(itemObjectId: String, status: Boolean) {
        val taskRef = findTaskById(itemObjectId)
//        taskRef?.updateChildren("done")
    }

    private fun addDataToList(dataSnapshot: DataSnapshot) {
        val items = dataSnapshot.children.iterator()
        if (items.hasNext()) {
            val toDoListindex = items.next()
            val itemsIterator = toDoListindex.children.iterator()

            while (itemsIterator.hasNext()) {
                val currentItem = itemsIterator.next()
                val todoItem = Task.create()
                val map = currentItem.value as HashMap<String, Any>
                todoItem.objectId = currentItem.key
                todoItem.done = map.get("done") as Boolean?
                todoItem.taskDesc = map.get("taskDesc") as String?
                toDoItemList.add(todoItem)
            }
        }
        adapter.notifyDataSetChanged()
    }

    @SuppressLint("RestrictedApi")
    fun createTask(todo: String) {

        val task = Task.create()

        task.taskDesc = todo
        task.done = false

        val newTask = dbRef?.child(Statics.FIREBASE_TASK)?.push()

        task.objectId = newTask?.key

        newTask?.setValue(task)

        txtNewTaskDesc.setText("")

        footer.visibility = View.GONE
        fab.visibility = View.VISIBLE
    }

    private fun addNewItemDialog() {
        val alert = AlertDialog.Builder(this)
        val itemEditText = EditText(this)
        alert.setMessage("Add New Item")
        alert.setTitle("Enter To Do Item Text")
        alert.setView(itemEditText)
        alert.setPositiveButton("Submit") { dialog, positiveButton ->
            createTask(itemEditText.text.toString())
        }
        alert.show()
    }


}
