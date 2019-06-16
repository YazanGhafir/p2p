package com.snubbull.app;

import com.snubbull.app.model.Priced;

class MockPriced implements Priced {

  private float price;

  public MockPriced(float price) {
    this.price = price;
  }

  @Override
  public float getPrice() {
    return price;
  }

  @Override
  public String toString() {
    return String.valueOf(price);
  }
}
