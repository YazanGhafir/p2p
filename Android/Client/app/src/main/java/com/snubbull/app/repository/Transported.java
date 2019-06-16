package com.snubbull.app.repository;

import com.snubbull.app.model.Utility.MeansOfTransport;

public interface Transported {

  /**
   * Get how the the transportation will take place, train, car, etc.
   *
   * @return The means of transport.
   */
  MeansOfTransport getMeansOfTransport();
}
