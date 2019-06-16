package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.UUID;

public class TransporterNotice implements Serializable {

    private String deviceID;
    private UUID id;
    private String from;
    private String to;
    private String phone;
    private float price;
    private String deliveryDate;
    private String capacity;
    private Utility.MeansOfTransport meansoftransport;

    public TransporterNotice(@JsonProperty("deviceID") String deviceID,
                             @JsonProperty("id") UUID id, @JsonProperty("from") String from,
                             @JsonProperty("to") String to, @JsonProperty("phone") String phone,
                             @JsonProperty("price") float price, @JsonProperty("deliveryDate") String deliveryDate,
                             @JsonProperty("meansoftransport") Utility.MeansOfTransport meansoftransport,
                             @JsonProperty("capacity") String capacity){
        //super(id,from,to,phone,price,meansoftransport,capacity);
        this.deviceID = deviceID;
        this.id = id;
        this.from = from;
        this.to = to;
        this.phone = phone;
        this.price = price;
        this.deliveryDate = deliveryDate;
        this.capacity = capacity;
        this.meansoftransport = meansoftransport;
    }

    TransporterNotice(){

    }
    /**
     * Get how this package will be delivered.
     * @return The method that will be used to deliver the package.
     */
    public Utility.MeansOfTransport getMeansoftransport() {
        return meansoftransport;
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
