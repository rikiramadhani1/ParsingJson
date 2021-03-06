package com.dicoding.parsingjson

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.parsingjson.model.DataItem
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter = UserAdapter(mutableListOf())

        rv_users.setHasFixedSize(true)
        rv_users.layoutManager = LinearLayoutManager(this)
        rv_users.adapter = adapter

        getUser()
    }

    private fun getUser() {
        val client = AsyncHttpClient()
        val url = "https://reqres.in/api/users?page=1"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                val response = String(responseBody)
                parseJson(response)
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                Toast.makeText(this@MainActivity, error.message, Toast.LENGTH_SHORT).show()
                error.printStackTrace()
            }
        })
    }

    private fun parseJson(response: String) {
        val jsonObject = JSONObject(response);
        val dataArray = jsonObject.getJSONArray("data")

        for (i in 0 until dataArray.length()) {
            val dataObject = dataArray.getJSONObject(i)
            val id = dataObject.getInt("id")
            val firstName = dataObject.getString("first_name")
            val lastName = dataObject.getString("last_name")
            val email = dataObject.getString("email")
            val avatar = dataObject.getString("avatar")

            val data = DataItem(
                id = id,
                firstName = firstName,
                lastName = lastName,
                email = email,
                avatar = avatar
            )

            adapter.addUser(data)
        }
    }
}
