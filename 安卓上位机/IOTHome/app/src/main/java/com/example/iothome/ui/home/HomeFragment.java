package com.example.iothome.ui.home;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.iothome.MainActivity;
import com.example.iothome.R;
import com.example.iothome.socket.TCPClient;
import com.google.android.material.textfield.TextInputEditText;

public class HomeFragment extends Fragment {

    private MainActivity  This = null;

    private HomeViewModel homeViewModel;

    private Button btnConnServer = null;
    private TextInputEditText inIp = null;
    private TextInputEditText inPort = null;

    private ListView lvOnlineNodes = null;
    private Button btnConnNodes = null;
    private Button btnFreshNodes = null;

    private  String node_selected;

    private ArrayAdapter<String> lvAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = ViewModelProviders.of(getActivity()).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        inIp = root.findViewById(R.id.inIP);
        inPort = root.findViewById(R.id.inPort);
        btnConnServer = root.findViewById(R.id.btnConnSrv);

        lvOnlineNodes = root.findViewById(R.id.lvNodes);
        btnConnNodes = root.findViewById(R.id.btnConnNode);
        btnFreshNodes = root.findViewById(R.id.btnFreshNode);

        //设置ip 监听
        homeViewModel.getIpText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                inIp.setText(s);
            }
        });
        homeViewModel.getPortText().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                inPort.setText(integer.toString());
            }
        });
        //设置连接按键的viewmodel监听
        homeViewModel.getBtnConnServerText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                btnConnServer.setText(s);
            }
        });

        homeViewModel.getBtnConnNodeText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                btnConnNodes.setText(s);
            }
        });

        homeViewModel.getLvNodesData().observe(getViewLifecycleOwner(), new Observer<ArrayAdapter<String>>() {
            @Override
            public void onChanged(ArrayAdapter<String> stringArrayAdapter) {
                lvOnlineNodes.setAdapter(stringArrayAdapter);
            }
        });

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        This = (MainActivity) getActivity();

        lvAdapter = new ArrayAdapter<>(getActivity(),R.layout.node_name);
       // lvAdapter.add("node1:192.168.1.10");
       // lvAdapter.add("node2:192.168.1.10");
        homeViewModel.setLvNodesData(lvAdapter);

        btnConnServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(This.mIsConnected == false){
                    final String ip = inIp.getText().toString();
                    final int port;
                    if(inPort.getText().toString() != "")
                        port = Integer.parseInt(inPort.getText().toString());
                    else
                        port = 0;
                    if(!ip.isEmpty() && port !=0) {
                        This.mTcpClient = new TCPClient(ip,port);
                        This.exec.execute(This.mTcpClient);
                        final ProgressDialog progressDialog = new ProgressDialog(This,
                                R.style.AppTheme_Dark_Dialog);
                        progressDialog.setIndeterminate(true);
                        progressDialog.setMessage("正在连接...");
                        progressDialog.show();
                        new Handler().postDelayed(new Runnable(){
                            public void run(){
                                if(This.mTcpClient != null){
                                    This.mIsConnected = true;
                                    homeViewModel.setBtnConnServerText("已连接");
                                    homeViewModel.setIpText(ip);
                                    homeViewModel.setPortText(port);
                                    //APP认证身份
                                    This.mTcpClient.send("{\"did\":"+This.APP_ID+",\"role\":1}");
                                }else{
                                    Toast.makeText(This,"连接失败！",Toast.LENGTH_LONG).show();
                                }
                                progressDialog.dismiss();
                            }
                        },1000);
                    }else{
                        Toast.makeText(This,"输入参数有误！",Toast.LENGTH_LONG).show();
                    }

                }else{  // 断开连接
                    homeViewModel.setBtnConnServerText("连接");
                    This.mIsConnected =false;
                    try
                    {
                        System.out.println("onDestroy.");
                        This.mTcpClient.closeSelf();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }

            }
        });

        btnConnNodes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnConnNodes.getText().toString().equals("订阅")){
                    homeViewModel.setBtnConnNodeText("取消");
                    if(This.mTcpClient != null ){
                        This.mConnectedNode = node_selected;
                        This.mTcpClient.send("{\"to_did\":"+This.mConnectedNode+",\"cmd\":\"subNode\"}");
                        //Toast.makeText(This,"正在刷新节点列表！",Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(This,"请先连接服务器或订阅节点！",Toast.LENGTH_SHORT).show();
                    }
                }else if(btnConnNodes.getText().toString().equals("取消")){
                    homeViewModel.setBtnConnNodeText("订阅");
                    if(This.mTcpClient != null ){
                        This.mTcpClient.send("{\"to_did\":"+This.mConnectedNode+",\"cmd\":\"deSubNode\"}");
                        This.mConnectedNode = "";
                       // Toast.makeText(This,"正在刷新节点列表！",Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(This,"请先连接服务器或订阅节点！",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        // 刷新监听
        btnFreshNodes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(This.mTcpClient != null){
                    This.mTcpClient.send("{\"to_did\":0,\"cmd\":\"getNodes\"}");
                    This.mReceivedType = This.DATA_NODES_LIST;
                    Toast.makeText(This,"正在刷新节点列表！",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(This,"请先连接服务器！",Toast.LENGTH_SHORT).show();
                }
            }
        });
        //listview 点击监听
        lvOnlineNodes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String node = (String) lvOnlineNodes.getItemAtPosition(position);
                Toast.makeText(This, "已选择节点：" + node, Toast.LENGTH_SHORT).show();
                node_selected = node;
            }
        });
    }
}
