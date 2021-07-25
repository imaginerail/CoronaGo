package com.aneeq.coronago.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aneeq.coronago.R
import com.aneeq.coronago.activity.DetailActivity
import com.aneeq.coronago.model.Countries
import java.util.*
import kotlin.collections.ArrayList

class CountryAdapter(val context: Context, var conList: ArrayList<Countries>) :
    RecyclerView.Adapter<CountryAdapter.CountriesViewHolder>(), Filterable {
    var searchFilterList = ArrayList<Countries>()

    init {
        searchFilterList = conList
    }

    class CountriesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtConName: TextView = view.findViewById(R.id.txtConName)
        val txtConConf: TextView = view.findViewById(R.id.txtConConf)
        val ConCof: TextView = view.findViewById(R.id.ConCof)
        val txtConDeath: TextView = view.findViewById(R.id.txtConDeath)
        val ConDeath: TextView = view.findViewById(R.id.ConDeath)
        val rlContent: RelativeLayout = view.findViewById(R.id.rlContent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountriesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.country_recycler_single_row, parent, false)
        return CountriesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return searchFilterList.size
    }

    override fun onBindViewHolder(holder: CountriesViewHolder, position: Int) {
        val countries = searchFilterList[position]
        holder.txtConName.text = countries.name
        holder.ConCof.text = countries.TotalConfirmed
        holder.ConDeath.text = countries.TotalDeaths

        holder.txtConName.setOnClickListener(View.OnClickListener {
            val intent = Intent(context, DetailActivity::class.java)
           intent.putExtra("conname", countries.name)
            //intent.putExtra("name", holder.txtResName.text.toString())
            context.startActivity(intent)

        })
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(p0: CharSequence?): FilterResults {
                val charString = p0.toString()
                if (charString.isEmpty()) {
                    searchFilterList = conList
                } else {
                    val filteredList: ArrayList<Countries> = ArrayList()

                    for (row in conList) {
                        if (row.name?.toLowerCase(Locale.getDefault())!!.contains(charString)
                        ) {
                            filteredList.add(row)
                        }
                    }

                    searchFilterList = filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = searchFilterList
                return filterResults
            }

            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                searchFilterList = p1?.values as ArrayList<Countries>
                notifyDataSetChanged()
            }

        }
    }
}