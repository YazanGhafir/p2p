package com.snubbull.app.view;

import android.app.DatePickerDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import com.snubbull.app.R;
import com.snubbull.app.model.AbstractNotice;
import com.snubbull.app.model.SenderNotice;
import com.snubbull.app.model.TransporterNotice;
import com.snubbull.app.viewmodel.NoticeViewModel;
import java.util.Calendar;

public class MatchView extends AppCompatActivity implements OnPlaceUpdatedListener {

  private NoticeViewModel viewModel;

  private String from = "";
  private String to = "";
  private String date = "";

  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    setContentView(R.layout.filter_view);
    viewModel = ViewModelProviders.of(this).get(NoticeViewModel.class);


    Button btnDate = findViewById(R.id.inputfieldDate);
    btnDate.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR) + 1900;

        DatePickerDialog dtp = new DatePickerDialog(MatchView.this
            , new DatePickerDialog.OnDateSetListener() {
          @Override
          public void onDateSet(DatePicker datePicker, int mYear, int mMonth, int mDay) {

            date =
                "" + mYear + "-" + (mMonth / 10) + "" + ((1 + mMonth) % 10) + "-" + (mDay / 10) + ""
                    + (mDay % 10);
            btnDate.setText(date);
          }
        }, day, month, year);
        dtp.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        dtp.show();
      }
    });

    Button matchButton = findViewById(R.id.match_button);
    matchButton.setOnClickListener(v -> {
      Intent createListView = new Intent(getApplicationContext(), NoticeView.class);
      if (getIntent().hasExtra("Sender")) {
        createListView.putExtra("senderNotice", new SenderNotice());

      } else {
        createListView.putExtra("transporterNotice", new TransporterNotice());
      }
      startActivity(createListView);
    });


  }

  private void updateLocation() {
    if (!from.isEmpty() && !to.isEmpty()) {
      NoticeViewModel viewModel = ViewModelProviders.of(this).get(NoticeViewModel.class);
      viewModel.setFilterNotice(new AbstractNotice() {
        @Override
        public String getFrom() {

          return from;
        }

        @Override
        public String getTo() {
          return to;
        }

      });
    }
  }

  @Override
  public void onFromUpdated(String newFrom) {
    from = newFrom;
    updateLocation();
  }

  @Override
  public void onToUpdated(String newTo) {
    to = newTo;
    updateLocation();
  }
}

