package com.example.smartpillbox3;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class board extends AppCompatActivity {
    boardAdapter adapter;
    ListView listView;
//    private ListView boardListView;
////    private postListAdapter adapter;
    private List<board_item> boardList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

//        boardListView = (ListView)findViewById(R.id.boardListView);
//        boardList = new ArrayList<board_item>();
//        boardList.add(new board_item("먹은 후기","게보린","감기"));
//        boardList.add(new board_item("먹은 후기","게보린","감기"));
//
//        adapter = new postListAdapter(getApplicationContext(), boardList);
//        boardListView.setAdapter(adapter);

        final Button writeButton=(Button) findViewById(R.id.writeButton);
        writeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent writeIntent = new Intent(board.this,board_write.class);
                board.this.startActivity(writeIntent);
            }
        });

        // Adapter 생성
        adapter = new boardAdapter() ;

        // 리스트뷰 참조 및 Adapter달기
        listView = (ListView) findViewById(R.id.boardListView);
        listView.setAdapter(adapter);


        // 첫 번째 아이템 추가.
        //adapter.addItem("당뇨", "별로", "댱뇨 약 2알") ;
        // 두 번째 아이템 추가.
        //adapter.addItem("고혈압", "좋아요", "고혈압 약 1알") ;
        // 세 번째 아이템 추가.
        //adapter.addItem("저혈당", "그냥그래요", "저혈당 약 1알") ;

        new BackgroundTask().execute();
        //Toast.makeText(getApplicationContext(),"성공",Toast.LENGTH_SHORT).show();
    }

    class BackgroundTask extends AsyncTask<Void, Void, String> {

        String target;//접속할 홈페이지 주소
        @Override
        protected void onPreExecute(){
            target = "http://codms1104.dothome.co.kr/boardList.php";
        }

        @Override
        protected String doInBackground(Void... voids) {
            try{
                URL url = new URL(target);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String temp;
                StringBuilder stringBuilder = new StringBuilder();
                //temp에 저장
                while((temp=bufferedReader.readLine())!=null){  //null값이 아닐때까지 반복
                    stringBuilder.append(temp+"");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim(); //결과괎이 리턴되면 onPostExcute파라미터로 넘겨짐
            }catch(Exception e){
                e.printStackTrace();
            }
            return null;


        }

        @Override
        public void onProgressUpdate(Void... values){
            super.onProgressUpdate();
        }

        //가져온 데이터를 board_item객체에 넣은 뒤 리스트 뷰 출력을 휘한 list객체에 넣어줌줌
       @Override
        public void onPostExecute(String result){
            try{
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                int count=0;
                String userID, title,postDate, medicine,content,category,postNum;
                //json타입의 값을 하나씩 빼서 board_item객체에 저장 후 리스트에 추가
                while(count<jsonArray.length()){
                    JSONObject object = jsonArray.getJSONObject(count);
                    userID = object.getString("userID");
                    title = object.getString("title");
                    postDate = object.getString("postDate");
                    medicine = object.getString("medi_name");
                    content = object.getString("content");
                    category = object.getString("category");
                    postNum = object.getString("postNum");

                    //하나의 게시글에 대한 객체생성
                    //board_item boarditem = new board_item(title,medi_name,category);
                    //addItem(boarditem);
                    adapter.addItem(userID, title, postDate, medicine, content, category, postNum);
                    adapter.notifyDataSetChanged();
                    count++;
                }

            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
