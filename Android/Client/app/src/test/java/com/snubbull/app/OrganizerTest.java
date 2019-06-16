package com.snubbull.app;

import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertEquals;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import com.snubbull.app.model.Priced;
import com.snubbull.app.model.Route;
import com.snubbull.app.model.Utility;
import com.snubbull.app.model.Utility.MeansOfTransport;
import com.snubbull.app.repository.Organizer;
import com.snubbull.app.repository.Organizer.Order;
import com.snubbull.app.repository.Transported;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class OrganizerTest {

  @Rule
  public InstantTaskExecutorRule rule = new InstantTaskExecutorRule();
  private Collection<? extends Route> allRoutes;
  private Collection<? extends Priced> allPriced;
  private Collection<? extends Transported> allTransported;

  private Collection<? extends Route> filterByRoute(String from, String to) {
    allRoutes = getRouteTestCollection();
    return Organizer
        .createOrganizer(allRoutes)
        .filterByRoute(x -> x, from, to)
        .getElements();
  }

  private Collection<? extends Priced> filterByPrice(Order order, float cutoff) {
    allPriced = getPricedTestCollection();
    return Organizer.createOrganizer(allPriced)
        .orderByPrice(x -> x, order)
        .filterByPrice(x -> x, x -> order == Order.ASCENDING ? x <= cutoff : x >= cutoff)
        .getElements();
  }

  private Collection<? extends Transported> filterByMeansOfTransport(
      Utility.MeansOfTransport meansOfTransport) {
    allTransported = getTransportedTestCollection();
    return Organizer
        .createOrganizer(allTransported)
        .filterByMeansOfTransport(x -> x, meansOfTransport)
        .getElements();
  }

  @Before
  public void setup() {
  }

  private Collection<? extends Transported> getTransportedTestCollection() {
    Collection<Transported> transports = new ArrayList<>();
    transports.add(new MockTransported(MeansOfTransport.CAR));
    transports.add(new MockTransported(MeansOfTransport.BUS));
    transports.add(new MockTransported(MeansOfTransport.CAR));
    transports.add(new MockTransported(MeansOfTransport.TRAIN));
    transports.add(new MockTransported(MeansOfTransport.CAR));
    transports.add(new MockTransported(MeansOfTransport.CAR));
    transports.add(new MockTransported(MeansOfTransport.BUS));
    return transports;
  }

  private Collection<? extends Route> getRouteTestCollection() {
    Collection<Route> routes = new ArrayList<>();
    //TODO: Expand for more exhaustive testing
    routes.add(new MockRoute("Göteborg", "Stockholm"));
    routes.add(new MockRoute("Göteborg", "Stockholm"));
    routes.add(new MockRoute("Stockholm", "Göteborg"));
    routes.add(new MockRoute("A", "B"));
    return routes;
  }

  private Collection<? extends Priced> getPricedTestCollection() {
    Collection<Priced> priced = new ArrayList<>();
    priced.add(new MockPriced(7));
    priced.add(new MockPriced(900));
    priced.add(new MockPriced(Float.MAX_VALUE));
    priced.add(new MockPriced(0));
    return priced;
  }

  @Test
  public void emptyFilterReturnsAll() {
    Collection<? extends Route> filtered = filterByRoute("", "");

    assertEquals("", allRoutes, filtered);
  }

  @Test
  public void filterReturnsTheCorrectElements() {
    List<Route> expected = new ArrayList<>();
    expected.add(new MockRoute("Göteborg", "Stockholm"));
    expected.add(new MockRoute("Göteborg", "Stockholm"));
    Collection<? extends Route> filtered = filterByRoute("Göteborg", "Stockholm");
    assertEquals("Wrong elements in the filtered list",
        expected.toString(), filtered.toString());
  }

  @Test
  public void filterOnNonExistentRouteReturnsEmpty() {
    Collection<? extends Route> filtered = filterByRoute("C", "D");
    assertEquals("filterByRoute on non existent route returned something"
        , 0, filtered.size());
  }

  @Test
  public void filterOnAtoBWhereAIsEmptyReturnsAllRoutesToB() {
    List<Route> expected = new ArrayList<>();
    expected.add(new MockRoute("Göteborg", "Stockholm"));
    expected.add(new MockRoute("Göteborg", "Stockholm"));
    Collection<? extends Route> filtered = filterByRoute("", "Stockholm");
    assertEquals("Items returned by A to B are incorrect"
        , expected.toString(), filtered.toString());
  }

  @Test
  public void filterOnAToBWhereBIsEmptyReturnsAllRoutesFromA() {
    List<Route> expected = new ArrayList<>();
    expected.add(new MockRoute("Stockholm", "Göteborg"));
    Collection<? extends Route> filtered = filterByRoute("Stockholm", "");
    assertEquals("A→B with empty B does not return"
        + "the correct elements.", expected.toString(), filtered.toString());
  }

  @Test
  public void filterByPriceReturnsAllElementsLessThanMaxPrice() {
    String expected = "[0.0, 7.0]";
    Collection<? extends Priced> filtered = filterByPrice(Order.ASCENDING, 500);
    assertEquals("All elements that "
        + "should be filtered were not filtered", expected, filtered.toString());
  }

  @Test
  public void filterWithMaxFloatAsMaxPriceReturnsAllElementsWhenAscending() {
    Collection<? extends Priced> filtered = filterByPrice(Order.ASCENDING, Float.MAX_VALUE);
    assertEquals("Organizer By Price with Float.MAX_VALUE as max price"
        + " does not return the entire list", allPriced.size(), filtered.size());
  }

  @Test
  public void filterByPriceReturnsElementsInAscendingOrder() {
    String expected = "[0.0, 7.0, 900.0, " + Float.MAX_VALUE + "]";
    Collection<? extends Priced> filtered = filterByPrice(Order.ASCENDING, Float.MAX_VALUE);
    assertEquals("The collection is not sorted in ascending order after "
        + "filtering by price with order set to Ascending", expected, filtered.toString());
  }

  @Test
  public void filterByPriceReturnsElementsInDescendingOrder() {
    String expected = "[" + Float.MAX_VALUE + ", 900.0, 7.0, 0.0]";
    Collection<? extends Priced> filtered = filterByPrice(Order.DESCENDING, 0);
    assertEquals("The collection is not sorted in descending order "
        + "after filtering by price with order set to Descending", expected, filtered.toString());
  }

  @Test
  public void filterByPriceWhenDescendingReturnsAllElementsGreaterThanCutoff() {
    String expected = "[" + Float.MAX_VALUE + ", 900.0]";
    Collection<? extends Priced> filtered = filterByPrice(Order.DESCENDING, 8);
    assertEquals("Organizer by price removes all items greater"
            + " than cutoff if order is set to Descending.",
        expected, filtered.toString());
  }

  @Test
  public void filterByMeansOfTransportYieldsCorrectMeans() {
    for (Utility.MeansOfTransport mot : Utility.MeansOfTransport.values()) { // Exhaustive test
      Collection<? extends Transported> filtered = filterByMeansOfTransport(mot);
      getTransportedTestCollection();
      for (Transported transported : allTransported) {
        if (mot.equals(transported.getMeansOfTransport())) {
          assertTrue("When filtered by "
                  + mot.name()
                  + " as means of transport the filter (wrongfully) leaves out the element "
                  + transported.toString(),
              filtered.remove(transported));
        }
      }
      assertTrue("When filtered by "
          + mot.name()
          + " as means of transport the filter included more elements than expected ("
          + filtered.toString(), filtered.isEmpty());
    }
  }

  private class MockTransported implements Transported {

    private final MeansOfTransport mot;

    public MockTransported(MeansOfTransport mot) {
      this.mot = mot;
    }

    @Override
    public MeansOfTransport getMeansOfTransport() {
      return mot;
    }
  }
}