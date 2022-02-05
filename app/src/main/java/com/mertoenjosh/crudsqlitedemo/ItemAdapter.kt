package com.mertoenjosh.crudsqlitedemo

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class ItemAdapter(val context: Context, val items: ArrayList<EmpModel>) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val elView = LayoutInflater.from(context).inflate(R.layout.items_row, parent, false)

        return ViewHolder(elView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.tvName.text = item.name
        holder.tvEmail.text = item.email

        if (position % 2 == 0) {
            holder.llMain.setBackgroundColor(ContextCompat.getColor(
                context, R.color.colorLightGray
            ))
        } else {
            holder.llMain.setBackgroundColor(ContextCompat.getColor(
                context, R.color.colorWhite

            ))
        }

        holder.tvEdit.setOnClickListener {view ->

            if (context is MainActivity) {
                context.updateRecordDialog(item)
            }
        }
        holder.tvDelete.setOnClickListener {view ->

            if (context is MainActivity) {
                context.deleteRecordAlertDialog(item)
            }
        }

    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){

        val llMain: LinearLayout = view.findViewById(R.id.llMain)
        val tvName: TextView = view.findViewById(R.id.tvName)
        val tvEmail: TextView = view.findViewById(R.id.tvEmail)
        val tvEdit: ImageView = view.findViewById(R.id.ivEdit)
        val tvDelete: ImageView = view.findViewById(R.id.ivDelete)

    }
}
