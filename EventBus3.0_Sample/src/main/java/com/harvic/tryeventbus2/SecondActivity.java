package com.harvic.tryeventbus2;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.harvic.other.FirstEvent;
import com.harvic.other.SecondEvent;
import com.harvic.other.ThirdEvent;

import org.greenrobot.eventbus.EventBus;

public class SecondActivity extends Activity {
	private Button btn_FirstEvent, btn_SecondEvent, btn_ThirdEvent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_second);
		btn_FirstEvent = (Button) findViewById(R.id.btn_first_event);
		btn_SecondEvent = (Button) findViewById(R.id.btn_second_event);
		btn_ThirdEvent = (Button) findViewById(R.id.btn_third_event);

		btn_FirstEvent.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				EventBus.getDefault().post(
						new FirstEvent("FirstEvent 阿福"));
			}
		});
		
		btn_SecondEvent.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				EventBus.getDefault().post(
						new SecondEvent("SecondEvent 硅谷"));
			}
		});

		btn_ThirdEvent.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				EventBus.getDefault().post(
						new ThirdEvent("ThirdEvent btn clicked"));

			}
		});

	}

}
