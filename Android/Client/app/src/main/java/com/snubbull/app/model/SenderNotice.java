package com.snubbull.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.Objects;

public class SenderNotice extends AbstractNotice {

  @SerializedName("quantity")
  @Expose
  private int quantity;
  @SerializedName("size")
  @Expose
  private Utility.Capacity size;

  /**
   * Constructs a Sender Notice that can be displayed in a ListView.
   *
   * @param from The origin location
   * @param to The destination
   * @param phone The phone number to the sender.
   * @param price The offered price
   * @param deliveryDate The date and time on which the sender wants something delivered.
   * @param quantity The quantity of packages the sender wants delivered.
   * @param capacity How big the packages the sender want transported are.
   * @throws Exception Throws an exception if not all fields are entered.
   */
  public SenderNotice(String from, String to, String phone, float price, String deliveryDate,
      int quantity, Utility.Capacity capacity) throws Exception {
    super(from, to, phone, price, deliveryDate, capacity);
    this.quantity = quantity;
    this.size = size;
  }

  public SenderNotice() {

  }

  /**
   * Gets the quantity of packages this notice concerns.
   *
   * @return The quantity of packages
   */
  public int getQuantity() {
    return quantity;
  }

  /**
   * Sets the quantity of packages this notice concerns.
   *
   * @param quantity The quantity to set this notice package quantity to.
   */
  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  @Override
  public String toString() {
    return super.toString() + "quantity= " + quantity + ", size= " + size;
  }

  @Override
  public int hashCode() {
    return super.hashCode() + size.hashCode() + quantity;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || !(o instanceof SenderNotice)) {
      return false;
    }
    SenderNotice notice = (SenderNotice) o;
    return super.equals(o)
        && Objects.equals(quantity, notice.quantity)
        && Objects.equals(size, notice.size);
  }
}