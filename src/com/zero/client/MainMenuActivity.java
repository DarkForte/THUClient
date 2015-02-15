package com.zero.client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class MainMenuActivity extends Activity
{
	ImageButton loginBtn;
	ImageButton askBtn;
	ImageButton statusBtn;
	ImageButton numberBtn;
	ImageButton searchBtn;
	ImageButton stopBtn;
	
	THUClient the_app;
	
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_mainmenu);
		
		loginBtn = (ImageButton)findViewById(R.id.loginBtn);
		askBtn = (ImageButton) findViewById(R.id.askBtn);
		statusBtn = (ImageButton) findViewById(R.id.statusBtn);
		numberBtn = (ImageButton) findViewById(R.id.numberBtn);
		searchBtn = (ImageButton) findViewById(R.id.searchBtn);
		stopBtn = (ImageButton) findViewById(R.id.stopBtn);
		
		the_app = (THUClient) getApplicationContext();
		
		loginBtn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(MainMenuActivity.this, LoginActivity.class);
				MainMenuActivity.this.startActivity(intent);
			}
		});
		
		askBtn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) {
				
				if(the_app.socket == null)
				{
					Toast.makeText(MainMenuActivity.this,"网络连接好像有问题", Toast.LENGTH_SHORT).show();
				}
				else
				{
					Intent intent = new Intent();
					intent.setClass(MainMenuActivity.this, AskInputActivity.class);
					MainMenuActivity.this.startActivity(intent);
				}
			}
		});
		
		statusBtn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) {
				
				if(the_app.socket == null)
				{
					Toast.makeText(MainMenuActivity.this,"网络连接好像有问题", Toast.LENGTH_SHORT).show();
				}
				else
				{
					Intent intent = new Intent();
					intent.setClass(MainMenuActivity.this, StatusActivity.class);
					MainMenuActivity.this.startActivity(intent);
				}
			}
		});
		
		numberBtn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) {
				
				if(the_app.socket == null)
				{
					Toast.makeText(MainMenuActivity.this,"网络连接好像有问题", Toast.LENGTH_SHORT).show();
				}
				else
				{
					Intent intent = new Intent();
					intent.setClass(MainMenuActivity.this, ShowNumberActivity.class);
					MainMenuActivity.this.startActivity(intent);
				}
			}
		});
		
		searchBtn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) {
				
				if(the_app.socket == null)
				{
					Toast.makeText(MainMenuActivity.this,"网络连接好像有问题", Toast.LENGTH_SHORT).show();
				}
				else
				{
					Intent intent = new Intent();
					intent.setClass(MainMenuActivity.this, SearchActivity.class);
					MainMenuActivity.this.startActivity(intent);
				}
			}
		});
		
		stopBtn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) {
				
				if(the_app.socket == null)
				{
					Toast.makeText(MainMenuActivity.this,"网络连接好像有问题", Toast.LENGTH_SHORT).show();
				}
				else
				{
					Intent intent = new Intent();
					intent.setClass(MainMenuActivity.this, StopActivity.class);
					MainMenuActivity.this.startActivity(intent);
				}
			}
		});
	}
}







