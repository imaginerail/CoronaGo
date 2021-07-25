package com.aneeq.coronago.fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

import com.aneeq.coronago.R
import com.aneeq.coronago.util.ConnectionManager
import org.json.JSONException
import org.w3c.dom.Text
import java.text.DecimalFormat

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {
    lateinit var TotalConfirm: TextView
    lateinit var TotalFConfirm: TextView
    lateinit var TotalDeaths: TextView
    lateinit var TotalFDeaths: TextView
    lateinit var TotalRecovered: TextView
    lateinit var TotalFRecovered: TextView
    lateinit var NewConfirm: TextView
    lateinit var NewDeaths: TextView
    lateinit var NewRecovered: TextView
    lateinit var progressBar: ProgressBar
    lateinit var progressLayout: RelativeLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        progressBar = view.findViewById(R.id.progressBar)
        progressLayout = view.findViewById(R.id.progressLayout)
        progressLayout.visibility = View.GONE
        TotalConfirm = view.findViewById(R.id.TotalConfirm)
        TotalFConfirm = view.findViewById(R.id.TotalFConfirm)
        TotalDeaths = view.findViewById(R.id.TotalDeaths)
        TotalFDeaths = view.findViewById(R.id.TotalFDeaths)
        TotalRecovered = view.findViewById(R.id.TotalRecovered)
        TotalFRecovered = view.findViewById(R.id.TotalFRecovered)
        NewConfirm = view.findViewById(R.id.NewConfirm)
        NewDeaths = view.findViewById(R.id.NewDeaths)


        setUpGlobal(view)
        return view
    }

    private fun setUpGlobal(view: View) {
        val queue = Volley.newRequestQueue(activity as Context)
        val url = "https://covid-19.dataflowkit.com/v1/world"

        if (ConnectionManager().checkConnection(activity as Context)) {

//creating a json object
            val jsonObjectRequest = object : JsonObjectRequest(
                Method.GET, url, null, Response.Listener {

                    try {
                        progressLayout.visibility = View.GONE

                         TotalFConfirm.text="${it.getString("Total Cases_text")}"
                         NewConfirm.text="(${it.getString("New Cases_text")})"
                         TotalFDeaths.text="${it.getString("Total Deaths_text")}"
                                 NewDeaths.text="(${it.getString("New Deaths_text")})"
                        TotalFRecovered.text="${it.getString("Total Recovered_text")}"


                    } catch (e: JSONException) {
                        e.printStackTrace()
                        Toast.makeText(
                            activity as Context,
                            "Some UnExpected Error Occurred",
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }


                },
                Response.ErrorListener {
                    //println("Error is $it")
                    if (activity != null) {
                        Toast.makeText(
                            activity as Context,
                            "Volley Error Occurred",
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }
                }) {
                //headers are required for type and unique token
                /* override fun getHeaders(): MutableMap<String> {
                     val headers = hashMapOf<String>()
                     headers["Content-type"] = "application/json"
                     return headers
                 }*/
            }

            queue.add(jsonObjectRequest)
        } else {
            //creating a dialogue box if there is no internet connection

            val dialog = AlertDialog.Builder(activity as Context)
            dialog.setTitle("Failure")
            dialog.setMessage("Internet Connection NOT Found")
            dialog.setPositiveButton("Open Settings") { _, _ ->
                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                activity?.finish()
            }
            dialog.setNegativeButton("Exit") { _, _ ->
                ActivityCompat.finishAffinity(activity as Activity)
            }
            dialog.create()
            dialog.show()
        }

    }

}