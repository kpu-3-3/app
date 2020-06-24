package com.example.smartpillbox3;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class BluetoothActivity extends AppCompatActivity {
    private static final String TAG = "BluetoothClient";
    public static boolean alive;
    static ProgressDialog pd;

    //블루투스에 필요한 변수 선언
    TextView mTvBluetoothStatus;
    TextView mTvReceiveData;
    TextView mTvSendData;
    TextView mTvConnState;
    TextView mTvDivState;

    Button mBtnBluetoothOn;
    Button mBtnBluetoothOff;
    Button mBtnSearch;
    Button mBtnConnect;
    Button mBtnSendData;
    Button mBtnDisConnect;

    BluetoothAdapter mBluetoothAdapter;
    Set<BluetoothDevice> mPairedDevices;
    List<String> mListPairedDevices;

    Handler mBluetoothHandler;
    BluetoothDevice mBluetoothDevice;
    BluetoothSocket mBluetoothSocket;

    final static int BT_REQUEST_ENABLE = 1;
    final static int BT_MESSAGE_READ = 2;
    final static int BT_CONNECTING_STATUS = 3;
    final static UUID BT_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setTitle("Bluetooth");

        //블루투스
        mTvBluetoothStatus = (TextView)findViewById(R.id.tvBluetoothStatus);
        mTvReceiveData = (TextView)findViewById(R.id.tvReceiveData);
        mTvSendData =  (EditText) findViewById(R.id.tvSendData);
        mTvConnState = (TextView) findViewById(R.id.txtConnState);
        mTvDivState = (TextView) findViewById(R.id.txtDivState);
        mBtnBluetoothOn = (Button)findViewById(R.id.btnBluetoothOn);
        mBtnBluetoothOff = (Button)findViewById(R.id.btnBluetoothOff);
        mBtnSearch=(Button)findViewById(R.id.btnSearch);
        mBtnConnect = (Button)findViewById(R.id.btnConnect);
        mBtnSendData = (Button)findViewById(R.id.btnSendData);
        mBtnDisConnect =(Button)findViewById(R.id.btnDisConnect);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();


        mBtnBluetoothOn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                bluetoothOn();
            }
        });
        mBtnBluetoothOff.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                bluetoothOff();
            }
        });
        mBtnSearch.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1= new Intent(BluetoothActivity.this, SearchPairing.class);
                startActivity(intent1);
            }
        });
        mBtnConnect.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                listPairedDevices();
            }
        });
        mBtnSendData.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* if(mThreadConnectedBluetooth != null) {
                    mThreadConnectedBluetooth.write(mTvSendData.getText().toString());
                    mTvSendData.setText("");
                }
                */
            }
        });
        mBtnDisConnect.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopServices();
            }
        });

    }
    //블루투스 연결
    void bluetoothOn() {
        if(mBluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(), "블루투스를 지원하지 않는 기기입니다.", Toast.LENGTH_LONG).show();
        }
        else {
            //현재 블루투스가 활성화 되었는지 확인한다
            if (mBluetoothAdapter.isEnabled()) {
                Toast.makeText(getApplicationContext(), "블루투스가 이미 활성화 되어 있습니다.", Toast.LENGTH_LONG).show();
                //mTvBluetoothStatus.setText("활성화");
                mTvConnState.setText("Connect");
            }
            else {
                Toast.makeText(getApplicationContext(), "블루투스가 활성화 되어 있지 않습니다.", Toast.LENGTH_LONG).show();
                mTvConnState.setText("DISConnect");
                Intent intentBluetoothEnable = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(intentBluetoothEnable, BT_REQUEST_ENABLE);
            }
        }
    }
    //블루투스 연결해제
    void bluetoothOff() {
        if (mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.disable();
            Toast.makeText(getApplicationContext(), "블루투스가 비활성화 되었습니다.", Toast.LENGTH_SHORT).show();
            //mTvBluetoothStatus.setText("비활성화");
            mTvConnState.setText("DISConnect");
        }
        else {
            Toast.makeText(getApplicationContext(), "블루투스가 이미 비활성화 되어 있습니다.", Toast.LENGTH_SHORT).show();
            mTvConnState.setText("DISConnect");
        }
    }
    //응답 처리 메소드 재정의
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case BT_REQUEST_ENABLE:
                if (resultCode == RESULT_OK) { // 블루투스 활성화를 확인을 클릭하였다면
                    Toast.makeText(getApplicationContext(), "블루투스 활성화", Toast.LENGTH_LONG).show();
                    //mTvBluetoothStatus.setText("활성화");
                    mTvConnState.setText("Connect");
                } else if (resultCode == RESULT_CANCELED) { // 블루투스 활성화를 취소를 클릭하였다면
                    Toast.makeText(getApplicationContext(), "취소", Toast.LENGTH_LONG).show();
                    //mTvBluetoothStatus.setText("비활성화");
                    mTvConnState.setText("DISConnect");
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //블루투스 페어링 장치 목록 가져오는 메소드
    void listPairedDevices() {
        startServices();
    }

    //백그라운드 작업인 클라이언트서비스와 알람체크서비스를 시작하는 함수
    private void startServices() {
        Log.d(TAG, "starting ClientService and AlarmCheckService.");
        //알람 시간에 맞춰서 불을 켜도록하기 위해 알람이 울리는지 감시하는 서비스 시작
        startService(new Intent(getApplicationContext(), AlarmCheckService.class));
        if (!BluetoothService.connectedToServer) //서버와 연결, 통신 작업을 하는 서비스 시작
            startService(new Intent(getApplicationContext(), BluetoothService.class));
    }

    //백그라운드 작업인 클라이언트서비스와 알람체크서비스를 종료시키는 함수
    private void stopServices() {
        Log.d(TAG, "stopping ClientService and AlarmCheckService.");
        BluetoothService.isConnectionError = false;
        BluetoothService.connectedToServer = false;
        stopService(new Intent(getApplicationContext(), BluetoothService.class));
        stopService(new Intent(getApplicationContext(), AlarmCheckService.class));
    }


    //다른 클래스로부터 메시지를 받는 브로드캐스트리시버
    private class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action == null) return;
            if (action.equals(Actions.TO_MAIN_ACTIVITY)) {
                String msgs[] = intent.getStringArrayExtra("msg");
                if (msgs != null) {
                    switch (msgs[0]) {
                        case "DivSetTextView": //디바이스 연결시, 이름을 텍스트뷰에 설정하는 경우
                            String text = msgs[1];
                            mTvDivState.setText(text);
                            break;
                        case "setTextView": //디바이스 연결시, 이름을 텍스트뷰에 설정하는 경우
                            break;
                        case "clickButton": //버튼을 클릭하라는 명령을 받을경우
                            break;
                        case "showQuitDialog": //다이얼로그를 띄우라는 명령을 받을경우
                            break;
                        case "setEnableButton": //버튼 활성화 명령을 받을 경우
                            break;

                    }
                }
            }
        }
    }

    //다른 클래스의 브로드캐스트 리시버로 메시지를 보내는 함수
    private void sendMsgToOther(String action, String... msg) {
        Intent intent = new Intent();
        intent.setAction(action);
        intent.putExtra("msg", msg);
        sendBroadcast(intent);
    }

}