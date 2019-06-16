package com.snubbull.app.view;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Toast;
import com.snubbull.app.R;
import com.snubbull.app.model.AndroidID;
import com.snubbull.app.repository.Connected;
import com.snubbull.app.repository.Ping;
import com.snubbull.app.viewmodel.NoticeViewModel;

public class StartView extends AppCompatActivity implements Connected {

  private NoticeViewModel viewModel;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    viewModel = ViewModelProviders.of(this).get(NoticeViewModel.class);
    //pingpong();
    viewModel.updateRepository();
    onBackPressed();

    Button senderButton = findViewById(R.id.senderButton);
    senderButton.setOnClickListener(v -> {
      viewModel.updateRepository();
      Intent createSenderFilter = new Intent(getApplicationContext(), MatchView.class);
      createSenderFilter.putExtra("Sender", "Sender");
      startActivity(createSenderFilter);
    });

    Button transp = findViewById(R.id.BtnTransport);
    transp.setOnClickListener(v -> {
      viewModel.updateRepository();
      Intent createTransporterFilter = new Intent(getApplicationContext(), MatchView.class);
      createTransporterFilter.putExtra("Transport", "Transporter");
      startActivity(createTransporterFilter);
    });

    Button myNotices = findViewById(R.id.BtnNotices);
    myNotices.setOnClickListener(v -> {
      viewModel.updateRepository();
      Intent createOwnNoticeView = new Intent(getApplicationContext(), OwnNoticesView.class);
      startActivity(createOwnNoticeView);
    });

    androidIDinit();
  }

  /**
   * initializing the application with the device ID
   */
  protected void androidIDinit() {
    try {
      AndroidID.setAndroidID(Settings.Secure
          .getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID));
      if (AndroidID.getAndroidID().equals("")) {
        Toast.makeText(getApplicationContext(),
            "No Android ID could be loaded, Use the app on another android device",
            Toast.LENGTH_LONG).show();
      }
    } catch (Exception e) {
      Toast.makeText(getApplicationContext(),
          "No Android ID could be loaded, Use the app on another android device", Toast.LENGTH_LONG)
          .show();
      System.exit(0);
    }
  }

  @Override
  public void onBackPressed() {
  }

  /**
   * Checking the connection to the server by sending ping and receiving pong in parallel when the
   * program run
   */
  @Override
  public void pingpong() {
    Ping.checkConnection(getApplicationContext(), Toast.LENGTH_LONG);
  }

}