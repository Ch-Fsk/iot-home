package com.example.iothome;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.os.StrictMode;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.iothome.data.Data;
import com.example.iothome.data.Device;
import com.example.iothome.socket.TCPClient;
import com.example.iothome.ui.dashboard.DashboardViewModel;
import com.example.iothome.ui.home.HomeViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import org.json.JSONArray;

import java.lang.ref.WeakReference;
import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    public static final int DATA_HISTORY  = 0;
    public static final int DATA_NODE_SENSOR  = 1;
    public static final int DATA_NODES_LIST  = 2;

    public static final int ROLE_OTHER  = 0;
    public static final int ROLE_USER  = 1;
    public static final int ROLE_NODE  = 2;

    private static final String CMD_GET_NODE_LIST = "{\"to_did\":0,\"cmd\":\"getNodes\"}";
    private static final String CMD_GET_NODE_HISTORY = "{\"to_did\":0,\"cmd\":\"getNodes\"}";

    public static int APP_ID;

    public static Context context;
    public TCPClient mTcpClient = null;
    public int mReceivedType = DATA_NODES_LIST; //默认获取节点列表
    public String mConnectedNode;
    private final MyHandler myHandler = new MyHandler(this);
    private MyBroadcastReceiver myBroadcastReceiver = new MyBroadcastReceiver();
    public  ExecutorService exec = Executors.newCachedThreadPool();


    public boolean mIsConnected=false;
    private ArrayList<String> devicesList;

    private DashboardViewModel dashboardViewModel;
    private HomeViewModel homeViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //your codes here
        }
        return super.onCreateView(name, context, attrs);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 配置导航栏
        BottomNavigationView navView = findViewById(R.id.nav_view);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);

        devicesList = new ArrayList<>(100);

        dashboardViewModel = ViewModelProviders.of(this).get(DashboardViewModel.class);
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);

        getRandomAppID();

        bindReceiver();
    }

    private  void getRandomAppID(){
        context = this;
        Random random = new Random();
        APP_ID = random.nextInt(100000)%(100000-10000+1) + 10000;
    }

    // 处理服务器数据
    private class MyHandler extends android.os.Handler{
        private WeakReference<MainActivity> mActivity;

        MyHandler(MainActivity activity){
            mActivity = new WeakReference<MainActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            if (mActivity != null){
                switch (msg.what){
                    case 1:
                        //Toast.makeText(getApplicationContext(),"获取服务器数据："+msg.obj.toString(),Toast.LENGTH_SHORT).show();
                        processMessages(msg.obj.toString());
                        break;
                    case 2:
                        break;
                }

                }
            }
        }

    private class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String mAction = intent.getAction();
            switch (mAction){
                case "tcpClientReceiver":
                    String msg = intent.getStringExtra("tcpClientReceiver");
                    Message message = Message.obtain();
                    message.what = 1;
                    message.obj = msg;
                    myHandler.sendMessage(message);
                    break;
            }
        }
    }

    private void bindReceiver(){
        IntentFilter intentFilter = new IntentFilter("tcpClientReceiver");
        registerReceiver(myBroadcastReceiver,intentFilter);
    }

    //解析
    private void processMessages(String msg){
        if(mReceivedType == DATA_NODE_SENSOR){ //处理来自Node的数据
            try {
                Gson gson = new Gson();
                Data data = gson.fromJson(msg,Data.class);
                dashboardViewModel.setNodeData(data);
            }catch (Exception e ){
            }
        }else if (mReceivedType == DATA_NODES_LIST) {  //获取节点列表
            mReceivedType = DATA_NODE_SENSOR;           //获取一次便切换回Node状态
            try {
                JSONArray jsonArray = new JSONArray(msg);
                devicesList.clear();

                for(int i = 0 ;i< jsonArray.length(); i++){
                    devicesList.add(String.valueOf(jsonArray.getInt(i)));
                }
//
//                for(JsonElement devJson : jsonArray){
//                    Device dev = gson.fromJson(devJson,Device.class);
//                    devicesList.add(String.valueOf(dev.getDid()));
//                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,R.layout.node_name);
                arrayAdapter.addAll(devicesList);
                homeViewModel.setLvNodesData(arrayAdapter);

            }catch (Exception e){
                System.out.println("接收数据出错："+e.toString());
            }
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }

}
