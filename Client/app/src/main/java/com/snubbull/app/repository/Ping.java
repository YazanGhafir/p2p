package com.snubbull.app.repository;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.snubbull.app.repository.webservices.ServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Ping {

  @SerializedName("ping")
  @Expose
  public static String ping;

  public static boolean CONNECTION = false;


  Ping() {

  }

  Ping(String pingٍ) {
    ping = pingٍ;
  }

  private static int pingsend() {

    Call<Ping> call = ServiceGenerator.getInstance().getService().ping(new Ping("ping"));
    final int[] status = new int[1];
    status[0] = 1;
    call.enqueue(new Callback<Ping>() {
      @Override
      public void onResponse(Call<Ping> call, Response<Ping> response) {
        if (response.isSuccessful() || response.body().equals("pong")) {
          CONNECTION = true;
          System.out.println("connected!!!!!!!!!!!!!!!!!!!!!!!!");
          status[0] = 1;
        } else {
          CONNECTION = false;
          Log.e("Ping problem!!! ", response.message());
          status[0] = 0;
        }
      }

      @Override
      public void onFailure(Call<Ping> call, Throwable t) {
        Log.e("Ping problem!!!", t.getMessage());
      }
    });
    return status[0];
  }

  public static void checkConnection(Context c, int ll) {

    if (pingsend() == 1) {
      CONNECTION = true;
      System.out.println("COOOOOOOOONNNNNNEEEEEEEEEEEECTTTTTTTTTTEEEEEEEEEDDDDDDDDDDDDD");
    } else {
      CONNECTION = false;
    }

    if (!Ping.CONNECTION) {
      Toast.makeText(c, "No connection to the Server", ll).show();
    } else if (Ping.CONNECTION) {
      Toast.makeText(c, "Connected to the Server", ll).show();
    }
  }
}
