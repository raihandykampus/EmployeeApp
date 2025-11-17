package com.example.employeeapp

import android.app.Activity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.employeeapp.databinding.ActivityMainBinding
import com.example.employeeapp.databinding.DialogCreateEmployeeBinding
import com.example.employeeapp.model.CreateEmployeeRequest
import com.example.employeeapp.model.CreateEmployeeResponse
import com.example.employeeapp.model.Employee
import com.example.employeeapp.model.EmployeeResponse
import com.example.employeeapp.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var listAdapter: ArrayAdapter<String>
    private val client = ApiClient.getInstance()
    private var employees: MutableList<Employee> = mutableListOf()
    private val detailActivityLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->

        // Cek apakah DetailActivity ditutup dengan sinyal "RESULT_OK"
        if (result.resultCode == Activity.RESULT_OK) {
            // Ambil ID yang dihapus
            val deletedId = result.data?.getIntExtra("deletedId", -1)

            if (deletedId != null && deletedId != -1) {
                // Cari item di list 'employees' berdasarkan ID
                val employeeToRemove = employees.find { it.id == deletedId }

                if (employeeToRemove != null) {
                    // 1. Hapus dari list data utama
                    employees.remove(employeeToRemove)

                    // 2. Hapus namanya dari adapter
                    listAdapter.remove(employeeToRemove.employeeName)

                    // 3. Beri tahu adapter untuk refresh
                    listAdapter.notifyDataSetChanged()

                    Toast.makeText(this, "Data berhasil dihapus dari list", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        employees = mutableListOf() // siapkan list kosong
        listAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, mutableListOf())
        binding.lvUsers.adapter = listAdapter

        binding.lvUsers.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val id = employees[position].id
            val intent = DetailEmployeeActivity.newIntent(this@MainActivity, id)
            detailActivityLauncher.launch(intent)
        }
        loadEmployees() // ambil data awal

        binding.btnCreate.setOnClickListener {
            showCreateDialog()
        }
    }

    private fun loadEmployees() {
        client.getAllEmployes().enqueue(object : Callback<EmployeeResponse> {
            override fun onResponse(call: Call<EmployeeResponse>, response: Response<EmployeeResponse>) {
                if (!response.isSuccessful) return

                employees = response.body()?.data?.toMutableList() ?: mutableListOf()
                val names = employees.map { it.employeeName }
                listAdapter.clear()
                listAdapter.addAll(names)
                listAdapter.notifyDataSetChanged()

            }


            override fun onFailure(call: Call<EmployeeResponse>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Get data failed", Toast.LENGTH_SHORT).show()
            }
        })
    }



    private fun showCreateDialog() {
        val dialogBinding = DialogCreateEmployeeBinding.inflate(layoutInflater)

        var builder = AlertDialog.Builder(this)
        builder.setTitle("Create Employee")
        builder.setView(dialogBinding.root)
        builder.setPositiveButton("Create") { dialog, _ ->
            val name = dialogBinding.etName.text.toString().trim()
            val salary = dialogBinding.etSalary.text.toString().toIntOrNull()
            val age = dialogBinding.etAge.text.toString().toIntOrNull()

            if (name.isEmpty() || salary == null || age == null) {
                Toast.makeText(
                    this@MainActivity,
                    "Isi semua data",
                    Toast.LENGTH_SHORT
                ).show()

                return@setPositiveButton
            }

            createEmployee(name, salary!!, age!!) {
                dialog.dismiss()
            }
        }

        builder.setNegativeButton("Cancel", null)
        builder.show()
    }

    private fun createEmployee(name: String, salary: Int, age: Int, onSuccess: () -> Unit) {
        val body = CreateEmployeeRequest(employeName = name,
            age = age,
            salary = salary)
        client.createEmployee(body).enqueue(object : Callback<CreateEmployeeResponse> {
            override fun onResponse(c: Call<CreateEmployeeResponse>, r: Response<CreateEmployeeResponse>) {
                if (!r.isSuccessful) {
                    Toast.makeText(this@MainActivity, "Create gagal: HTTP ${r.code()}", Toast.LENGTH_SHORT).show()
                    return
                }

                val emp = r.body()?.data ?: return
                val newEmployee = Employee(
                    id = emp.id,
                    employeeName = emp.employeeName,
                    employeeSalary = emp.employeeSalary,
                    employeeAge = emp.employeeAge,
                    profilImage = ""
                )
                employees.add(newEmployee)
                listAdapter.add(newEmployee.employeeName)
                listAdapter.notifyDataSetChanged()
                Toast.makeText(this@MainActivity, "Create berhasil!: HTTP ${r.code()}", Toast.LENGTH_SHORT).show()
                onSuccess()
            }
            override fun onFailure(c: Call<CreateEmployeeResponse>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Gagal: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}