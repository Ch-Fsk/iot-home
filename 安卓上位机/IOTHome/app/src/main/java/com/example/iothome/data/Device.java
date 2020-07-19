package com.example.iothome.data;

public class Device {
    private int did;
    private int role;

    public void setDid(int did){
        this.did = did;
    }
    public int getDid(){
        return this.did;
    }
    public void setRole(int role){
        this.role = role;
    }
    public int getRole(){
        return this.role;
    }
}
