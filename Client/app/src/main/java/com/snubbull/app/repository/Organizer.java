package com.snubbull.app.repository;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.annimon.stream.function.DoublePredicate;
import com.annimon.stream.function.Function;
import com.snubbull.app.model.AndroidID;
import com.snubbull.app.model.Notice;
import com.snubbull.app.model.Priced;
import com.snubbull.app.model.Route;
import com.snubbull.app.model.Utility;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public final class Organizer<E> {

  private Stream<E> stream;

  /**
   * This class is not meant to be initialized through a Constructor. Use {@link
   * Organizer#createOrganizer(Collection)} instead.
   */
  private Organizer() {
  }

  private Organizer(Stream<E> stream) {
    this.stream = stream;
  }

  /**
   * Creates an organizer that will be used to organize a specified collection of elements.
   *
   * @param elements The collection of elements
   * @return The organizer set to organize the collection of items
   */
  public static <T> Organizer<T> createOrganizer(Collection<? extends T> elements) {
    Organizer<T> organizer = new Organizer<>(Stream.of(elements));
    return organizer;
  }

  /**
   * Filters a collection of routes by the from and to destinations. Leaving any parameter blank
   * selects all of that parameter.
   *
   * @param mapper Maps the collection of Es to a collection of Routes
   * @param from The origin location
   * @param to The destination
   * @return An intermediate organizer where its collection has been filtered by specified route
   */
  public <T extends Route> Organizer<T> filterByRoute(Function<? super E, ? extends T> mapper,
      String from, String to) {
    Organizer<T> organizer = new Organizer<>(stream.map(mapper));
    organizer.stream = organizer.stream
        .filter(x -> "".equals(from.replaceAll(" ", "")) || from.equals(x.getFrom()))
        .filter(x -> "".equals(to.replaceAll(" ", "")) || to.equals(x.getTo()));
    return organizer;
  }

  /**
   * Filters a collection of Priced by its price, specified by predicate.
   * <br><br>
   * Usage example: <br>
   * <code>filterByPrice(el -> el, price -> price < 100);</code><br>
   * In this example the consecutive types used are the same, so the mapper is the identity
   * function. The predicate given selects all Priced elements with a price less than 100.
   *
   * @param mapper Maps the collection of Es to a collection of Priced
   * @param predicate The predicate with which to select elements from the collection. It is a
   * function where the parameter is the price.
   * @param <T> A Priced item
   * @return An intermediate organizer where its collection has been filtered by specified filter.
   */
  public <T extends Priced> Organizer<T> filterByPrice(Function<? super E, ? extends T> mapper,
      DoublePredicate predicate) {

    Organizer<T> organizer = new Organizer<>();
    organizer.stream = this.stream.map(mapper);
    organizer.stream = organizer.stream.filter(x -> predicate.test(x.getPrice()));
    return organizer;
  }
  /**
   * Orders a collection of Priced elements by price.
   *
   * @param mapper Maps the collection of Es to a collection of the same android device ID
   * @param deviceID The andoid ID to be compared with.
   * @param <T> An item with an ID.
   * @return An intermediate organizer where the elements have been ordered by matching deviceID.
   */

  public <T extends Notice> Organizer<T> filterByID(Function<? super E, ? extends T> mapper,String deviceID) {

    Organizer<T> organizer = new Organizer<>();
    organizer.stream = this.stream.map(mapper);
    organizer.stream = organizer.stream.filter((x)-> (x.getDeviceId() != (null))).filter(x -> x.getDeviceId().equals(deviceID));
    return organizer;
  }

  /**
   * Orders a collection of Priced elements by price.
   *
   * @param mapper Maps the collection of Es to a collection of Priced objects
   * @param order The order to order the items in.
   * @param <T> A priced item.
   * @return An intermediate organizer where the elements have been ordered by price.
   */
  public <T extends Priced> Organizer<T> orderByPrice(Function<? super E, ? extends T> mapper,
      Order order) {
    Organizer<T> organizer = new Organizer<>(stream.map(mapper));
    organizer.stream = organizer.stream.sortBy(x -> x.getPrice() * order.order);
    return organizer;
  }

  /**
   * Filters a collection of Transported elements by their means of transport. The means of
   * transport that should be retained in the collection is specified by meansOfTransport.
   *
   * @param mapper Maps the collection of Es to a collection of Priced objects
   * @param meansOfTransport The means of transport the selected elements should have in the
   * filtered collection
   * @param <T> An item that is transported.
   * @return A collection of all elements in the original collection that have a specified means of
   * transport.
   */
  public <T extends Transported> Organizer<T> filterByMeansOfTransport(
      Function<? super E, ? extends T> mapper, Utility.MeansOfTransport meansOfTransport) {
    Organizer<T> organizer = new Organizer<>(stream.map(mapper));
    organizer.stream = organizer.stream.filter(x -> x.getMeansOfTransport() == meansOfTransport);
    return organizer;
  }

  /**
   * A terminal operation that returns the fully organized collection as a collection of E's.
   *
   * @return An organized collection.
   */
  public List<E> getElements() {
    return stream.collect(Collectors.toCollection(LinkedList::new));
  }

  public enum Order {
    ASCENDING(1),
    DESCENDING(-1);

    private final int order;

    Order(int order) {
      this.order = order;
    }
  }
}
