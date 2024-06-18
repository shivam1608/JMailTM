package me.shivzee.util;

public class Sender {
    private String address;
    private String name;

    public Sender(String address , String name){
        this.address = address;
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }
}
