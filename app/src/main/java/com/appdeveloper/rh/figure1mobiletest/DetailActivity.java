package com.appdeveloper.rh.figure1mobiletest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class DetailActivity extends AppCompatActivity {

    ImageObject obj;
    ImageView detailIV;
    TextView titleTV;
    ImageButton shareBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        obj = getIntent().getParcelableExtra("theObject");

        detailIV = (ImageView) findViewById(R.id.detailImageView);
        titleTV = (TextView) findViewById(R.id.titleTextView);
        shareBtn = (ImageButton) findViewById(R.id.shareBtn);

        Glide.with(this).load("https://i.imgur.com/" +
                obj.id + ".jpg").into(detailIV);
        titleTV.setText(obj.title);

        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareInfo = "Here is something you should check out: " + "https://i.imgur.com/" +
                        obj.id + ".jpg";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Share Image");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareInfo);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });
    }
}
