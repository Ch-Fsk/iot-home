package com.example.iothome.data;

public class Data {

    private float light;
    private float mq2;
    private float mq135;
    private float temp;
    private float wet;

    public void setLight(float light){
        this.light = light;
    }
    public float getLight(){
        return this.light;
    }
    public void setMq2(float mq2){
        this.mq2 = mq2;
    }
    public float getMq2(){
        return this.mq2;
    }
    public void setMq135(float mq135){
        this.mq135 = mq135;
    }
    public float getMq135(){
        return this.mq135;
    }
    public void setTemp(float temp){
        this.temp = temp;
    }
    public float getTemp(){
        return this.temp;
    }
    public void setWet(float wet){
        this.wet = wet;
    }
    public float getWet(){
        return this.wet;
    }
}

