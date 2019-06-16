package com.snubbull.app.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import com.snubbull.app.R;

public class AcceptedNotice extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_accepted_notice);

    Button startAgainButton = findViewById(R.id.StartAgainButton);
    startAgainButton.setOnClickListener(v -> {
      Intent createStartView = new Intent(getApplicationContext(), StartView.class);
      startActivity(createStartView);
    });
  }
}
