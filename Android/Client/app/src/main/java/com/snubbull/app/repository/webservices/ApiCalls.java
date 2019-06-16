package com.snubbull.app.repository.webservices;

import com.snubbull.app.model.SenderNotice;
import com.snubbull.app.model.TransporterNotice;
import com.snubbull.app.repository.Ping;

import java.util.List;
import java.util.UUID;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiCalls {

  /**
   * this method sends an Http GET request with Ping to the server
   *
   * @return Pong from the server if the connection is established, null if not.
   */
  @POST("/api/vi/pingpong")
  Call<Ping> ping(@Body Ping ping);

  /**
   * this method sends an Http GET request to the server
   *
   * @return a list of sender notices from the server
   */
  @GET("/api/vi/sendernotice")
  Call<List<SenderNotice>> getSenderNotices();

  /**
   * this method sends an Http GET request to the server
   *
   * @return a list of transporter notices from the server
   */
  @GET("/api/vi/transporternotice")
  Call<List<TransporterNotice>> getTransporterNotices();

  /**
   * this method sends an Http POST request to the server
   *
   * @return a list of sender notices from the server
   */
  @POST("/api/vi/sendernotice")
  Call<List<SenderNotice>> createSenderNotice(@Body SenderNotice notice);

  /**
   * this method sends an Http POST request to the server
   *
   * @return a list of transporter notices from the server
   */
  @POST("/api/vi/transporternotice")
  Call<List<TransporterNotice>> createTransporterNotice(@Body TransporterNotice notice);

  /**
   * this method sends an Http DELETE request to the server
   *
   * @param id the id of notice that will be deleted
   * @return updated sender List after deleting
   */
  @DELETE("/api/vi/sendernotice/{id}")
  Call<List<SenderNotice>> deleteSenderNotice(@Path("id") UUID id);

  /**
   * this method sends an Http DELETE request to the server
   *
   * @param id the id of notice that will be deleted
   * @return updated transporter notice List after deleting
   */
  @DELETE("/api/vi/transporternotice/{id}")
  Call<List<TransporterNotice>> deleteTransporterNotice(@Path("id") UUID id);

  /**
   * this method sends an Http PUT request to the server
   *
   * @return updated sender List after updating the notice
   */
  @PUT("/api/vi/sendernotice/{id}")
  Call<List<SenderNotice>> updateSenderNotice(@Body SenderNotice notice, @Path("id") UUID id);

  /**
   * this method sends an Http PUT request to the server
   *
   * @returnupdated transporter notice List after updating the notice
   */
  @PUT("/api/vi/transporternotice/{id}")
  Call<List<TransporterNotice>> updateTransporterNotice(@Body TransporterNotice notice,
      @Path("id") UUID id);

  /**
   * Sends an HTTP DELETE request to the server to delete all notices (sender- and transporter notices).
   */
  @DELETE("/api/vi/notice")
  Call<Integer> deleteAllNotices();
}
