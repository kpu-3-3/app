package com.example.smartpillbox3;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class LoginRequest extends StringRequest {
   //서버 url설정(php파일 연동)
    final static private String URL="http://codms1104.dothome.co.kr/Login.php";
    private Map<String,String> parameters;
    public LoginRequest(String userID, String userPassword, Response.Listener<String> Listener){
            super(Method.POST,URL,Listener, null);

        parameters = new HashMap<>();
        parameters.put("userID",userID);//get값, 실제 넣을 문자열 값
        parameters.put("userPassword",userPassword);
}

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}
