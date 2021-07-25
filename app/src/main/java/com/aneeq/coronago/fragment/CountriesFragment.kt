package com.aneeq.coronago.fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

import com.aneeq.coronago.R
import com.aneeq.coronago.adapter.CountryAdapter
import com.aneeq.coronago.model.Countries

import com.aneeq.coronago.util.ConnectionManager
import org.json.JSONArray

import org.json.JSONException


/**
 * A simple [Fragment] subclass.
 */
class CountriesFragment : Fragment() {
    lateinit var searchEditText: EditText
    lateinit var recycleCountry: RecyclerView
    private lateinit var countryRecyclerAdapter: CountryAdapter
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var progressLayout: RelativeLayout
    lateinit var search: SearchView
    lateinit var progressBar: ProgressBar
    val countriesList = arrayListOf<Countries>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_countries, container, false)
        setHasOptionsMenu(true)

        search = view.findViewById(R.id.search1)

        progressLayout = view.findViewById(R.id.progressLayout)
        progressBar = view.findViewById(R.id.progressBar)
        progressLayout.visibility = View.VISIBLE
        setUpRecycler(view)
        layoutManager = LinearLayoutManager(activity)
        ////////////////////////////////////////////////////////
        search.queryHint = "Type the name of Country here"
        searchEditText = view.findViewById(androidx.appcompat.R.id.search_src_text)
        searchEditText.setTextColor(ContextCompat.getColor(context!!, R.color.colorAccent))
        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                countryRecyclerAdapter.filter.filter(newText)
                return false
            }

        })

        return view
    }

    private fun setUpRecycler(view: View) {
        recycleCountry = view.findViewById(R.id.recycleCountries) as RecyclerView
        val queue = Volley.newRequestQueue(activity as Context)
        val url = "https://covid-19.dataflowkit.com/v1"

        if (ConnectionManager().checkConnection(activity as Context)) {

//creating a json object
            val jsonArrayRequest = object : JsonArrayRequest(
                Method.GET, url, null, Response.Listener {

                    progressLayout.visibility = View.GONE

                    try {
                        //val json = it.getJSONArray(0)
                        for (i in 0 until it.length()){
                        val resJsonObject = it.getJSONObject(i)
                        val restaurantObject = Countries(
                            resJsonObject.getString("Country_text"),
                            resJsonObject.getString("Total Cases_text"),
                            resJsonObject.getString("Total Deaths_text")
                        )
                        countriesList.add(restaurantObject)
                            if (activity != null) {
                            countryRecyclerAdapter =
                                CountryAdapter(activity as Context, countriesList)
                            val mLayoutManager =
                                LinearLayoutManager(activity)
                            recycleCountry.layoutManager = mLayoutManager
                            recycleCountry.itemAnimator = DefaultItemAnimator()
                            recycleCountry.adapter = countryRecyclerAdapter
                            recycleCountry.setHasFixedSize(true)
                        }
                    }

                        } catch (e: JSONException) {
                            e.printStackTrace()

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

                }


                    queue.add(jsonArrayRequest)
                } else {
                //creating a dialogue box if there is no internet connection

                val dialog = AlertDialog.Builder(activity as Context)
                dialog.setTitle("Failure")
                dialog.setMessage("Internet Connection NOT Found")
                dialog.setPositiveButton("Open Settings")
                {
                    _, _ ->
                    val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                    startActivity(settingsIntent)
                    activity?.finish()
                }
                dialog.setNegativeButton("Exit")
                {
                    _, _ ->
                    ActivityCompat.finishAffinity(activity as Activity)
                }
                dialog.create()
                dialog.show()
            }

        }
    }
