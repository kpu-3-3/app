package com.example.smartpillbox3;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

public class mypostAdapter extends BaseAdapter {
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<board_item> board_item_List = new ArrayList<board_item>() ;
    private Activity parentActivity;

    // ListViewAdapter의 생성자
    public mypostAdapter() {

    }

    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return board_item_List.size() ;
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "board_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.mypost_item, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView categoryTextView = (TextView) convertView.findViewById(R.id.categoryText) ;
        TextView contentTextView = (TextView) convertView.findViewById(R.id.titleText) ;
        TextView medicineTextView = (TextView) convertView.findViewById(R.id.medicineText) ;

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        board_item listViewItem = board_item_List.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        categoryTextView.setText(listViewItem.getCategory());
        contentTextView.setText(listViewItem.getTitle());
        medicineTextView.setText(listViewItem.getMedicine());

        final String userID, title, postDate, medicine, content, category, postNum;
        userID=listViewItem.getUserID();
        title=listViewItem.getTitle();
        postDate=listViewItem.getPostDate();
        medicine=listViewItem.getMedicine();
        content = listViewItem.getContent();
        category=listViewItem.getCategory();
        postNum=listViewItem.getpostNum();


        //클릭 시 이동
        TextView titleText = (TextView) convertView.findViewById(R.id.titleText);
        titleText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,PostActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("pos",pos);
                intent.putExtra("userID",userID);
                intent.putExtra("title",title);
                intent.putExtra("postDate",postDate);
                intent.putExtra("medicine",medicine);
                intent.putExtra("content",content);
                intent.putExtra("category",category);
                intent.putExtra("postNum",postNum);
                context.startActivity(intent);
            }
        });
        //버튼 클릭 시 삭제
        Button deleteButton = (Button) convertView.findViewById(R.id.deletepostButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if(success){
                                board_item_List.remove(pos); //리스트에서 해당부분을 지움
                                notifyDataSetChanged();//데이터가 변경된 것을 어댑터에 알려줌
                            }

                        }
                        catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                };
                MypostDeleteRequest deleteRequest = new MypostDeleteRequest(postNum, responseListener);
                //mypostAdapter는 Activity에서 상속받은 클래스가 아니므로 Activity값을 생성자로 받아서 사용한다.
                RequestQueue queue = Volley.newRequestQueue(context);
                queue.add(deleteRequest);
                Toast.makeText(context,"삭제되었습니다",Toast.LENGTH_SHORT).show();
            }
        });

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
        return board_item_List.get(position) ;
    }

    // 아이템 데이터 추가를 위한 함수
    public void addItem(String userID, String title, String postDate, String medicine, String content, String category, String postNum) {
        board_item item = new board_item();
        item.setUserID(userID);
        item.setpostTitle(title);
        item.setpostDate(postDate);
        item.setMedicine(medicine);
        item.setContent(content);
        item.setCategory(category);
        item.setPostNum(postNum);


        board_item_List.add(item);
    }
}
