package com.snubbull.app.view;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.snubbull.app.R;
import com.snubbull.app.model.AbstractNotice;
import com.snubbull.app.model.AndroidID;
import com.snubbull.app.model.Notice;
import com.snubbull.app.model.SenderNotice;
import com.snubbull.app.model.TransporterNotice;
import com.snubbull.app.model.Utility;
import com.snubbull.app.model.Utility.MeansOfTransport;
import com.snubbull.app.repository.NoticeRepository;
import com.snubbull.app.viewmodel.NoticeViewModel;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class OwnNoticesView extends AppCompatActivity {

  private NoticeViewModel viewModel;
  private ListView listView;
  List<Notice> ownNotices;
  NoticeRepository repo = NoticeRepository.getNoticeRepository();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_own_notices_view);
    listView = findViewById(R.id.noticeList);
    ownNotices = repo.getOwnNotices();


    viewModel = ViewModelProviders.of(this).get(NoticeViewModel.class);
    onNoticeUpdate();

    listView.setOnItemClickListener((parent, view, position, id) -> {

      AbstractNotice notice = (AbstractNotice) parent.getItemAtPosition(position);

      Intent openOwnNoticeInfo = new Intent(getApplicationContext(), OwnNoticeInfoView.class);
      openOwnNoticeInfo.putExtra("senderNotice", notice);
      //send notice object to the next view
      startActivity(openOwnNoticeInfo);
    });

  }

  private void onNoticeUpdate() {
    ownNotices = repo.getOwnNotices();
    if (listView.getAdapter() != null) {
      ArrayAdapter<Notice> adapter = (ArrayAdapter<Notice>) listView.getAdapter();
      adapter.clear();
      adapter.addAll(ownNotices);
    } else {
      NoticeAdapter adapter = new NoticeAdapter<>(this, R.layout.list_element, ownNotices);
      listView.setAdapter(adapter);
      adapter.notifyDataSetChanged();
    }
  }

}


