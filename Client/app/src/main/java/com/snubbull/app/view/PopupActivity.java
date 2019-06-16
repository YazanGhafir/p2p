package com.snubbull.app.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.snubbull.app.R;
import com.snubbull.app.model.AbstractNotice;
import com.snubbull.app.model.TransporterNotice;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;

public class PopupActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        Button closeButton = (Button) findViewById(R.id.closeButton);
        TextView textView = (TextView) findViewById(R.id.textView2);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.6), (int)(height*.5));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;

        getWindow().setAttributes(params);
        closeButton.setOnClickListener(v -> {
            Intent startView = new Intent(getApplicationContext(), StartView.class);
            startActivity(startView);
            /*
            if (getIntent().hasExtra("senderNotice")) {
                Intent ownNoticeList = new Intent(getApplicationContext(), OwnNoticesView.class);
                ownNoticeList.putExtra("senderNotice", "senderNotice");
                startActivity(ownNoticeList);
            } else {
                Intent ownNoticeList = new Intent(getApplicationContext(), OwnNoticesView.class);
                ownNoticeList.putExtra("transporterNotice", "transporterNotice");
                startActivity(ownNoticeList);
            }
            */

            });


    }
}
