package com.snubbull.app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.snubbull.app.model.Utility.Capacity;
import com.snubbull.app.model.Utility.MeansOfTransport;
import com.snubbull.app.repository.Transported;
import java.util.Objects;

public class TransporterNotice extends AbstractNotice implements Transported {

  @SerializedName("meansoftransport")
  @Expose
  private MeansOfTransport meansoftransport;


  /**
   * Constructs a Notice that organizes all the data a Transporter has to provide.
   *
   * @param from The origin location
   * @param to The destination
   * @param phone The phone number to the transporter.
   * @param price The requested price.
   * @param deliveryDate When the delivery will take place.
   * @param meansOfTransport By which means the package will be delivered, car, train, etc.
   * @param capacity How much the transporter can transport.
   * @throws Exception Throws an exception if not all fields are entered properly.
   */
  public TransporterNotice(String from, String to, String phone, float price, String deliveryDate,
      MeansOfTransport meansOfTransport, Capacity capacity) throws Exception {
    super(from, to, phone, price, deliveryDate, capacity);
    this.meansoftransport = meansOfTransport;
  }

  public TransporterNotice() {

  }

  /**
   * Get how this package will be delivered.
   *
   * @return The method that will be used to deliver the package.
   */
  public MeansOfTransport getMeansOfTransport() {
    return meansoftransport;
  }

  /**
   * Set how this package will be delivered.
   *
   * @param meansOfTransport How the package will be delivered, car, train, etc.
   */
  public void setMeansOfTransport(MeansOfTransport meansOfTransport) {
    meansoftransport = meansOfTransport;
  }


  @Override
  public String toString() {
    return super.toString() + "quantity= " + meansoftransport;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || !(o instanceof TransporterNotice)) {
      return false;
    }
    TransporterNotice notice = (TransporterNotice) o;
    return super.equals(o) && Objects.equals(meansoftransport, notice.meansoftransport);
  }

  @Override
  public int hashCode() {
    return super.hashCode() + meansoftransport.hashCode();
  }

}
