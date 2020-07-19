package com.example.iothome.ui.charts;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.iothome.data.Data;

import java.lang.reflect.Array;

public class ChartsViewModel extends ViewModel {

    private MutableLiveData<Data[]> chartsData = new MutableLiveData<>();
    private MutableLiveData<Integer> spCurrentSelected = new MutableLiveData<>();

    public ChartsViewModel(){

    }

    public MutableLiveData<Integer> getSpCurrentSelected(){
        return  spCurrentSelected;
    }
    public void setSpCurrentSelected(Integer s){
        spCurrentSelected.setValue(s);
    }

}
