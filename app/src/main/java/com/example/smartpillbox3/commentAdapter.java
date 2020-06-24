package com.example.smartpillbox3;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class commentAdapter extends BaseAdapter {
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<comment_item> comment_item_List = new ArrayList<comment_item>() ;

    // ListViewAdapter의 생성자
    public commentAdapter() {

    }

    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return comment_item_List.size() ;
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "comment_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.comment_item, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView userIDTextView = (TextView) convertView.findViewById(R.id.userIDText) ;
        TextView commentTextView = (TextView) convertView.findViewById(R.id.commentText) ;
        TextView commentDateTextView = (TextView) convertView.findViewById(R.id.commentDateText) ;

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        comment_item listViewItem = comment_item_List.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        userIDTextView.setText(listViewItem.getUserID());
        commentTextView.setText(listViewItem.getComment());
        commentDateTextView.setText(listViewItem.getCommentDate());

//        final String userID, commentNum, comment, commentDate, postNum;
//        commentNum=listViewItem.getCommentNum();
//        userID=listViewItem.getUserID();
//        postNum=listViewItem.getPostNum();
//        comment=listViewItem.getComment();
//        commentDate = listViewItem.getCommentDate();

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
        return comment_item_List.get(position) ;
    }

    // 아이템 데이터 추가를 위한 함수
    public void addItem(String commentNum, String userID, String postNum, String comment, String commentDate) {
        comment_item item = new comment_item();
        item.setCommentNum(commentNum);
        item.setUserID(userID);
        item.setPostNum(postNum);
        item.setComment(comment);
        item.setCommentDate(commentDate);

        comment_item_List.add(item);
    }
}
