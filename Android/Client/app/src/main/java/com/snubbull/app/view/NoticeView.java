package com.snubbull.app.view;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import com.snubbull.app.R;
import com.snubbull.app.model.AbstractNotice;
import com.snubbull.app.model.Notice;
import com.snubbull.app.model.SenderNotice;
import com.snubbull.app.model.TransporterNotice;
import com.snubbull.app.model.Utility.MeansOfTransport;
import com.snubbull.app.viewmodel.NoticeViewModel;
import java.util.List;


public class NoticeView extends AppCompatActivity {

  private NoticeViewModel viewModel;
  private ListView listView;
  private Spinner filterMoTSpinner;


  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {

    setContentView(R.layout.notice_list);
    super.onCreate(savedInstanceState);


    listView = findViewById(R.id.noticeList);
    filterMoTSpinner = findViewById(R.id.filterMoT);
    fillSpinner();
    //  String listID = getArguments().getString(UID_KEY);;

    viewModel = ViewModelProviders.of(this).get(NoticeViewModel.class);

    /*
    if (getIntent().hasExtra("senderNotice")){
      Notice senderNotice = (Notice) getIntent().getSerializableExtra("senderNotice");
      viewModel.addNotice(senderNotice);
*/
    if (getIntent().hasExtra("senderNotice")) {
      SenderNotice senderNotice = (SenderNotice) getIntent().getSerializableExtra("senderNotice");
//viewModel.addSenderNotice(senderNotice);
      viewModel.observeTransporterNotices(this, this::onNoticeUpdate);
      //viewModel.setFilterNotice(senderNotice); // Temp fix
    } else if (getIntent().hasExtra("transporterNotice")) {
      TransporterNotice transporterNotice = (TransporterNotice) getIntent()
          .getSerializableExtra("transporterNotice");
//viewModel.addTransporterNotice(transporterNotice);
      viewModel.observeSenderNotices(this, this::onNoticeUpdate);
      //viewModel.setFilterNotice(transporterNotice); // Temp fix
      filterMoTSpinner.setVisibility(View.GONE);
    }

    //Just update the view. no needed inputs from a previous activity
    //viewModel.observeTransporterNotices(this, this::onNoticeUpdate);
    //viewModel.observeSenderNotices(this, this::onNoticeUpdate);

    listView.setOnItemClickListener((parent, view, position, id) -> {

      AbstractNotice notice = (AbstractNotice) parent.getItemAtPosition(position);
      notice.getFrom();

      Intent openNoticeInfo = new Intent(getApplicationContext(), NoticeInfoView.class);
     openNoticeInfo.putExtra("senderNotice", notice);
      //send notice object to the next view
      startActivity(openNoticeInfo);

    });

    //Toolbar toolbar = findViewById(R.id.toolbar);
    //setSupportActionBar(toolbar);

    Button postButton = findViewById(R.id.postingBtn);
    postButton.setOnClickListener(v -> {

      if (getIntent().hasExtra("senderNotice")) {
        SenderNotice senderNotice = (SenderNotice) getIntent().getSerializableExtra("senderNotice");
        Intent createSenderNotice = new Intent(getApplicationContext(), SenderNoticeSheet.class);
        startActivity(createSenderNotice);
        //viewModel.addSenderNotice(senderNotice);
      } else if (getIntent().hasExtra("transporterNotice")) {
        TransporterNotice transporterNotice = (TransporterNotice) getIntent()
            .getSerializableExtra("transporterNotice");
        Intent createTransportNotice = new Intent(getApplicationContext(),
            TransportNoticeSheet.class);
        startActivity(createTransportNotice);
        //viewModel.addTransporterNotice(transporterNotice);
      }
    });

  }

  private void fillSpinner() {
    ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this,
        R.layout.support_simple_spinner_dropdown_item);
    spinnerAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
    filterMoTSpinner.setAdapter(spinnerAdapter);
    String[] options = new String[]{"Any", "Bus", "Car", "Train"};
    spinnerAdapter.addAll(options);
    spinnerAdapter.notifyDataSetChanged();

    filterMoTSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        MeansOfTransport mot;
        if (position == 0) {
          mot = null;
        } else {
          mot = MeansOfTransport.getMeansOfTransport(options[position]);
        }
        viewModel.setMeansOfTransport(mot);
      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {

      }
    });
  }

  private void onNoticeUpdate(List<? extends Notice> notices) {
    if (listView.getAdapter() != null) {
      ArrayAdapter<Notice> adapter = (ArrayAdapter<Notice>) listView.getAdapter();
      adapter.clear();
      adapter.addAll(notices);
    } else {
      NoticeAdapter adapter = new NoticeAdapter<>(this, R.layout.list_element, notices);
      listView.setAdapter(adapter);
      adapter.notifyDataSetChanged();
    }
  }
}