package com.snubbull.app.view;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.snubbull.app.R;
import com.snubbull.app.model.AbstractNotice;
import com.snubbull.app.model.SenderNotice;
import com.snubbull.app.model.TransporterNotice;

public class NoticeInfoView extends AppCompatActivity {
  private static final int PERMISSION_REQUEST_CODE = 1;
  String wantPermission = Manifest.permission.CALL_PHONE;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.notice_info_view);

    LinearLayout mainLayout = findViewById(R.id.noticeinfo);

    if (getIntent().hasExtra("senderNotice")) {
      AbstractNotice notice = (AbstractNotice) getIntent().getSerializableExtra("senderNotice");

      if (notice.getClass() == TransporterNotice.class) {
        View view = getLayoutInflater().inflate(R.layout.transport_notice_info, mainLayout, false);

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
        ((Button)view.findViewById(R.id.callButton)).setOnClickListener(new OnClickListener() {
          @Override
          public void onClick(View v) {
            if (!checkPermission(wantPermission)) {
              requestPermission(wantPermission);
            } else {
              Intent callIntent = new Intent(Intent.ACTION_CALL);
              callIntent.setData(Uri.parse("tel:"+notice.getPhone()));
              startActivity(callIntent);
            }
          }
        });
        fromView.setText(notice.getFrom());
        deliveryDateView.setText(notice.getDeliveryDate());
        capacityView.setText(notice.getCapacity().toString());
        priceView.setText("" + notice.getPrice());
        meansOfTransportView.setText("" + ((TransporterNotice) notice).getMeansOfTransport());

        mainLayout.addView(view);
      } else if (notice.getClass() == SenderNotice.class) {
        View view = getLayoutInflater().inflate(R.layout.sender_notice_info, mainLayout, false);

        final TextView typeView = view.findViewById(R.id.noticeTypeItem);
        final TextView toView = view.findViewById(R.id.toItem);
        final TextView accountIdView = view.findViewById(R.id.accountIdItem);

        ((Button)view.findViewById(R.id.callButton)).setOnClickListener(new OnClickListener() {
          @Override
          public void onClick(View v) {
            if (!checkPermission(wantPermission)) {
              requestPermission(wantPermission);
            } else {
              Intent callIntent = new Intent(Intent.ACTION_CALL);
              callIntent.setData(Uri.parse("tel:"+notice.getPhone()));
              startActivity(callIntent);
            }
          }
        });

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
      }

    }

  }

  private String getPhone() {
    TelephonyManager phoneMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
    if (ActivityCompat.checkSelfPermission(NoticeInfoView.this, wantPermission)
        != PackageManager.PERMISSION_GRANTED) {
      return "";
    }
    return phoneMgr.getLine1Number();
  }

  /*
  Evaluate whether we should show permission rationale UI and if so, show information text.
  Lastly, the function calls requestPermissions() which prompt the user for the permission that is needed.
   */
  private void requestPermission(String permission) {
    if (ActivityCompat.shouldShowRequestPermissionRationale(NoticeInfoView.this, permission)) {
      Toast.makeText(NoticeInfoView.this,
          "Phone state permission allows us to get phone number. Please allow it for additional functionality.",
          Toast.LENGTH_LONG).show();
    }
    ActivityCompat.requestPermissions(NoticeInfoView.this, new String[]{permission},
        PERMISSION_REQUEST_CODE);

  }

  /*
  Callback for the result from requesting permissions.
  This method is invoked for every call on requestPermissions(android.app.Activity, String[], int).
   */
  @Override
  public void onRequestPermissionsResult(int requestCode, String[] permissions,
      int[] grantResults) {
    switch (requestCode) {
      case PERMISSION_REQUEST_CODE:
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        } else {
          Toast.makeText(NoticeInfoView.this, "Permission Denied. We can't get phone number.",
              Toast.LENGTH_LONG).show();
        }
        break;
    }
  }

  /*
  Determine whether you have been granted READ_PHONE_STATE permission
   */
  private boolean checkPermission(String permission) {
    if (Build.VERSION.SDK_INT >= 23) {
      int result = ContextCompat.checkSelfPermission(NoticeInfoView.this, permission);
      return result == PackageManager.PERMISSION_GRANTED;
    } else {
      return true;
    }
  }

}
