package com.mertoenjosh.crudsqlitedemo

import android.app.AlertDialog
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var toolbar_main_activity: Toolbar
    private lateinit var btnAdd: Button
    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var rvItemsList: RecyclerView
    private lateinit var tvNoRecordsAvailable: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbar_main_activity = findViewById(R.id.toolbar_main_activity)
        setSupportActionBar(toolbar_main_activity)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setTitle("SQLite CRUD")
        toolbar_main_activity.setNavigationOnClickListener {
            onBackPressed()
        }

        // Initialize UI elements
        btnAdd = findViewById(R.id.btnAdd)
        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmailID)
        rvItemsList = findViewById(R.id.rvItemsList)
        tvNoRecordsAvailable = findViewById(R.id.tvNoRecordsAvailable)

        btnAdd.setOnClickListener { view ->
            addRecord(view)
        }

        setupListofDataIntoRecyclerView()

    }

    private fun addRecord(view: View) {
        val name = etName.text.toString()
        val email = etEmail.text.toString()
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        if (name.isNotEmpty() && email.isNotEmpty()) {
            val status = databaseHandler.addEmployee(EmpModel(0, name, email))

            if (status > -1) {
                Toast.makeText(applicationContext, "Employee added successfully", Toast.LENGTH_LONG).show()
                etName.text.clear()
                etEmail.text.clear()

                setupListofDataIntoRecyclerView()
            }
        } else {
            Toast.makeText(this, "Name or email cannot be empty", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupListofDataIntoRecyclerView() {
        if (getItemsList().size > 0) {
            rvItemsList.visibility = View.VISIBLE
            tvNoRecordsAvailable.visibility = View.GONE

            // 1. Set the layout manager
            rvItemsList.layoutManager = LinearLayoutManager(this)

            // 2. Initialize the adapter class
            val itemAdapter = ItemAdapter(this, getItemsList())

            // 3. set adapter to recyclerview
            rvItemsList.adapter = itemAdapter
        } else {
            rvItemsList.visibility = View.GONE
            tvNoRecordsAvailable.visibility = View.VISIBLE
        }
    }

    private fun getItemsList(): ArrayList<EmpModel> {
        // create an instance of the DatabaseHandler class
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)

        return databaseHandler.viewEmployee()
    }


    fun updateRecordDialog(empModel: EmpModel) {
        val updateDialog = Dialog(this, R.style.Theme_Dialog)
        updateDialog.setCancelable(false)

        // set the dialog layout
        updateDialog.setContentView(R.layout.dialog_update)

        // set preview values
        val nameField: EditText = updateDialog.findViewById(R.id.etUpdateName)
        val emailField: EditText = updateDialog.findViewById(R.id.etUpdateEmail)
        nameField.setText(empModel.name)
        emailField.setText(empModel.email)

        // Update dialog
        val btnUpdate: TextView = updateDialog.findViewById(R.id.tvUpdate)
        val btnCancel: TextView = updateDialog.findViewById(R.id.tvCancel)

        btnUpdate.setOnClickListener {
            val name = nameField.text.toString()
            val email = emailField.text.toString()

            val databaseHandler: DatabaseHandler = DatabaseHandler(this)

            if (name.isNotEmpty() && email.isNotEmpty()) {
                val status =
                    databaseHandler.updateEmployee(EmpModel(empModel.id, name, email))

                if (status > -1) {
                    Toast.makeText(applicationContext, "Employee data updated", Toast.LENGTH_SHORT).show()
                    setupListofDataIntoRecyclerView()
                    updateDialog.dismiss() // dismiss the dialog
                } else {
                    Toast.makeText(applicationContext, "Name or Email cannot be blank", Toast.LENGTH_SHORT).show()
                }
            }
        }

        btnCancel.setOnClickListener {
            updateDialog.dismiss()
        }

        // show the dialog
        updateDialog.show()
    }

    fun deleteRecordAlertDialog(empModel: EmpModel) {

        // create an instance of an alert dialog builder
        val builder = AlertDialog.Builder(this)

        // set title for alert dialog
        builder.setTitle("Delete Record")
        // set message for alert dialog
        builder.setMessage("Are you sure you want to delete ${empModel.name}")
        // set the dialog icon
        builder.setIcon(R.drawable.ic_alert)

        // perform positive action
        builder.setPositiveButton("Yes") { dialogInterface, _ ->

            // Create a database handler instance
            val databaseHandler = DatabaseHandler(this)
            val status = databaseHandler.deleteEmployee(EmpModel(empModel.id, "", ""))

            if (status > -1) {
                Toast.makeText(applicationContext, "Deleted successfully", Toast.LENGTH_LONG).show()
                setupListofDataIntoRecyclerView()
            }

            dialogInterface.dismiss() // dismiss dialog
        }

        builder.setNegativeButton("No") { dialogInterface, _ ->
            dialogInterface.dismiss()
        }

        // create the alert dialog
        val alertDialog = builder.create()

        // addition: Will not dismiss by clicking any other part of the screen
        alertDialog.setCancelable(false)

        // show the alert dialog
        alertDialog.show()

    }
}