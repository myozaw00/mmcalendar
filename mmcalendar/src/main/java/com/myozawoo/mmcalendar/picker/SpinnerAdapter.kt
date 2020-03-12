package com.myozawoo.mmcalendar.picker

import android.content.Context
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.myozawoo.mmcalendar.R
import kotlinx.android.synthetic.main.item_spinner_item.view.*

class SpinnerAdapter(private val context: Context,
                    private val items: List<String>) : BaseAdapter() {



    private val layoutInflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = layoutInflater.inflate(R.layout.item_spinner_item, null)
        val data = items[position]
        view.tvEngYear.text = data.split(" ")[0]
        view.tvBurmeseYear.text = data.split(" ")[1]
        return view

    }

    override fun getItem(position: Int): Any {
        return ""
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return items.count()
    }
}