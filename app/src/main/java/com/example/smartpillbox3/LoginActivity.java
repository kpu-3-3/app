package com.example.smartpillbox3;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    static int check = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        if(check == 0){
            Intent intentLoading = new Intent(this, SplashActivity.class);
            startActivity(intentLoading);
            check++;
        }

        final EditText idText=(EditText)findViewById(R.id.idText);
        final EditText passwordText =(EditText)findViewById(R.id.passwordText);
        final Button loginButton=(Button) findViewById(R.id.loginButton);
        final Button registerButton=(Button) findViewById(R.id.registerButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this,RegisterActivity.class);
                LoginActivity.this.startActivity(registerIntent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String userID = idText.getText().toString();
                final String userPassword = passwordText.getText().toString();

//                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                intent.putExtra("userID",userID);
//                intent.putExtra("userPassword",userPassword);
//               inActivity.this.startActivity(intent); //intent는 화면이동 Log

// 로그인 클릭 시 로그인 오류 안 나면 로그인 정보 주고받기 가능!
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response){
                                    try {
                                        JSONObject jsonResponse = new JSONObject(response);
                                        //회원가입 요청을 한 다음에 결과값을 jsonobject로 받는다.
                                        boolean success = jsonResponse.getBoolean("success"); //성공여부를 담는다.
                                        if(success){    //로그인 성공한 경우
                                            String userID = jsonResponse.getString("userID");
                                            String userPassword = jsonResponse.getString("userPassword");
                                            Intent intent = new Intent(LoginActivity.this, midimenu.class);
                                            intent.putExtra("userID",userID);
                                            intent.putExtra("userPassword",userPassword);
                                           LoginActivity.this.startActivity(intent);
                                            } else{ //로그인 실패한 경우
                                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                                builder.setMessage("로그인에 실패하였습니다.")
                                                        .setNegativeButton("다시시도",null)
                                                        .create()
                                                        .show();
                                                 }

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                    }
                };
                LoginRequest loginRequest = new LoginRequest(userID,userPassword,responseListener);
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                queue.add(loginRequest);
            }
        });
    }
}
