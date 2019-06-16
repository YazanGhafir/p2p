package com.example.demo.model;

import java.io.Serializable;

public class Utility {

    public enum Capacity implements Serializable {
        SMALL, MEDIUM, BIG;

        /**
         * Uses the weight and volume the user entered to roughly
         * describe the size of the package.
         * @param weightString The weight of the package
         * @param volumeString The volume of the package.
         * @return The Capacity, calculated from the weight and volume.
         */
        public static Capacity getSize(String weightString, String volumeString) {
            float weight = Float.parseFloat(weightString);
            float volume = Float.parseFloat(volumeString);
            if (weight < 10 && volume < 10) {
                return SMALL;
            } else if (weight < 20 && volume < 20) {
                return MEDIUM;
            } else {
                return BIG;
            }
        }
    }


    public enum MeansOfTransport {
        CAR, BUS, TRAIN;

        /**
         * Parse the means of transport the User entered to one of our alternatives.
         * @param meansOfTransport By which means the transportation will take place.
         * @return
         */
        public static MeansOfTransport getMeansOfTransport(String meansOfTransport) {
            if ("car".equals(meansOfTransport.toLowerCase())) {
                return CAR;
            } else if ("bus".equals(meansOfTransport.toLowerCase())) {
                return BUS;
            } else {
                return TRAIN;
            }
        }


    }

}