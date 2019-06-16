package com.snubbull.app.view;



import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.snubbull.app.R;
import com.snubbull.app.model.AbstractNotice;
import com.snubbull.app.model.SenderNotice;
import com.snubbull.app.model.TransporterNotice;
import com.snubbull.app.viewmodel.NoticeViewModel;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;

public class OwnNoticeInfoView extends AppCompatActivity {

  private NoticeViewModel viewModel;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.own_notice_info_view);
    LinearLayout mainLayout = findViewById(R.id.ownnoticeinfo);
    viewModel = ViewModelProviders.of(this).get(NoticeViewModel.class);

    if (getIntent().hasExtra("senderNotice")) {
      AbstractNotice notice = (AbstractNotice) getIntent().getSerializableExtra("senderNotice");

      if (notice.getClass() == TransporterNotice.class) {
        View view = getLayoutInflater()
            .inflate(R.layout.own_transport_notice_info, mainLayout, false);

        final TextView typeView = view.findViewById(R.id.noticeTypeItem);
        final TextView toView = view.findViewById(R.id.toItem);
        final TextView accountIdView = view.findViewById(R.id.accountIdItem);
        final TextView fromView = view.findViewById(R.id.fromItem);
        final TextView deliveryDateView = view.findViewById(R.id.dateItem);
        final TextView capacityView = view.findViewById(R.id.capacityItem);
        final TextView priceView = view.findViewById(R.id.priceItem);
        final TextView meansOfTransportView = view.findViewById(R.id.meansItem);

        typeView.setText("Transporter");
        toView.setText(notice.getTo());
        accountIdView.setText("" + notice.getPhone());
        fromView.setText(notice.getFrom());
        deliveryDateView.setText(notice.getDeliveryDate());
        capacityView.setText(notice.getCapacity().toString());
        priceView.setText("" + notice.getPrice());
        meansOfTransportView.setText("" + ((TransporterNotice) notice).getMeansOfTransport());

        mainLayout.addView(view);

        final Button deleteButton = view.findViewById(R.id.deleteTbutton);
        deleteButton.setOnClickListener(v -> {
          Call<List<TransporterNotice>> call = viewModel.getService().deleteTransporterNotice(notice.getId());
          Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
              try {
                call.execute().body();
              } catch (IOException e) {
                e.printStackTrace();
              }
              viewModel.updateRepository();
              Intent transporterDeleted = new Intent(getApplicationContext(), PopupActivity.class);
              transporterDeleted.putExtra("DeletedTransporterNotice", true);
              startActivity(transporterDeleted);
            }
          });thread.start();

        });

      } else if (notice.getClass() == SenderNotice.class) {
        View view = getLayoutInflater().inflate(R.layout.own_sender_notice_info, mainLayout, false);

        final TextView typeView = view.findViewById(R.id.noticeTypeItem);
        final TextView toView = view.findViewById(R.id.toItem);
        final TextView accountIdView = view.findViewById(R.id.accountIdItem);
        final TextView fromView = view.findViewById(R.id.fromItem);
        final TextView deliveryDateView = view.findViewById(R.id.dateItem);
        final TextView capacityView = view.findViewById(R.id.capacityItem);
        final TextView priceView = view.findViewById(R.id.priceItem);

        final TextView quantityView = view.findViewById(R.id.meansItem);

        toView.setText(notice.getTo());
        typeView.setText("Sender");
        accountIdView.setText("" + notice.getPhone());
        fromView.setText(notice.getFrom());
        deliveryDateView.setText(notice.getDeliveryDate());
        capacityView.setText(notice.getCapacity().toString());
        priceView.setText("" + notice.getPrice());
        quantityView.setText("" + ((SenderNotice) notice).getQuantity());

        mainLayout.addView(view);


        final Button deleteButton = view.findViewById(R.id.deleteSbutton);
        deleteButton.setOnClickListener(v -> {
          Call<List<SenderNotice>> call = viewModel.getService().deleteSenderNotice(notice.getId());
          Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
              try {
                call.execute().body();
              } catch (IOException e) {
                e.printStackTrace();
              }
              viewModel.updateRepository();
              Intent senderDeleted = new Intent(getApplicationContext(), PopupActivity.class);
              senderDeleted.putExtra("DeletedSenderNotice", true);
              startActivity(senderDeleted);
            }
          });thread.start();

        });
      }

    }
  }

}