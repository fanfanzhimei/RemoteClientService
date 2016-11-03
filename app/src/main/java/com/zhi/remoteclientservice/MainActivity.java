package com.zhi.remoteclientservice;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zhi.aidl.QueryStudentAIDL;

public class MainActivity extends Activity implements View.OnClickListener {
    private ServiceConnection conn = new QueryServiceConnection();
    private QueryStudentAIDL aidl;
    private EditText mEtNumber;
    private Button mBtnQuery;
    private TextView mTvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEtNumber = (EditText) findViewById(R.id.et_number);
        mTvResult = (TextView) findViewById(R.id.tv_result);
        mBtnQuery = (Button) findViewById(R.id.btn_query);
        mBtnQuery.setOnClickListener(this);

        Intent intent = new Intent("com.zhi.www.fan");
        bindService(intent, conn, BIND_AUTO_CREATE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_query:
                query();
                break;
        }
    }

    private void query(){
        Editable editable = mEtNumber.getText();
        if(null == editable || "".equals(editable.toString().trim())){
            Toast.makeText(MainActivity.this, R.string.str_input_error, Toast.LENGTH_SHORT).show();
            return;
        }
        int number = Integer.valueOf(editable.toString());
        try {
            mTvResult.setText(aidl.queryStudent(number));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public class QueryServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            aidl =  QueryStudentAIDL.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            aidl = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(conn);
    }
}