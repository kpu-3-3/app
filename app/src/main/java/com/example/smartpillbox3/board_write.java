package com.example.smartpillbox3;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class board_write extends AppCompatActivity {

    Integer available=0;
    EditText content;

    private Spinner spinner;
    private ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_write);
        spinner=(Spinner) findViewById(R.id.writeSpinner);
        adapter = ArrayAdapter.createFromResource(this,R.array.category, android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        final EditText contentText=findViewById(R.id.contentText);
        final EditText titleText=findViewById(R.id.titleText);
        final EditText medicineText=findViewById(R.id.medicineText);

        Button postButton=findViewById(R.id.postButton);
        //회원가입 버튼 클릭 시 수행
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //EditText에 현재 입력되어있는 값을 가져온다.
                 //사용자가 입력한 값을 문자열 형태로 가져온다
                String userID= midimenu.userID;
                String content=contentText.getText().toString();
                String title=titleText.getText().toString();
                String medi_name=medicineText.getText().toString();
                String category=spinner.getSelectedItem().toString();
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
                                AlertDialog.Builder builder = new AlertDialog.Builder(board_write.this);
                                builder.setMessage("게시글을 등록합니다")
                                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent = new Intent(board_write.this, board.class);
                                                board_write.this.startActivity(intent);
                                                finish();
                                            }
                                        });
                                        builder.create();
                                        builder.show();

                            } else{ //글쓰기에 실패한 경우
                                AlertDialog.Builder builder = new AlertDialog.Builder(board_write.this);
                                builder.setMessage("게시글 등록에 실패했습니다")
                                        .setNegativeButton("다시시도",null)
                                        .create()
                                        .show();
                            }
                        } catch (JSONException e) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(board_write.this);
                            builder.setMessage("게시글을 등록합니다")
                                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(board_write.this, board.class);
                                            board_write.this.startActivity(intent);
                                            finish();
                                        }
                                    });
                            builder.create();
                            builder.show();
                            //Toast.makeText(getApplicationContext(),"실패", Toast.LENGTH_SHORT).show();
                            //e.printStackTrace();
                        }

                    }
                };

                //서버로 Volley를 이용해서 요청을 함
                //RequestObject를 생성한다. 이 때 서버로부터 데이터를 받을 responseListener을 넘겨준다
                board_writeRequest boardwriteRequest = new board_writeRequest(userID, title, medi_name, content, category, responseListener);
                //RequestQueue를 생성한다.
                RequestQueue queue = Volley.newRequestQueue(board_write.this);
                //RequestQueue에 RequestObject를 넘겨준다.
                queue.add(boardwriteRequest);
            }
        });

}

}
