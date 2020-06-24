package com.example.smartpillbox3;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import java.util.HashMap;
import java.util.Map;

public class mediUpdateRequest extends StringRequest{
    final static private String URL="http://codms1104.dothome.co.kr/updatetakinginfo.php";
    private Map<String,String> parameters;
    public mediUpdateRequest(String userID, String a_time, String b_time, String Date, Response.Listener<String> listener){
        super(Request.Method.POST,URL,listener,null);
        parameters = new HashMap<>();//해쉬맵 생성후 parameters 변수에 값을 넣어줌
        parameters.put("userID", userID);
        parameters.put("a_time", a_time);
        parameters.put("b_time", b_time);
        parameters.put("Date", Date);
    }
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}


