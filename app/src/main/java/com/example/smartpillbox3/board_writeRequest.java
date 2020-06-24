package com.example.smartpillbox3;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class board_writeRequest extends StringRequest {
    //서버 url설정(php파일 연동)oard
    final static private String URL="http://codms1104.dothome.co.kr/boardWrite.php";
    private Map<String,String> parameters;
    public board_writeRequest(String userID, String title, String medi_name, String content, String category, Response.Listener<String> Listener){
        super(Method.POST,URL,Listener, null);

        parameters = new HashMap<>();
        parameters.put("userID",userID);
        parameters.put("title",title);
        parameters.put("medi_name",medi_name);
        parameters.put("content",content);
        parameters.put("category",category);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}
