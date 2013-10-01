package com.social.gamerpoint;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Activity_Shop_List extends Activity {
	TextView textView_title, textView_artist, textView_time, textView_price;
	ImageView imageView_image;
	Button button_close;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.custom_shop_list);
		textView_title = (TextView) findViewById(R.id.textView_title);
		textView_artist = (TextView) findViewById(R.id.textView_artist);
		textView_time = (TextView) findViewById(R.id.textView_time);
		textView_price = (TextView) findViewById(R.id.textView_price);
		imageView_image = (ImageView) findViewById(R.id.imageView_profile_pic);
		textView_title.setText(getIntent().getStringExtra("title"));
		textView_price.setText(getIntent().getStringExtra("price"));
		textView_artist.setText(getIntent().getStringExtra("artist"));
		textView_time.setText(getIntent().getStringExtra("time"));
		imageView_image.setImageResource(getIntent().getIntExtra("image_path",
				0));
		button_close = (Button) findViewById(R.id.button_close);
		button_close.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

	}

}
