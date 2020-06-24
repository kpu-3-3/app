package com.example.smartpillbox3;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class TakingRequest extends StringRequest {
    //서버 url설정(php파일 연동)
    //수정필요
    final static private String URL="http://codms1104.dothome.co.kr/takinginfo.php";
    private Map<String,String> parameters;
    //생성자
    public TakingRequest(String userID, String type, String a_time, String b_time, String medi_name, String take, String Date, String medi_type, Response.Listener<String> Listener){
        super(Method.POST,URL,Listener, null);

        parameters = new HashMap<>();
        parameters.put("userID",userID);//get값, 실제 넣을 문자열 값
        parameters.put("type",type);
        parameters.put("a_time",a_time);
        parameters.put("b_time",b_time);
        parameters.put("medi_name",medi_name);
        parameters.put("take",take);
        parameters.put("Date", Date);
        parameters.put("medi_type", medi_type);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}