package com.aneeq.coronago.activity

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.aneeq.coronago.R
import com.aneeq.coronago.adapter.CountryAdapter
import com.aneeq.coronago.model.Countries
import com.aneeq.coronago.util.ConnectionManager
import org.json.JSONException

class DetailActivity : AppCompatActivity() {
    lateinit var TotalConfirm: TextView
    lateinit var TotalActive: TextView
    lateinit var TotalDeaths: TextView
    lateinit var TotalRecovered: TextView
    lateinit var txtCountry: TextView
    lateinit var LastUpdated: TextView
    lateinit var progressLayout: RelativeLayout
    lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        TotalConfirm =findViewById(R.id.TotalConfirm)
        TotalActive =findViewById(R.id.TotalActive)
        TotalDeaths=findViewById(R.id.TotalDeaths)
        TotalRecovered=findViewById(R.id.TotalRecovered)
        txtCountry=findViewById(R.id.txtCountry)
        LastUpdated=findViewById(R.id.LastUpdated)
        progressLayout=findViewById(R.id.progressLayout)
        progressBar=findViewById(R.id.progressBar)
        progressLayout.visibility = View.VISIBLE
        setUpDetails()
    }
    private fun  setUpDetails(){
        val queue = Volley.newRequestQueue(this@DetailActivity)

val countName=intent.getStringExtra("conname")
        val url = "https://covid-19.dataflowkit.com/v1/$countName"
        if (ConnectionManager().checkConnection(this@DetailActivity)) {


            val jsonobjectRequest = object : JsonObjectRequest(
                Method.GET, url, null, Response.Listener {

                    progressLayout.visibility = View.GONE

                    try {
                            txtCountry.text=it.getString("Country_text")
                            LastUpdated.text= "Last Updated: ${it.getString("Last Update")}"
                            TotalConfirm.text="Total Confirmed Cases:\n${it.getString("Total Cases_text")}\n(${it.getString("New Cases_text")})"
                            TotalActive.text="Total Active Cases:\n${it.getString("Active Cases_text")}"
                            TotalDeaths.text="Total Deaths:\n${it.getString("Total Deaths_text")}\n(${it.getString("New Deaths_text")})"
                            TotalRecovered.text="Total Recovered:\n${it.getString("Total Recovered_text")}"




                    } catch (e: JSONException) {
                        e.printStackTrace()

                    }


                },
                Response.ErrorListener {

                        Toast.makeText(
                            this@DetailActivity,
                            "Volley Error Occurred",
                            Toast.LENGTH_LONG
                        )
                            .show()

                }) {

            }


            queue.add(jsonobjectRequest)
        } else {

            val dialog = AlertDialog.Builder(this@DetailActivity)
            dialog.setTitle("Failure")
            dialog.setMessage("Internet Connection NOT Found")
            dialog.setPositiveButton("Open Settings")
            {
                    _, _ ->
                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                finish()
            }
            dialog.setNegativeButton("Exit")
            {
                    _, _ ->
                ActivityCompat.finishAffinity(this@DetailActivity)
            }
            dialog.create()
            dialog.show()
        }

    }
    }
