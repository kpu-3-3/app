package com.example.smartpillbox3;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class TakingMedicineInfo extends AppCompatActivity {
    Button btnType1;
    Button btnType2;
    Button btnType3;
    Button btnType4;
    Button btnType5;
    //날짜,시간설정버튼
    Button btnTime;
    Button btnDate;
    //type 설정 저장버튼
    Button save_taking;

    TextView txtSetDate;
    TextView txtSetTime;

    Spinner PillName;
    Spinner TakingCount;

    EditText editPilName;

    final int DIALOG_DATE=1;
    final int DIALOG_TIME=2;
    String Type_Name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taking_medicine_info);
        btnType1=(Button)findViewById(R.id.type1);
        btnType2=(Button)findViewById(R.id.type2);
        btnType3=(Button)findViewById(R.id.type3);
        btnType4=(Button)findViewById(R.id.type4);
        btnType5=(Button)findViewById(R.id.type5);

        btnDate=(Button)findViewById(R.id.btndate);
        btnTime=(Button)findViewById(R.id.btntime);

        save_taking=(Button)findViewById(R.id.save_taking);

        txtSetDate=(TextView)findViewById(R.id.txtSetDate);
        txtSetTime=(TextView)findViewById(R.id.txtSetTime);

        editPilName=(EditText)findViewById(R.id.PilName);

        PillName=(Spinner)findViewById(R.id.spin_pillName);
        TakingCount=(Spinner)findViewById(R.id.spin_TakingCount);

        final String[] spinnerPillName = new String[1];
        final String[] taking = new String[1];

        //스피너 구현-어뎁터에 붙인다
        //1. 복용 약 종류 스피너
        ArrayAdapter<CharSequence> spinneradapter=ArrayAdapter.createFromResource(this,R.array.pill_array,android.R.layout.simple_spinner_item);
        spinneradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        PillName.setAdapter(spinneradapter);
        PillName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
               // Toast.makeText(adapterView.getContext(),adapterView.getItemAtPosition(pos).toString(),Toast.LENGTH_SHORT).show();
                spinnerPillName[0] =adapterView.getItemAtPosition(pos).toString();

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        //2.복용개수 스피너
        ArrayAdapter<CharSequence> spinneradapter2=ArrayAdapter.createFromResource(this,R.array.Taking_array,android.R.layout.simple_spinner_item);
        spinneradapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        TakingCount.setAdapter(spinneradapter2);
        TakingCount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView,  View view, int pos, long id) {
               // Toast.makeText(getApplicationContext(),adapterView.getItemAtPosition(pos).toString(),Toast.LENGTH_SHORT).show();
                taking[0]=adapterView.getItemAtPosition(pos).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //Type 별 이벤트 리스너
        btnType1.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Type_Name="Type1";
            }
        });

        btnType2.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Type_Name="Type2";
            }
        });

        btnType3.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Type_Name="Type3";
            }
        });

        btnType4.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Type_Name="Type4";
            }
        });

        btnType5.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Type_Name="Type5";
            }
        });
        btnDate.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(DIALOG_DATE);

            }
        });
        btnTime.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(DIALOG_TIME);
            }
        });
        //복용저장->서버,DB에 저장하는 button
        save_taking.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                //아이디 중복되면 디비에 안들어감!
                String userID= midimenu.userID;
                String type=Type_Name;
                String a_time="";
                String b_time=txtSetTime.getText().toString();
                String medi_name= editPilName.getText().toString();
                String take= taking[0];
                String Date=txtSetDate.getText().toString();
                String medi_type=spinnerPillName[0];

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response){
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            //요청 결과값을 jsonobject로 받는다.
                            boolean success = jsonResponse.getBoolean("success"); //성공여부를 담는다.
                            if(success){    //서버 접속 성공한 경우
                                Toast.makeText(getApplicationContext(), " 저장에 성공했습니다.", Toast.LENGTH_SHORT).show();
                                String userID = jsonResponse.getString("userID");
                                String type=jsonResponse.getString("type");
                                String a_time=jsonResponse.getString("a_time");
                                String b_time=jsonResponse.getString("b_time");
                                String medi_name=jsonResponse.getString("medi_name");
                                String take=jsonResponse.getString("take");
                                String Date=jsonResponse.getString("Date");
                                String medi_type=jsonResponse.getString("medi_type");

                                Intent intent = new Intent(TakingMedicineInfo.this, MainActivity.class);
                                intent.putExtra("userID",userID);
                                intent.putExtra("type",type);
                                intent.putExtra("userID",userID);
                                intent.putExtra("a_time",a_time);
                                intent.putExtra("b_time",b_time);
                                intent.putExtra("medi_name",medi_name);
                                intent.putExtra("take",take);
                                intent.putExtra("Date",Date);
                                intent.putExtra("medi_type",medi_type);
                                TakingMedicineInfo.this.startActivity(intent);
                            } else{ //로그인 실패한 경우
                                AlertDialog.Builder builder = new AlertDialog.Builder(TakingMedicineInfo.this);
                                builder.setMessage("저장에 실패하였습니다.")
                                        .setNegativeButton("다시시도",null)
                                        .create()
                                        .show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                };
                TakingRequest takingRequest = new TakingRequest(userID,type,a_time,b_time,medi_name,take,Date,medi_type,responseListener);
                RequestQueue queue = Volley.newRequestQueue(TakingMedicineInfo.this);
                queue.add(takingRequest);
            }
        });

    }
    //DATE, TIME 구현
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    @Deprecated
    protected Dialog onCreateDialog(int id){
        switch(id){
            case DIALOG_DATE:
                DatePickerDialog dpd=new DatePickerDialog(TakingMedicineInfo.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        txtSetDate.setText(year+"년 "+(month+1)+"월 "+dayOfMonth+"일");
                    }
                },2020,5,6);
                return dpd;
            case DIALOG_TIME:
                TimePickerDialog tpd=new TimePickerDialog(TakingMedicineInfo.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        txtSetTime.setText(hourOfDay+"시 "+minute+"분");
                    }
                },4,19,false);
                return tpd;
        }
        return  super.onCreateDialog(id);
    }
}
