package com.snubbull.app.model;

public interface Route {

  /**
   * Gets the origin location.
   *
   * @return The origin location.
   */
  String getFrom();

  /**
   * Gets the destination.
   *
   * @return The destination.
   */
  String getTo();
}
