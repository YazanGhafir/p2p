package com.snubbull.app;

import com.snubbull.app.model.Route;

class MockRoute implements Route {

  private String from;
  private String to;

  public MockRoute(String from, String to) {
    this.from = from;
    this.to = to;
  }

  @Override
  public String getFrom() {
    return from;
  }

  @Override
  public String getTo() {
    return to;
  }

  @Override
  public String toString() {
    return from + "→" + to;
  }
}
