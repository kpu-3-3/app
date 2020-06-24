package com.example.smartpillbox3;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class PostActivity extends AppCompatActivity {
    private int pos;
    commentAdapter adapter;
    ListView listView;
    private String postNum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        TextView titleText = (TextView)findViewById(R.id.titleText);
        TextView categoryText = (TextView)findViewById(R.id.categoryText);
        TextView medicineText = (TextView)findViewById(R.id.medicineText);
        TextView dateText = (TextView)findViewById(R.id.dateText);
        TextView contentText = (TextView)findViewById(R.id.contentText);
        TextView userIDText = (TextView)findViewById(R.id.userIDText);
        TextView postNumText=(TextView)findViewById(R.id.postNumText);

        Intent intent = getIntent(); // 보내온 Intent를 얻는다
        pos=intent.getIntExtra("pos",0);
        //postNumText.setText(intent.getStringExtra("postNum"));
        postNum =intent.getStringExtra("postNum");
        titleText.setText(intent.getStringExtra("title"));
        dateText.setText(intent.getStringExtra("postDate"));
        categoryText.setText(intent.getStringExtra("category"));
        medicineText.setText(intent.getStringExtra("medicine"));
        contentText.setText(intent.getStringExtra("content"));
        userIDText.setText(intent.getStringExtra("userID"));


        final EditText commentText=findViewById(R.id.commentText);



        // Adapter 생성
        adapter = new commentAdapter() ;

        // 리스트뷰 참조 및 Adapter달기
        listView = (ListView) findViewById(R.id.commentListView);
        listView.setAdapter(adapter);


        new BackgroundTask().execute();
        //Toast.makeText(getApplicationContext(),"성공",Toast.LENGTH_SHORT).show();

        Button commentwritebutton=findViewById(R.id.commentwritebutton);
        //댓글 올리기 버튼 클릭 시 수행
        commentwritebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //EditText에 현재 입력되어있는 값을 가져온다.
                //사용자가 입력한 값을 문자열 형태로 가져온다
                String userID= midimenu.userID;
                String comment=commentText.getText().toString();
                //콜백 처리 부분(volley 사용을 위한 ResponseListener 구현 부분)

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    //json
                    @Override
                    public void onResponse(String response) {
                        try {
                            //글쓰기 요청을 한 다음에 결과값을 jsonObject로 받는다.
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success"); //성공여부를 담는다.
                            if(success){    //업로드 성공한 경우 success값이 true
                                AlertDialog.Builder builder = new AlertDialog.Builder(PostActivity.this);
                                builder.setMessage("댓글을 등록합니다")
                                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                finish();
                                                startActivity(getIntent());
                                                adapter.notifyDataSetChanged();
                                                commentText.setText("");
                                            }
                                        });
                                builder.create();
                                builder.show();

                            } else{ //글쓰기에 실패한 경우
                                AlertDialog.Builder builder = new AlertDialog.Builder(PostActivity.this);
                                builder.setMessage("댓글 등록에 실패했습니다")
                                        .setNegativeButton("다시시도",null)
                                        .create()
                                        .show();
                            }
                        } catch (JSONException e) {
                            //Toast.makeText(getApplicationContext(),"실패", Toast.LENGTH_SHORT).show();
                            //e.printStackTrace();
                            AlertDialog.Builder builder = new AlertDialog.Builder(PostActivity.this);
                            builder.setMessage("댓글을 등록합니다")
                                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            finish();
                                            startActivity(getIntent());
                                            adapter.notifyDataSetChanged();
                                            commentText.setText("");
                                        }
                                    });
                            builder.create();
                            builder.show();
                        }

                    }
                };

                //서버로 Volley를 이용해서 요청을 함
                //RequestObject를 생성한다. 이 때 서버로부터 데이터를 받을 responseListener을 넘겨준다
                comment_writeRequest commentwriteRequest = new comment_writeRequest(userID, postNum, comment, responseListener);
                //RequestQueue를 생성한다.
                RequestQueue queue = Volley.newRequestQueue(PostActivity.this);
                //RequestQueue에 RequestObject를 넘겨준다.
                queue.add(commentwriteRequest);
            }
        });
    }

    class BackgroundTask extends AsyncTask<Void, Void, String> {

        String target;//접속할 홈페이지 주소
        @Override
        protected void onPreExecute(){
            target = "http://codms1104.dothome.co.kr/commentList.php?postNum=" + postNum;

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

        //가져온 데이터를 comment_item객체에 넣은 뒤 리스트 뷰 출력을 휘한 list객체에 넣어줌
        @Override
        public void onPostExecute(String result){
            try{
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                int count=0;
                String userID, commentNum,postNum, comment,commentDate;
                //json타입의 값을 하나씩 빼서 board_item객체에 저장 후 리스트에 추가
                while(count<jsonArray.length()){
                    JSONObject object = jsonArray.getJSONObject(count);
                    commentNum = object.getString("commentNum");
                    userID = object.getString("userID");
                    postNum = object.getString("postNum");
                    comment = object.getString("comment");
                    commentDate = object.getString("commentDate");


                    //하나의 게시글에 대한 객체생성
                    adapter.addItem(commentNum, userID, postNum, comment, commentDate);
                    adapter.notifyDataSetChanged();
                    count++;
                }

            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
