package com.example.iothome.ui.charts;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.iothome.MainActivity;
import com.example.iothome.R;
import com.example.iothome.data.Data;
import com.example.iothome.ui.dashboard.DashboardViewModel;
import com.github.mikephil.charting.charts.LineChart;

import java.util.ArrayList;
import java.util.List;


public class ChartsFragment extends Fragment {

    private ChartsViewModel chartsViewModel;
    private DashboardViewModel dashboardViewModel;
    private MainActivity This;

  //  private Spinner spSelector;
    private LineChart lineChart;

    private DynamicLineChartManager dynamicLineChartManager1;
    private List<Integer> list = new ArrayList<>(); //数据集合
    private List<String> names = new ArrayList<>(); //折线名字集合
    private List<Integer> colour = new ArrayList<>();//折线颜色集合


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_chart, container, false);

        chartsViewModel = ViewModelProviders.of(getActivity()).get(ChartsViewModel.class);
        dashboardViewModel = ViewModelProviders.of(getActivity()).get(DashboardViewModel.class);

       // spSelector = root.findViewById(R.id.spSelector);
        lineChart = root.findViewById(R.id.charts);

        InitUI();

        chartsViewModel.getSpCurrentSelected().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer s) {
      //          spSelector.setSelection(s);
            }
        });

        dashboardViewModel.getNodeData().observe(getViewLifecycleOwner(), new Observer<Data>() {
            @Override
            public void onChanged(Data data) {
                if(data !=null){
                    list.add((int)data.getTemp());
                    list.add((int)data.getWet());
                    list.add((int)data.getLight());
                    list.add((int)data.getMq2());
                    list.add((int)data.getMq135());
                    dynamicLineChartManager1.addEntry(list);
                    list.clear();
                }
            }
        });

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        This = (MainActivity) getActivity();
    }

    private  void InitUI(){
        //折线名字
        names.add("温度");
        names.add("湿度");
        names.add("光强");
        names.add("MQ2");
        names.add("MQ135");
        //折线颜色
        colour.add(Color.RED);
        colour.add(Color.YELLOW);
        colour.add(Color.GREEN);
        colour.add(Color.BLUE);
        colour.add(Color.BLACK);

        dynamicLineChartManager1 = new DynamicLineChartManager(lineChart, names, colour);
        dynamicLineChartManager1.setYAxis(1000, 0, 100);
    }
    //按钮点击添加数据
    public void addEntry(int value) {
        dynamicLineChartManager1.addEntry(value);
    }
}
