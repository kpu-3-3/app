package com.example.smartpillbox3;

import android.app.ProgressDialog;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class BluetoothService extends Service {
    private static final String TAG = "BluetoothClient";
    private ConnectedTask mConnectedTask = null; //서버와 클라이언트가 메시지를 주고받는 작업
    private BluetoothDevice mPairedDevice = null; //연결된 서버 기기 정보
    private BluetoothDevice mPairedDevices[]=null; //페어링된 기기들 정보
    private int deviceIdx = 0;
    private ConnectTask mConnectTask = null; //클라이언트가 서버에 연결하는 작업
    private String mConnectedDeviceName; //연결된 서버 기기의 이름
    static boolean isConnectionError; //연결 오류 여부
    static boolean connectedToServer; //서버와 연결 여부
    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private BluetoothSocket mBluetoothSocket = null; //블루투스 소켓
    private InputStream mInputStream = null; //서버와 연결된 입력 스트림
    private OutputStream mOutputStream = null; //서버와 연결된 출력 스트림
    private BroadcastReceiver msgReceiver; //다른 클래스에서 온 메시지를 처리하는 브로드캐스트 리시버

    public BluetoothService() {
    }
    @Override
    public void onCreate() {
        Log.i(TAG, "onCreate");
        msgReceiver = new MessageReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Actions.TO_CLIENT_SERVICE); //인텐트 필터에 액션 등록
        registerReceiver(msgReceiver, intentFilter); //리시버 등록
    }
    //서비스가 시작될 때 호출
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //현재 페어링되어있는 블루투스들 중 스마트 스위치를 찾아서 연결
        Log.i(TAG, "onStartCommand");
        Log.i(TAG, " mPairedDevice:" + mPairedDevice);
        if (mPairedDevice == null||!connectedToServer) {
            findSmartSwitchFromPairedDevices();
        }
        return START_STICKY_COMPATIBILITY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    //현재 페어링되어있는 블루투스들 중 스마트 약통를 찾아서 연결
    public void findSmartSwitchFromPairedDevices()
    {
        Toast.makeText(getApplicationContext(), "스마트 약통에 연결 시도중...", Toast.LENGTH_LONG).show();
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter(); //블루투스 어댑터 초기화
        Set<BluetoothDevice> devices = bluetoothAdapter.getBondedDevices(); //페어링된 기기들의 목록
        final BluetoothDevice[] pairedDevices = devices.toArray(new BluetoothDevice[0]);
        if (pairedDevices.length == 0) //페어링된 기기가 없을 경우
        {
            if (BluetoothActivity.alive)
            {
                //종료 다이얼로그(메시지 박스) 뛰움
                sendMsgToOther(Actions.TO_MAIN_ACTIVITY, "showQuitDialog", "스마트 약통을 찾을 수 없습니다.");
                sendMsgToOther(Actions.TO_STAT_ACTIVITY, "finish");
            }
            stopSelf(); //자신의 서비스 종료
            return;
        }
        //앱 저장소에서 가장 마지막에 연결 성공한 기기 이름을 가져옴
        SharedPreferences pref = getSharedPreferences("pref",MODE_PRIVATE);
        String lastDeviceName= pref.getString("lastDeviceName", "");
        mPairedDevices = pairedDevices;
        if(lastDeviceName.equals("")){ //저장된 기기 이름이 없으면
            Log.d(TAG, "find device name : " + mPairedDevices[deviceIdx].getName());
            sendMsgToOther(Actions.TO_MAIN_ACTIVITY, "setTextView", R.id.txtConnState + "", "연결 상태: "+mPairedDevices[deviceIdx].getName()+"이 스마트 약통인지 확인 중...");
            mConnectTask = new ConnectTask(mPairedDevices[deviceIdx++]); //페어링되어있는 기기들 중 첫번째 기기에 연결 시도
            mConnectTask.execute();
        }
        else{ //저장된 기기 이름이 있다면
            //페어링된 기기들 중 저장된 기기 이름과 같은 것을 찾아 연결
            for(BluetoothDevice device : mPairedDevices){
                if(device.getName().equals(lastDeviceName)){
                    mConnectTask = new ConnectTask(device);
                    mConnectTask.execute();
                    break;
                }
            }
        }
    }

    //서버에 클라이언트 연결을 수행
    private class ConnectTask extends AsyncTask<Void, Void, Boolean> {
        ConnectTask(BluetoothDevice bluetoothDevice) {
            mConnectedDeviceName = bluetoothDevice.getName();
            //SPP 서버 고유 id 생성
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            //블루투스 소켓 생성
            try {
                mBluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(uuid);
                Log.d(TAG, "create socket for " + mConnectedDeviceName);
            } catch (IOException e) {
                Log.e(TAG, "socket create failed " + e.getMessage());
            }
        }
        @Override
        protected Boolean doInBackground(Void... params) {

            if (bluetoothAdapter != null)
                bluetoothAdapter.cancelDiscovery(); //스마트폰의 블루투스 탐색 중지
            try {
                mBluetoothSocket.connect();//블루투스 서버 소켓에 연결
            } catch (IOException e) {
                try {
                    mBluetoothSocket.close();
                    connectedToServer = false;
                } catch (IOException e2) {
                    Log.e(TAG, "unable to close() " +
                            " socket during connection failure", e2);
                }
                return false;
            }
            return true;
        }
        //연결 시도가 성공했는지 실패했는지에 따라 작업이 달라짐
        @Override
        protected void onPostExecute(Boolean isSucess) {
            if (isSucess) { //연결 성공
                Toast.makeText(getApplicationContext(), "스마트 약통에 연결 성공!", Toast.LENGTH_LONG).show();
                connected(mBluetoothSocket); //서버와 메시지 통신 작업 시작
                //가장 마지막으로 연결 성공한 기기 이름을 저장소에 등록
                SharedPreferences pref = getSharedPreferences("pref",MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("lastDeviceName", mConnectedDeviceName);
                editor.apply();
                if (BluetoothActivity.alive) {
                    sendMsgToOther(Actions.TO_MAIN_ACTIVITY, "DivSetTextView",mConnectedDeviceName,R.id.txtConnState + "", "연결 상태: 연결 안됨");
                }
            } else { //연결 실패
                isConnectionError = true;
                Toast.makeText(getApplicationContext(), "스마트 약통에 연결 실패", Toast.LENGTH_LONG).show();
                Log.d(TAG, "Unable to connect device");
                //가장 마지막으로 연결 성공한 기기 이름 삭제
                SharedPreferences pref = getSharedPreferences("pref",MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("lastDeviceName", "");
                editor.apply();
                if (BluetoothActivity.alive)
                {
                    //페어링되어있는 모든 기기에 연결해보았으나 연결에 실패했을 경우
                    if(mPairedDevices==null || deviceIdx>mPairedDevices.length-1)
                    {
                        sendMsgToOther(Actions.TO_MAIN_ACTIVITY, "showQuitDialog", "스마트 약통을 찾을수 없습니다.");
                        sendMsgToOther(Actions.TO_STAT_ACTIVITY, "finish");
                    }
                    //페어링되어 있는 기기중 다음 목록에 있는 기기로 다시 연결 시도
                    else {
                        Log.d(TAG, "find device name : " + mPairedDevices[deviceIdx].getName());
                        sendMsgToOther(Actions.TO_MAIN_ACTIVITY, "setTextView", R.id.txtConnState + "", "연결 상태: "+mPairedDevices[deviceIdx].getName()+"이 스마트 약통인지 확인 중...");
                        mConnectTask = new ConnectTask(mPairedDevices[deviceIdx++]);
                        mConnectTask.execute();
                    }
                }
            }
        }
    }
    //서버와 연결되면 서로 메시지 통신을 시작함
    public void connected(BluetoothSocket socket) {
        mConnectedTask = new ConnectedTask(socket);
        mConnectedTask.execute();
        connectedToServer = true;
        Log.d(TAG, "connected to " + mConnectedDeviceName);
    }

    //서버와 연결된 상태에서 서로 메시지 통신하는 작업
    private class ConnectedTask extends AsyncTask<Void, String, Boolean> {
        ConnectedTask(BluetoothSocket socket) {
            Toast.makeText(getApplicationContext(), "스마트 약통 메세지 통신 시작", Toast.LENGTH_LONG).show();
            mBluetoothSocket = socket;
            try {
                if (BluetoothActivity.alive) {
                    sendMsgToOther(Actions.TO_MAIN_ACTIVITY, "setTextView", R.id.txtConnState + "", "연결 상태: "+mConnectedDeviceName+"에 연결중...");
                }
                //블루투스 소켓에서 입력, 출력 스트림 생성
                mInputStream = mBluetoothSocket.getInputStream();
                mOutputStream = mBluetoothSocket.getOutputStream();
            } catch (IOException e) {
                Log.e(TAG, "socket not created", e);
            }
        }
        @Override
        protected Boolean doInBackground(Void... params) {
            byte[] readBuffer = new byte[1024];
            int readBufferPosition = 0;
            //서버에서 메시지가 오는 걸 계속 받는 작업
            while (true) {
                if (isCancelled()) return false;
                try {
                    int bytesAvailable = mInputStream.available();
                    if (bytesAvailable > 0)
                    {
                        byte[] packetBytes = new byte[bytesAvailable];
                        //서버에서 온 패킷을 읽음
                        mInputStream.read(packetBytes);
                        for (int i = 0; i < bytesAvailable; i++) {
                            byte b = packetBytes[i]; //바이트 단위로 패킷을 읽음
                            if (b == '\n') //메시지를 한 줄 다 읽으면
                            {
                                byte[] encodedBytes = new byte[readBufferPosition];
                                System.arraycopy(readBuffer, 0, encodedBytes, 0,
                                        encodedBytes.length);

                                //서버에 받은 메시지
                                String recvMessage = new String(encodedBytes, "UTF-8");
                                readBufferPosition = 0;
                                publishProgress(recvMessage);
                            } else //메시지를 \n이 나올 때까지 계속해서 읽음
                            {
                                readBuffer[readBufferPosition++] = b;
                            }
                        }


                    }
                } catch (IOException e) {
                    Log.e(TAG, "disconnected", e);
                    return false;
                }
            }
        }
        @Override
        protected void onProgressUpdate(String... message) {
            receiveMsgFromServer(message[0].trim());
        }
        @Override
        protected void onPostExecute(Boolean isSucess) {
            super.onPostExecute(isSucess);
            //서버와 연결이 안되어있어서 교신에 실패한 경우
            if (!isSucess) {
                closeSocket(); //소켓 닫기
                Log.d(TAG, "Device connection was lost");
                isConnectionError = true;
                if (BluetoothActivity.alive) {
                    sendMsgToOther(Actions.TO_MAIN_ACTIVITY, "showQuitDialog", "기기와 연결이 끊어졌습니다.");
                    sendMsgToOther(Actions.TO_STAT_ACTIVITY, "finish");
                }
            }
        }

        @Override
        protected void onCancelled(Boolean aBoolean) {
            super.onCancelled(aBoolean);
            Log.d(TAG, "onCancelled : closeSocket");
            closeSocket();
        }

        //블루투스 소켓 닫기
        void closeSocket() {
            try {
                Log.d(TAG, "closeSocket : closeSocket");
                mBluetoothSocket.close();
                if (BluetoothActivity.alive) {
                    sendMsgToOther(Actions.TO_MAIN_ACTIVITY, "setTextView", R.id.txtConnState + "", "연결 상태: 연결 안됨");
                }
                Log.d(TAG, "close socket()");
                connectedToServer = false;
            } catch (IOException e2) {

                Log.e(TAG, "unable to close() " +
                        " socket during connection failure", e2);
            }
        }
        //출력 스트림으로 메시지 전송
        void write(String msg) {
            msg += "\n";
            try {
                mOutputStream.write(msg.getBytes());
                mOutputStream.flush();
            } catch (IOException e) {
                Log.e(TAG, "Exception during send", e);
                if(BluetoothActivity.alive)
                {
                    sendMsgToOther(Actions.TO_MAIN_ACTIVITY, "showQuitDialog", "스마트 약통과 연결되어있지 않습니다.");
                    sendMsgToOther(Actions.TO_STAT_ACTIVITY, "finish");
                }
                else {
                    stopSelf();
                }
            }
        }
    }

    //서비스가 종료될 때 호출
    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy");
        if (mConnectTask != null) {
            mConnectTask.cancel(true); //클라이언트-서버 연결 작업 중지
        }
        if (mConnectedTask != null) {
            mConnectedTask.cancel(true); //클라이언트-서버 메시지 통신 작업 중지
        }
        connectedToServer = false;
        if(msgReceiver!=null)
            unregisterReceiver(msgReceiver); //리시버 등록 해제
    }

    //서버에서 메시지를 받는 작업(파라미터가 서버에서 온 메시지다.)
    void receiveMsgFromServer(String msg)
    {
        boolean showToast = true;
        //블루투스가 꺼지거나 켜질 조건을 기술 ->
        if (msg.equals("turn on complete") || msg.equals("turn off complete")) {
            if (BluetoothActivity.alive) {
                final ProgressDialog pd = BluetoothActivity.pd;
                if (pd != null) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            pd.dismiss();
                        }
                    }, 500);
                }
            }
        }


    }

    //서버로 메시지 전송
    void sendMsgToServer(String msg) {
        if (mConnectedTask != null) {
            //스트림에 메시지 넣기
            mConnectedTask.write(msg);
            Log.d(TAG, "send message: " + msg);
            Toast.makeText(getApplicationContext(), "나:  " + msg, Toast.LENGTH_SHORT).show();
        }
    }


    //다른 클래스로부터 메시지를 받는 브로드캐스트리시버
    private class MessageReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent)
        {
            String action = intent.getAction();
            if(action==null)return;
            if(action.equals(Actions.TO_CLIENT_SERVICE)) {
                String msgs[] = intent.getStringArrayExtra("msg");
                if(msgs!=null)
                    if(msgs[0].equals("sendMsgToServer")){
                        sendMsgToServer(msgs[1]);
                    }
            }
        }
    }
    //다른 클래스의 브로드캐스트 리시버로 메시지를 전송하는 함수
    private void sendMsgToOther(String action, String ...msg)
    {
        Intent intent = new Intent();
        intent.setAction(action);
        intent.putExtra("msg",msg);
        sendBroadcast(intent);
    }

}
