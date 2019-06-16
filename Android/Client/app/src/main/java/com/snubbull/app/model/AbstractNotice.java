package com.snubbull.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Objects;
import java.util.UUID;

public abstract class AbstractNotice implements Notice, Serializable {



  @SerializedName("id")
  @Expose
  private UUID id;
  @SerializedName("deviceID")
  @Expose
  private String deviceID;
  @SerializedName("from")
  @Expose
  private String from;
  @SerializedName("to")
  @Expose
  private String to;
  @SerializedName("phone")
  @Expose
  private String phone;
  @SerializedName("price")
  @Expose
  private float price;
  @SerializedName("deliveryDateFormat")
  @Expose
  private transient SimpleDateFormat deliveryDateFormat;
  @SerializedName("deliveryDate")
  @Expose
  private String deliveryDate;
  @SerializedName("capacity")
  @Expose
  private Utility.Capacity capacity;

  /**
   * Constructs a Notice that can be displayed in a list view.
   *
   * @param from The origin location.
   * @param to The destination.
   * @param phone The phone number to the creator of this Notice.
   * @param price The requested or offered price.
   * @param deliveryDateText The delivery date of the package, as a string.
   * @param capacity The size or transport room of the notice.
   * @throws Exception An exception is thrown if not all fields are entered.
   */
  protected AbstractNotice(String from, String to, String phone, float price,
      String deliveryDateText,
      Utility.Capacity capacity) throws Exception {
    if (from.equals("") || to.equals("") || deliveryDateText.equals("")) {
      throw new Exception("you need to write all the corresponding values ");
    }
    int numbers = phone.length();
    if (numbers != 9) {
      throw new Exception("enter a 9 digit phonenumber plz");
    }

    this.from = from;
    this.to = to;
    this.phone = phone;
    if (price < 0) {
      throw new Exception("price has to be higher then 0");
    }
    this.price = price;
    this.capacity = capacity;
    deliveryDate = deliveryDateText;
    //deliveryDate = deliveryDateFormat.parse(deliveryDateText);

    deviceID = AndroidID.getAndroidID();
  }

  public AbstractNotice() {

  }

  public UUID getId() {
    return id;
  }

  /**
   * Get the origin location.
   *
   * @return where the package departed..
   */
  public String getFrom() {
    return from;
  }

  /**
   * Set the origin location. Update the variable from.
   */
  public void setFrom(String from) {
    this.from = from;
  }

  /**
   * Get the destination.
   *
   * @return where the package should be sent.
   */
  public String getTo() {
    return to;
  }

  /**
   * Set the destination. Update the variable to.
   */
  public void setTo(String to) {
    this.to = to;
  }

  /**
   * Returns the phone number to the creator of the notice.
   *
   * @return The phone number.
   */
  public String getPhone() {
    return phone;
  }

  /**
   * Sets the phone number to the creator of the notice.
   */
  public void setPhone(String phone) {
    this.phone = phone;
  }

  /**
   * Getter for the price of this notice.
   *
   * @return price of the notice.
   */
  public float getPrice() {
    return price;
  }

  /**
   * Setter for the price of this notice. Update the variable price.
   */
  public void setPrice(float price) {
    this.price = price;
  }

  /**
   * Get the delivery date.
   *
   * @return the delivery date.
   */
  public String getDeliveryDate() {
    return deliveryDate;
  }

  /**
   * Set the Delivery date. Update the variable date.
   */
  public void setDeliveryDate(String deliveryDate) {
    this.deliveryDate = deliveryDate;
  }

  /**
   * Get the format used used for the date.
   *
   * @return returns the format that is used when parsing the date.
   */
  public SimpleDateFormat getDeliveryDateFormat() {
    return deliveryDateFormat;
  }

  /**
   * The capacity roughly describes the size of the package.
   *
   * @return get the capacity.
   */
  public Utility.Capacity getCapacity() {
    return capacity;
  }

  /**
   * The capacity roughly describes the size of the package or how much transport room there is.
   * Update the variable capacity.
   */
  public void setCapacity(Utility.Capacity capacity) {
    this.capacity = capacity;
  }

  public String getDeviceId(){ return deviceID; }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || !(o instanceof AbstractNotice)) {
      return false;
    }
    AbstractNotice notice = (AbstractNotice) o;
    return Objects.equals(to, notice.to)
        && Objects.equals(from, notice.from)
        && Objects.equals(phone, notice.phone)
        && Objects.equals(price, notice.price)
        && Objects.equals(capacity, notice.capacity)
        && deliveryDate.equals(notice.deliveryDate);
  }


  @Override
  public int hashCode() {
    return Objects.hash(to, from, phone, price) + deliveryDate.hashCode() + capacity.hashCode();
  }


  @Override
  public String toString() {
    return "AbstractNotice{"
        + "to =" + to
        + ", from =" + from
        + ", phone =" + phone
        + ", price =" + price
        + ", deliverDate =" + deliveryDate
        + ", Capacity =" + capacity
        + '}';
  }
}