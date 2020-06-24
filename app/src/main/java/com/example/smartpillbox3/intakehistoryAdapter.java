package com.example.smartpillbox3;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;
public class intakehistoryAdapter extends BaseAdapter {
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<usermedi_item> intakehistory_item_List = new ArrayList<usermedi_item>() ;
    private Activity parentActivity;
    // ListViewAdapter의 생성자
    public intakehistoryAdapter() {

    }

    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return intakehistory_item_List.size() ;
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();
        final String userID= midimenu.userID;
        // "board_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.intakehistory_item, parent, false);
        }
        ViewGroup.LayoutParams layoutParams = convertView.getLayoutParams();
        layoutParams.height = 200;
        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView typeTextView = (TextView) convertView.findViewById(R.id.typeText) ;
        TextView categoryTextView = (TextView) convertView.findViewById(R.id.categoryText) ;
        TextView medicineTextView = (TextView) convertView.findViewById(R.id.medicineText) ;
        final TextView AtimeTextView = (TextView) convertView.findViewById(R.id.atimeText) ;
        final TextView DateTextView = (TextView) convertView.findViewById(R.id.dateText) ;
        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        usermedi_item listViewItem = intakehistory_item_List.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        typeTextView.setText(listViewItem.getType());
        categoryTextView.setText(listViewItem.getCategory());
        medicineTextView.setText(listViewItem.getMediname());
        AtimeTextView.setText(listViewItem.getA_time());
        DateTextView.setText(listViewItem.getDate());



//        final String userID, type, a_time, b_time, medicine, take, date, category;
////        userID=listViewItem.getUserID();
////        type=listViewItem.getType();
////        a_time=listViewItem.getA_time();
////        b_time = listViewItem.getB_time();
////        medicine=listViewItem.getMediname();
////        take = listViewItem.getTake();
////        date=listViewItem.getDate();


        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position ;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return intakehistory_item_List.get(position) ;
    }

    // 아이템 데이터 추가를 위한 함수
    public void addItem(String userID, String type, String a_time, String b_time, String medicine, String take, String date, String category) {
        usermedi_item item = new usermedi_item();
        item.setUserID(userID);
        item.setType(type);
        item.setA_time(a_time);
        item.setB_time(b_time);
        item.setMediname(medicine);
        item.setTake(take);
        item.setDate(date);
        item.setCategory(category);



        intakehistory_item_List.add(item);
    }
}
