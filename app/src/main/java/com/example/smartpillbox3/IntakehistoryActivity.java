package com.example.smartpillbox3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class IntakehistoryActivity extends AppCompatActivity {

    intakehistoryAdapter adapter;
    ListView listView;
    //    private ListView boardListView;
////    private postListAdapter adapter;
    private List<usermedi_item> intakehistoryList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intakehistory);

        // Adapter 생성
        adapter = new intakehistoryAdapter() ;

        // 리스트뷰 참조 및 Adapter달기
        listView = (ListView) findViewById(R.id.intakehistoryListView);
        listView.setAdapter(adapter);


        new BackgroundTask().execute();
        //Toast.makeText(getApplicationContext(),"성공",Toast.LENGTH_SHORT).show();
    }

    class BackgroundTask extends AsyncTask<Void, Void, String> {

        String target;//접속할 홈페이지 주소
        @Override
        protected void onPreExecute(){
            target = "http://codms1104.dothome.co.kr/medisettingList.php?userID=" + midimenu.userID;

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

        //가져온 데이터를 medisetting_item객체에 넣은 뒤 리스트 뷰 출력을 휘한 list객체에 넣어줌줌
        @Override
        public void onPostExecute(String result){
            try{
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                int count=0;
                String userID, type,a_time, b_time,medi_name,take,Date,medi_type;
                //json타입의 값을 하나씩 빼서 board_item객체에 저장 후 리스트에 추가
                while(count<jsonArray.length()){
                    JSONObject object = jsonArray.getJSONObject(count);
                    userID = object.getString("userID");
                    type = object.getString("type");
                    a_time = object.getString("a_time");
                    b_time = object.getString("b_time");
                    medi_name = object.getString("medi_name");
                    take = object.getString("take");
                    Date = object.getString("Date");
                    medi_type = object.getString("medi_type");

                    //하나의 게시글에 대한 객체생성
                    //board_item boarditem = new board_item(title,medi_name,category);
                    //addItem(boarditem);
                    if(!(a_time.equals("null"))){
                        adapter.addItem(userID, type, a_time, b_time, medi_name, take, Date, medi_type);
                        adapter.notifyDataSetChanged();
                        count++;

                    } else{count++;}

                }

            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
