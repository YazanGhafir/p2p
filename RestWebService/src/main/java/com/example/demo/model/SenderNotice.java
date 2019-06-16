package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.UUID;

public class SenderNotice implements Serializable {

    private String deviceID;
    private UUID id;
    private String from;
    private String to;
    private String phone;
    private float price;
    private String deliveryDate;
    private String capacity;
    private int quantity;

    public SenderNotice(@JsonProperty("deviceID") String deviceID, @JsonProperty("id") UUID id,
                        @JsonProperty("from") String from,
                        @JsonProperty("to") String to, @JsonProperty("phone") String phone,
                        @JsonProperty("price") float price, @JsonProperty("deliveryDate") String deliveryDate,
                        @JsonProperty("quantity") int quantity,
                        @JsonProperty("capacity") String capacity) {

        //super(id,from,to,phone,price,deliveryDate,capacity);
        this.deviceID = deviceID;
        this.id = id;
        this.from = from;
        this.to = to;
        this.phone = phone;
        this.price = price;
        this.deliveryDate = deliveryDate;
        this.capacity = capacity;
        this.quantity = quantity;
    }
    SenderNotice(){

    }

    public int getQuantity() {
        return quantity;
    }

    public UUID getId() {
        return id;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getPhone() {
        return phone;
    }

    public float getPrice() {
        return price;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public String getCapacity() {
        return capacity;
    }

    public String getDeviceID() {
        return deviceID;
    }
}
