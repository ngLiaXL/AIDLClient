package com.ldroid.aidlclient;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.ldroid.aidlserver.IMyAidlInterface;

/**
 * 1、新建aidl server aidl client 两个工程
 * 2、server 新建 xxx.aidl 文件 rebuild 后app/generated/source/aidl/debug 会生成xxx.java 接口文件
 * 3、client 新建xxx.aidl 包名要和server端包名一致，工程main目录新建aidl目录，aidl目录新建包，包名是server的aidl文件包名
 * 4、编写server端远程service
 * 5、client端调用service
 */
public class MainActivity extends AppCompatActivity {


    private IMyAidlInterface mMyInterface;

    private TextView mTvShow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTvShow = (TextView) findViewById(R.id.show_server);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setAction("com.ldroid.action.AIDL_SERVICE");
                bindService(intent, serviceConnection, Service.BIND_AUTO_CREATE);
                mTvShow.setText("start to connect remote service");
            }
        });


        findViewById(R.id.grep).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getRemoteName();
            }
        });

    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mMyInterface = IMyAidlInterface.Stub.asInterface(service);
            System.out.println("service connected");
            mTvShow.setText("service connected");


        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mMyInterface = null;
            mTvShow.setText("service disconnected");


        }
    };


    public void getRemoteName() {
        try {
            String name = mMyInterface.getMyName();
            mTvShow.setText("I am remote service name " + name);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

}
