package com.comtip.tip.tviewk.Adapter

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.comtip.tip.tuberook.R
import com.comtip.tip.tuberook.RoomDatabase.YoutubeData

/**
 * Created by TipRayong on 9/4/2561 12:20
 * TViewK
 */
class CustomListViewAdapter(context: Context,
                            internal val data: List<YoutubeData>)
    : ArrayAdapter<YoutubeData>(context, R.layout.listview_custom, data) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val viewHolder:ViewHolder
        val mConvertView:View
        if(convertView == null){
            mConvertView = (context as Activity).layoutInflater.inflate(R.layout.listview_custom,parent,false)
            viewHolder = ViewHolder(mConvertView)
            mConvertView.tag = viewHolder
        } else {
            mConvertView = convertView
            viewHolder = convertView.tag as ViewHolder
        }
        viewHolder.textviewCustom.text = data[position].name
        return mConvertView
    }

    private inner class ViewHolder(convertView: View){
        val textviewCustom: TextView = convertView.findViewById(R.id.textviewCustom) as TextView
    }
}

