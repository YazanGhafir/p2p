package com.snubbull.app;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.Date;

public class TransporterNotice {

  @SerializedName("to")
  @Expose
  private String to;
  @SerializedName("from")
  @Expose
  private String from;
  @SerializedName("meansoftransport")
  @Expose
  private String meansoftransport;
  @SerializedName("time")
  @Expose
  private Date time;

  /**
   * Constructor.
   */
  public TransporterNotice(String from, String to, String meansoftransport, Date time) {
    this.to = to;
    this.from = from;
    this.meansoftransport = meansoftransport;
    this.time = time;

  }


  public String getFrom() {
    return from;
  }

  public String getTo() {
    return to;
  }

  public Date getTime() {
    return time;
  }

  public String getMeansOfTransport() {
    return meansoftransport;
  }

}
