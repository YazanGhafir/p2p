package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Ping {

    private String ping;

    public Ping(@JsonProperty("ping") String ping){
        this.ping = ping;
    }

    public Ping(){
    }

    public String getPing() {
        return ping;
    }
}
