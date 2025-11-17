package com.example.employeeapp

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.employeeapp.databinding.ActivityDetailEmployeeBinding
import com.example.employeeapp.model.EmployeeDetailResponse
import com.example.employeeapp.model.UpdateEmployeeRequest
import com.example.employeeapp.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.widget.EditText
import com.example.employeeapp.model.DeleteResponse
import com.example.employeeapp.model.UpdateResponse

class DetailEmployeeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailEmployeeBinding

    private val client = ApiClient.getInstance()

    private var employeeId: Int = -1
    companion object {
        private const val EXTRA_EMPLOYEE_ID = "extra_employee_id"

        fun newIntent(context: Context, employeeId: Int): Intent =
            Intent(context, DetailEmployeeActivity::class.java)
                .putExtra(EXTRA_EMPLOYEE_ID, employeeId)

        fun start(context: Context, employeeId: Int) {
            context.startActivity(newIntent(context, employeeId))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailEmployeeBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val employeeId = intent.getIntExtra(EXTRA_EMPLOYEE_ID, -1)
        if (employeeId == -1) {
            Toast.makeText(
                this,
                "ID tidak valid",
                Toast.LENGTH_SHORT
            ).show()

            finish()
            return
        }

        getDetailEmployee(employeeId)

        binding.btnDelete.setOnClickListener {
            deleteEmployee(employeeId)
        }
        binding.btnEdit.setOnClickListener {
            showEditDialog(employeeId)
        }
    }

    private fun getDetailEmployee(id: Int) {
        val response = client.getAllEmployeDetail(id)

        // lakukan request dengan async (tidak ditunggu)
        // ketika sudah dapat datanya baru di proses
        response.enqueue(object: Callback<EmployeeDetailResponse> {
            override fun onResponse(
                call: Call<EmployeeDetailResponse?>,
                response: Response<EmployeeDetailResponse?>
            ) {
                if (!response.isSuccessful) {
                    Toast.makeText(
                        this@DetailEmployeeActivity,
                        "HTTP ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()

                    return
                }

                val body = response.body()
                val employee = body?.data

                val name = employee?.employeeName ?: "Data Kosong"
                val age = employee?.employeeAge ?: 0
                val salary = employee?.employeeSalary ?: 0

                binding.txtName.setText("Name: $name")
                binding.txtAge.setText("Age: $age")
                binding.txtSalary.setText("Salary: $salary")
            }

            override fun onFailure(p0: Call<EmployeeDetailResponse?>, p1: Throwable) {
                Toast.makeText(
                    this@DetailEmployeeActivity,
                    "Get data failed",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
    private fun deleteEmployee(id: Int) {
        client.deleteEmployee(id).enqueue(object: Callback<DeleteResponse> {
            override fun onResponse(
                call: Call<DeleteResponse>,
                response: Response<DeleteResponse>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(this@DetailEmployeeActivity, "Deleted", Toast.LENGTH_SHORT).show()
                    val intent = Intent()
                    intent.putExtra("deletedId", id)
                    setResult(RESULT_OK, intent)
                    finish()

                } else {
                    Toast.makeText(this@DetailEmployeeActivity, "Delete gagal (HTTP ${response.code()})", Toast.LENGTH_SHORT).show()
                    Log.d("API", "code = ${response.code()}, body = ${response.errorBody()?.string()}")

                }
            }
            override fun onFailure(call: Call<DeleteResponse>, t: Throwable) {
                Toast.makeText(this@DetailEmployeeActivity, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun updateEmployee(id: Int, name: String, salary: Int, age: Int) {
        val body = UpdateEmployeeRequest(
            name = name,
            salary = salary,
            age = age
        )

        client.updateEmployee(id, body).enqueue(object : Callback<UpdateResponse> {
            override fun onResponse(
                call: Call<UpdateResponse>,
                response: Response<UpdateResponse>
            ) {
                if (!response.isSuccessful) {
                    Toast.makeText(
                        this@DetailEmployeeActivity,
                        "Update gagal (HTTP ${response.code()})",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }

                val updatedData = response.body()?.updatedData
                if (updatedData != null) {
                    binding.txtName.text = "Name: ${updatedData.name}"
                    binding.txtAge.text = "Age: ${updatedData.age}"
                    binding.txtSalary.text = "Salary: ${updatedData.salary}"
                }

                Toast.makeText(this@DetailEmployeeActivity, "Update berhasil", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<UpdateResponse>, t: Throwable) {
                Toast.makeText(this@DetailEmployeeActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showEditDialog(id: Int) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_create_employee, null)

        val etName = dialogView.findViewById<EditText>(R.id.etName)
        val etSalary = dialogView.findViewById<EditText>(R.id.etSalary)
        val etAge = dialogView.findViewById<EditText>(R.id.etAge)

        // PRE-FILL DATA LAMA
        etName.setText(binding.txtName.text.toString().replace("Name: ", ""))
        etSalary.setText(binding.txtSalary.text.toString().replace("Salary: ", ""))
        etAge.setText(binding.txtAge.text.toString().replace("Age: ", ""))

        AlertDialog.Builder(this)
            .setTitle("Edit Employee")
            .setView(dialogView)
            .setPositiveButton("Update") { _, _ ->
                val name = etName.text.toString()
                val salary = etSalary.text.toString().toInt()
                val age = etAge.text.toString().toInt()

                updateEmployee(id, name, salary, age)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

}