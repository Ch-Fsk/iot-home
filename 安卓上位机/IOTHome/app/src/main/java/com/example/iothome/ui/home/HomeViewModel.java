package com.example.iothome.ui.home;

import android.widget.ArrayAdapter;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.iothome.R;

public class HomeViewModel extends ViewModel {

    // 连接服务器按键状态
    private MutableLiveData<String> btnConnText = new MutableLiveData<>();
    private MutableLiveData<String> inIp = new MutableLiveData<>();
    private MutableLiveData<Integer> inPort = new MutableLiveData<>();

    // ListView s数据
    private MutableLiveData<ArrayAdapter<String> > listViewData = new MutableLiveData<>();
    private MutableLiveData<String> btnNodeText  = new MutableLiveData<>();

    //设置默认值
    public HomeViewModel(){
        inIp.setValue("129.211.127.83");
        inPort.setValue(8000);
    }

    public LiveData<String> getBtnConnServerText() {
        return btnConnText;
    }
    public void setBtnConnServerText(String s){
        btnConnText.setValue(s);
    }

    public LiveData<String> getIpText() {
        return inIp;
    }
    public void setIpText(String s){
        inIp.setValue(s);
    }

    public LiveData<Integer> getPortText() {
        return inPort;
    }
    public void setPortText(Integer p){
        inPort.setValue(p);
    }

    public LiveData<String> getBtnConnNodeText() {
        return btnNodeText;
    }
    public void setBtnConnNodeText(String s){
        btnNodeText.setValue(s);
    }

    public LiveData<ArrayAdapter<String>> getLvNodesData() {
        return listViewData;
    }
    public void setLvNodesData(ArrayAdapter<String> array) {
        listViewData.setValue(array);
    }

}