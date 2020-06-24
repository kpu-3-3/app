package com.example.smartpillbox3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class MypageMainActivity extends AppCompatActivity {

    mypostAdapter mypost_adapter;
    mycommentAdapter mycomment_adapter;
    ListView mypostlistView, mycommentlistView;
    private List<board_item> boardList;
    private List<mycomment_item> mycommentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage_main);

        Button checkmedisettingButton=(Button) findViewById(R.id.checkmedisettingButton);
        Button intakehistoryButton=(Button) findViewById(R.id.intakehistoryButton);



        intakehistoryButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), IntakehistoryActivity.class);
                startActivity(intent); // 다음 화면으로 넘어간다
            }
        });

        checkmedisettingButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CheckmediSettingActivity.class);
                startActivity(intent); // 다음 화면으로 넘어간다
            }
        });

        // Adapter 생성
        mypost_adapter = new mypostAdapter() ;
        mycomment_adapter = new mycommentAdapter() ;

        // 리스트뷰 참조 및 Adapter달기
        mypostlistView = (ListView) findViewById(R.id.MypostListView);
        mypostlistView.setAdapter(mypost_adapter);

        mycommentlistView = (ListView) findViewById(R.id.MycommentListView);
        mycommentlistView.setAdapter(mycomment_adapter);


        new BackgroundTask().execute();
        new commentBackgroundTask().execute();
        //Toast.makeText(getApplicationContext(),"성공",Toast.LENGTH_SHORT).show();
    }

    class BackgroundTask extends AsyncTask<Void, Void, String> {

        String mypost_target;//접속할 홈페이지 주소
        @Override
        protected void onPreExecute(){
            mypost_target = "http://codms1104.dothome.co.kr/mypostList.php?userID=" + midimenu.userID;
        }

        @Override
        protected String doInBackground(Void... voids) {
            try{
                URL url = new URL(mypost_target);
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

        //가져온 데이터를 mypost_item객체에 넣은 뒤 리스트 뷰 출력을 휘한 list객체에 넣어줌줌
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
                    mypost_adapter.addItem(userID, title, postDate, medicine, content, category, postNum);
                    mypost_adapter.notifyDataSetChanged();
                    count++;
                }

            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    class commentBackgroundTask extends AsyncTask<Void, Void, String> {

        String mycomment_target;//접속할 홈페이지 주소
        @Override
        protected void onPreExecute(){
            mycomment_target = "http://codms1104.dothome.co.kr/mycommentList.php?userID=" + midimenu.userID;
        }

        @Override
        protected String doInBackground(Void... voids) {
            try{
                URL url = new URL(mycomment_target);
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
                String commentNum,userID, postNum, comment, commentDate, postuserID, title, postDate, medicine,content,category;
                //json타입의 값을 하나씩 빼서 board_item객체에 저장 후 리스트에 추가
                while(count<jsonArray.length()){
                    JSONObject object = jsonArray.getJSONObject(count);
                    commentNum = object.getString("commentNum");
                    userID = object.getString("userID");
                    postNum = object.getString("postNum");
                    comment = object.getString("comment");
                    commentDate = object.getString("commentDate");
                    postuserID = object.getString("postuserID");
                    title = object.getString("title");
                    postDate = object.getString("postDate");
                    medicine = object.getString("medi_name");
                    content = object.getString("content");
                    category = object.getString("category");

                    //하나의 게시글에 대한 객체생성
                    //board_item boarditem = new board_item(title,medi_name,category);
                    //addItem(boarditem);
                    mycomment_adapter.addItem(commentNum, userID, postNum,comment, commentDate, postuserID, title, postDate, medicine, content, category);
                    mycomment_adapter.notifyDataSetChanged();
                    count++;
                }

            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
