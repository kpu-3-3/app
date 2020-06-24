package com.example.smartpillbox3;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class postListAdapter extends BaseAdapter {
    private Context context;
    private List<board_item> boardList;

    public postListAdapter(Context context,List<board_item> boardList){
        this.context = context;
        this.boardList = boardList;
    }
    @Override
    public int getCount() {
        return boardList.size();
    }

    @Override
    public Object getItem(int position) {
        return boardList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = View.inflate(context, R.layout.activity_post,null);
        TextView titleText = (TextView)v.findViewById(R.id.titleText) ;
        TextView categoryText = (TextView)v.findViewById(R.id.categoryText) ;
        TextView medicineText = (TextView)v.findViewById(R.id.medicineText) ;
        TextView contentText = (TextView)v.findViewById(R.id.contentText) ;
        TextView dateText = (TextView)v.findViewById(R.id.dateText) ;

        titleText.setText(boardList.get(position).getTitle());
        categoryText.setText(boardList.get(position).getCategory());
        medicineText.setText(boardList.get(position).getMedicine());
        contentText.setText(boardList.get(position).getTitle());
        dateText.setText(boardList.get(position).getCategory());

        v.setTag(boardList.get(position).getTitle());
        return v;
    }



}
