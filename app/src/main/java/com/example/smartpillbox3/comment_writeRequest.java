package com.example.smartpillbox3;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class comment_writeRequest extends StringRequest {
    //서버 url설정(php파일 연동)oard
    final static private String URL="http://codms1104.dothome.co.kr/commentWrite.php";
    private Map<String,String> parameters;
    public comment_writeRequest(String userID, String postNum, String comment, Response.Listener<String> Listener){
        super(Method.POST,URL,Listener, null);

        parameters = new HashMap<>();
        parameters.put("userID",userID);
        parameters.put("postNum",postNum);
        parameters.put("comment",comment);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}
