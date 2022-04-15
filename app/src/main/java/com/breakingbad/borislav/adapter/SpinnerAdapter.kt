package com.breakingbad.borislav.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.breakingbad.borislav.R

class SpinnerAdapter(private val context: Context, var dataSource: Array<String>) : BaseAdapter() {

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val view: View
        val viewHolder: ItemHolder
        if (convertView == null) {
            view = inflater.inflate(R.layout.single_season, parent, false)
            viewHolder = ItemHolder(view)
            view?.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ItemHolder
        }
        viewHolder.seasonName.text = dataSource[position]



        return view
    }

    override fun getItem(position: Int): Any? {
        return dataSource[position];
    }

    override fun getCount(): Int {
        return dataSource.size;
    }

    override fun getItemId(position: Int): Long {
        return position.toLong();
    }

    private class ItemHolder(row: View?) {
        val seasonName: TextView = row?.findViewById(R.id.season) as TextView
    }

}