package com.example.smartpillbox3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class midimenu extends AppCompatActivity {
    public static String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        ImageButton boardImageButton=(ImageButton) findViewById(R.id.boardImageButton);
        ImageButton takingImageButton=(ImageButton) findViewById(R.id.takingImageButton);
        ImageButton blueImageButton=(ImageButton) findViewById(R.id.blueImageButton);
        ImageButton mypageImageButton=(ImageButton) findViewById(R.id.mypageImageButton);
        ImageButton searchImageButton=(ImageButton) findViewById(R.id.searchImageButton);
        Intent intent =getIntent();
        userID = intent.getStringExtra("userID");

        boardImageButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), board.class);
                startActivity(intent); // 다음 화면으로 넘어간다
            }
        });

        takingImageButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TakingMedicineInfo.class);
                startActivity(intent); // 다음 화면으로 넘어간다
            }
        });

        blueImageButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent); // 다음 화면으로 넘어간다
            }
        });


        mypageImageButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MypageMainActivity.class);
                startActivity(intent); // 다음 화면으로 넘어간다
            }
        });


        searchImageButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MedicineApiActivity.class);
                startActivity(intent); // 다음 화면으로 넘어간다
            }
        });
    }
}
