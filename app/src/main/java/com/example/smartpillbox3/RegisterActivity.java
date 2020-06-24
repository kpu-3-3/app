package com.example.smartpillbox3;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {
    private String userGender;
    @Override
    protected void onCreate(Bundle savedInstanceState) {    //액티비티 시작시 처음으로 실행되는 생명주기
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText idText=findViewById(R.id.idText);
        final EditText passwordText =findViewById(R.id.passwordText);
        final EditText nameText=findViewById(R.id.nameText);
        final EditText ageText=findViewById(R.id.ageText);
        final EditText emailText=findViewById(R.id.emailText);

        RadioGroup genderGroup = (RadioGroup) findViewById(R.id.genderGroup);
        int genderGroupID = genderGroup.getCheckedRadioButtonId();//남자인지 여자인지 확인할 수 있도록 id값 부여
        userGender = ((RadioButton)findViewById(genderGroupID)).getText().toString();

        genderGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton genderButton = (RadioButton) findViewById(i);
                userGender = genderButton.getText().toString();
            }
        });
        Button registerButton=findViewById(R.id.registerButton);

        //회원가입 버튼 클릭 시 수행
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //EditText에 현재 입력되어있는 값을 가져온다.
                String userID=idText.getText().toString(); //사용자가 입력한 값을 문자열 형태로 가져온다
                String userPassword=passwordText.getText().toString();
                String userName=nameText.getText().toString();
                int userAge=Integer.parseInt(ageText.getText().toString());
                String userEmail=emailText.getText().toString();
                //콜백 처리 부분(volley 사용을 위한 ResponseListener 구현 부분)

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    //json
                    @Override
                    public void onResponse(String response) {
                        try {
                                //회원가입 요청을 한 다음에 결과값을 jsonObject로 받는다.
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success"); //성공여부를 담는다.
                                if(success){    //회원등록에 성공한 경우 success값이 true
                                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                    builder.setMessage("회원 등록에 성공했습니다")
                                            .setPositiveButton("확인",null)
                                            .create()
                                            .show();
                                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                    intent.putExtra("check",true);
                                    RegisterActivity.this.startActivity(intent);
                            } else{ //회원등록에 실패한 경우
                                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                    builder.setMessage("회원 등록에 실패했습니다")
                                            .setNegativeButton("다시시도",null)
                                            .create()
                                            .show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(),"실패", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }

                    }
                };
                //서버로 Volley를 이용해서 요청을 함
                //RequestObject를 생성한다. 이 때 서버로부터 데이터를 받을 responseListener을 넘겨준다
                RegisterRequest registerRequest = new RegisterRequest(userID, userPassword,userName, userAge, userEmail, userGender, responseListener);
                //RequestQueue를 생성한다.
                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                //RequestQueue에 RequestObject를 넘겨준다.
                queue.add(registerRequest);
            }
        });

    }
}
