package com.example.iothome.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.iothome.MainActivity;
import com.example.iothome.R;
import com.example.iothome.data.Data;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModels = null;
    private MainActivity This;

    private EditText edTemp;
    private EditText edWet;
    private EditText edLight;
    private EditText edMq2;
    private EditText edMq135;

    private EditText edMq135Th;
    private SeekBar sbMq135Th;
    private Button btnMq135Set;

    private Switch swFan;
    private Switch swBeep;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
         dashboardViewModels = ViewModelProviders.of(getActivity()).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        // 找ID
        edTemp =root.findViewById(R.id.edTemp);
        edWet  =root.findViewById(R.id.edWet);
        edLight = root.findViewById(R.id.edLight);
        edMq2 = root.findViewById(R.id.edMq2);
        edMq135 = root.findViewById(R.id.edMq135);

        edMq135Th  =root.findViewById(R.id.edMq135Set);
        sbMq135Th = root.findViewById(R.id.sbMq135);
        btnMq135Set = root.findViewById(R.id.btnSetMq135);

        swFan = root.findViewById(R.id.switchFan);
        swBeep = root.findViewById(R.id.switchBeep);

        //设置监听
        dashboardViewModels.getNodeData().observe(getViewLifecycleOwner(), new Observer<Data>() {
            @Override
            public void onChanged(Data data) {
                LoadUIData(data);
            }
        });
        dashboardViewModels.getMq135Th().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                edMq135Th.setText(s);
                if(!s.isEmpty()){
                    int progress = Integer.parseInt(s);
                    sbMq135Th.setProgress(progress);
                }
            }
        });
        dashboardViewModels.getSwFan().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                swFan.setChecked(aBoolean);
            }
        });
        dashboardViewModels.getSwBeep().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                swBeep.setChecked(aBoolean);
            }
        });
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        This = (MainActivity) getActivity();
        // 设置按键 监听
        btnMq135Set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String th = edMq135Th.getText().toString();
                if(!th.isEmpty()){
                    sbMq135Th.setProgress(Integer.parseInt(th));
                    if(This.mTcpClient!=null) {
                        This.mTcpClient.send("{ \"to_did\":" + This.mConnectedNode + ",\"cmd\":\"send:*T" + th + "#\"}"); //发送设置阈值指令
                    }else{
                        Toast.makeText(getActivity(),"请先连接服务器",Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(getActivity(),"输入有误",Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 滑条控件监听
       sbMq135Th.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
           @Override
           public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
               edMq135Th.setText(String.valueOf(progress));
           }

           @Override
           public void onStartTrackingTouch(SeekBar seekBar) {

           }

           @Override
           public void onStopTrackingTouch(SeekBar seekBar) {

           }
       });

        // 状态监听设置
        swFan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(This.mTcpClient!=null) {
                    if(isChecked){
                        This.mTcpClient.send("{\"to_did\":" + This.mConnectedNode + ",\"cmd\":\"send:*SF1#\"}"); //发送指令
                    }else{
                        This.mTcpClient.send("{\"to_did\":" + This.mConnectedNode + ",\"cmd\":\"send:*SF0#\"}");
                    }
                }else{
                    Toast.makeText(getActivity(),"请先连接服务器",Toast.LENGTH_LONG).show();
                }
            }
        });
        swBeep.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(This.mTcpClient!=null) {
                    if(isChecked){
                        This.mTcpClient.send("{\"to_did\":" + This.mConnectedNode + ",\"cmd\":\"send:*SB1#\"}");
                    }else{
                        This.mTcpClient.send("{\"to_did\":" + This.mConnectedNode + ",\"cmd\":\"send:*SB0#\"}");
                    }
                }else{
                    Toast.makeText(getActivity(),"请先连接服务器",Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    // 加载UI数据
    private void LoadUIData(Data data){
        edTemp.setText(String.valueOf(data.getTemp())+" ℃");
        edWet.setText(String.valueOf(data.getWet())+" %RH");
        edLight.setText(String.valueOf(data.getLight())+" Lux");
        edMq2.setText(String.valueOf(data.getMq2()));
        edMq135.setText(String.valueOf(data.getMq135()));
    }
}
