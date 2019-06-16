package com.snubbull.app.model;

/**
 * AndroidID class with shared static androidID field
 */
public class AndroidID {

  private static String androidID = "";

  AndroidID(String androidID) {
    AndroidID.androidID = androidID;
  }

  public static String getAndroidID() {
    return androidID;
  }

  public static void setAndroidID(String androidID) {
    AndroidID.androidID = androidID;
  }
}
