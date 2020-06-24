package com.example.smartpillbox3;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import java.util.HashMap;
import java.util.Map;

public class mycommentDeleteRequest extends StringRequest{
    final static private String URL="http://codms1104.dothome.co.kr/mycommentdelete.php";
    private Map<String,String> parameters;
    public mycommentDeleteRequest(String commentNum, Response.Listener<String> listener){
        super(Request.Method.POST,URL,listener,null);
        parameters = new HashMap<>();//해쉬맵 생성후 parameters 변수에 값을 넣어줌
        parameters.put("commentNum",commentNum);

    }
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}


