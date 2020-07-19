package com.example.iothome.ui.dashboard;

import androidx.annotation.BoolRes;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.iothome.data.Data;

public class DashboardViewModel extends ViewModel {

    private MutableLiveData<Data> nodeData = new MutableLiveData<>();
    private MutableLiveData<String> mq135Th =new MutableLiveData<>();
    private MutableLiveData<Boolean> swFan = new MutableLiveData<>();
    private MutableLiveData<Boolean> swBeep = new MutableLiveData<>();

    public DashboardViewModel() {
    }

    public MutableLiveData<Data> getNodeData(){
        return  nodeData;
    }
    public void setNodeData(Data data){
        nodeData.setValue(data);
    }

    public MutableLiveData<String> getMq135Th(){
        return mq135Th;
    }
    public void setMq135Th(String s){
        mq135Th.setValue(s);
    }

    public MutableLiveData<Boolean> getSwFan(){
        return swFan;
    }
    public void setSwFan(Boolean b){
        swFan.setValue(b);
    }

    public MutableLiveData<Boolean> getSwBeep(){
        return  swBeep;
    }
    public void setSwBeep(Boolean b){
        swBeep.setValue(b);
    }

}