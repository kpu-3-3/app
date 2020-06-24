package com.example.smartpillbox3;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

//RequestObject생성
public class RegisterRequest extends StringRequest {
   //서버 url설정(php파일 연동)
    final static private String URL="http://codms1104.dothome.co.kr/Register.php";
    private Map<String,String> parameters;
    //생성자
    public RegisterRequest(String userID, String userPassword, String userName, int userAge, String userEmail, String userGender, Response.Listener<String> Listener){
        super(Method.POST,URL,Listener, null);

        parameters = new HashMap<>();
        parameters.put("userID",userID);//get값, 실제 넣을 문자열 값
        parameters.put("userPassword",userPassword);
        parameters.put("userName",userName);
        parameters.put("userAge",userAge+"");  //int형이기 때문에 문자열 형태변환
        parameters.put("userEmail",userEmail);
        parameters.put("userGender",userGender);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}
