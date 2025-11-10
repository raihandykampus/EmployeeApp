package com.example.employeeapp

import android.R
import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.employeeapp.databinding.ActivityMainBinding
import com.example.employeeapp.model.EmployeeResponse
import com.example.employeeapp.network.ApiClient
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // api client
    private val client = ApiClient.getInstance()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with (binding) {
            loadEmployees()
        }
    }

    // fungsi untuk load data employee dari server
    fun loadEmployees() {
        val response = client.getAllEmployees()

        // lakukan request dengan async (tidak ditunggu)
        // ketika sudah dapat datanya baru diproses
        response.enqueue(object : Callback<EmployeeResponse> {
            override fun onResponse(
                call: Call<EmployeeResponse?>,
                response: Response<EmployeeResponse?>
            ) {
                if (!response.isSuccessful) {
                    Toast.makeText(
                        this@MainActivity,
                        "HTTP ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()

                    return
                }

                val body = response.body()
                val employees = body?.data.orEmpty()

                if (employees.isEmpty()) {
                    Toast.makeText(
                        this@MainActivity,
                        "Data kosong",
                        Toast.LENGTH_SHORT
                    ).show()

                    return
                }

                //
                val names = employees.map {it.employeeName}

                //
                val listAdapater = ArrayAdapter(
                    this@MainActivity,
                    R.layout.simple_list_item_1,
                    names
                )

                //
                binding.lvEmployee.adapter = listAdapater

                // jika item diklik
                binding.lvEmployee.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
                    // buka detail page sekalian kirim ID
                    val id = employees[position].id
                    val intent = Intent(this@MainActivity, DetailEmployeeActivity::class.java)
                    intent.putExtra("EXTRA_ID", id)
                    startActivity(intent)
                }
            }

            override fun onFailure(p0: Call<EmployeeResponse?>, p1: Throwable) {
                Toast.makeText(
                    this@MainActivity,
                    "Error ${p1.message}",
                    Toast.LENGTH_SHORT
                ).show()

            }
        })
    }
}