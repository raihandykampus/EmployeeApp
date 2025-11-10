package com.example.employeeapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.employeeapp.databinding.ActivityDetailEmployeeBinding
import com.example.employeeapp.model.EmployeeDetailResponse
import com.example.employeeapp.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailEmployeeActivity : AppCompatActivity() {

    private val client = ApiClient.getInstance()

    private lateinit var binding: ActivityDetailEmployeeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailEmployeeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {

        }

        val employeeId = intent.getIntExtra("EXTRA_ID", -1)
        if (employeeId == -1) {
            Toast.makeText(
                this,
                "ID tidak valid",
                Toast.LENGTH_SHORT
            ).show()

            finish()
            return
        }

        // panggil fungsi untuk get API employee detail
        getEmployeeDetail(employeeId)
    }

    fun getEmployeeDetail(id: Int) {
        val response = client.getEmployeeDetail(id)

        response.enqueue(object : Callback<EmployeeDetailResponse>
        {
            override fun onResponse(
                p0: Call<EmployeeDetailResponse?>,
                response: Response<EmployeeDetailResponse?>
            )
            {
                if (!response.isSuccessful) {
                    Toast.makeText(
                        this@DetailEmployeeActivity,
                        "HTTP ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                val body = response.body()
                val employee = body?.data

                binding.txtName.setText(employee?.employeeName.toString())
                binding.txtAge.setText(employee?.age.toString())
                binding.txtSalary.setText(employee?.employeeSalary.toString())

            }

            override fun onFailure(p0: Call<EmployeeDetailResponse?>, p1: Throwable)
            {
                Toast.makeText(
                    this@DetailEmployeeActivity,
                    "Failed to get detail Employee",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}