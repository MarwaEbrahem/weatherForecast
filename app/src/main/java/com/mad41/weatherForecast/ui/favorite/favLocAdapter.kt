package com.mad41.weatherForecast.ui.favorite

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.mad41.weatherForecast.R
import com.mad41.weatherForecast.dataLayer.entity.favLocModel.favLocation
import com.mad41.weatherForecast.ui.MainActivity


class favLocAdapter(
    var favLocList: List<favLocation>,
    context: Context?,
    listner: favListner
) :
    RecyclerView.Adapter<favLocAdapter.CurrentViewHolder>() {
     val c = context
     val lis = listner
    fun dataChange(favLoc: List<favLocation>) {
        favLocList = favLoc
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int) = CurrentViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.fav_loc_card, parent, false)
    )

    override fun getItemCount() = favLocList.size

    override fun onBindViewHolder(holder: CurrentViewHolder, position: Int) {
        holder.bind(favLocList.get(position) , c ,lis )
    }

    class CurrentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val address =  view.findViewById<TextView>(R.id.desc)
        private val latitute =  view.findViewById<TextView>(R.id.lat)
        private val langitute =  view.findViewById<TextView>(R.id.lan)
        private val delete =  view.findViewById<Button>(R.id.deleteLoc)


        fun bind(
            favLoc: favLocation,
            context: Context?,
            lis: favListner
        ) {
            address.text = favLoc.address
            latitute.text = favLoc.lat.toString()
            langitute.text = favLoc.lon.toString()
            delete.setOnClickListener(View.OnClickListener {
                AlertDialog.Builder(context!!)
                    .setTitle(R.string.TitleDelete)
                    .setMessage(R.string.deleteEntry)
                    .setPositiveButton(
                        android.R.string.yes,
                        DialogInterface.OnClickListener { dialog, which ->
                            lis.DeleteLocation(favLoc.address)
                            if(lis.getAddressFromSharedPreference()!!.equals(favLoc.address)){
                                lis.writeFavInSharedPreference(false)
                            }
                        })
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show()
            })
            itemView.setOnClickListener(View.OnClickListener {
                System.out.println(favLoc.address)
                lis.saveLocationInSharedPreference(
                    "${favLoc.address}",
                    "${favLoc.lat},${favLoc.lon}"
                )
                val intent = Intent(context, MainActivity::class.java)
                context!!.startActivity(intent)
            })
        }
    }
}