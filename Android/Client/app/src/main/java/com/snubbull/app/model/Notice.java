package com.snubbull.app.model;

public interface Notice extends Priced, Route {

  /**
   * Get the date the sender wants the delivery to be made or the date the transporter is offering
   * to transport the package.
   *
   * @return The delivery date.
   */
  String getDeliveryDate();
  String getDeviceId();
}
