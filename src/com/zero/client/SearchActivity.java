package com.zero.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SearchActivity extends Activity 
{
	EditText input;
    String target;
    Button okBtn;
    TextView response;
    
    Socket socket;
    
    THUClient the_app;
    
    Handler handler;
    
    class NetThread implements Runnable
    {
		@Override
		public void run() 
		{
			try 
			{
				PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
				out.println("4 " + target);
				
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				String ans = in.readLine();
				
				if(ans != null)
				{
					Message msg = new Message();
					msg.obj = ans;
					SearchActivity.this.handler.sendMessage(msg);
				}
				
			} catch (IOException e) 
			{
				Message msg = new Message();
				msg.obj = "net error";
				SearchActivity.this.handler.sendMessage(msg);
			}
			
		}
    	
    }
    
    
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_search);
		
		the_app = (THUClient) getApplicationContext();
		socket = the_app.socket;
		
		input = (EditText) findViewById(R.id.nameInputID);
		okBtn = (Button) findViewById(R.id.okBtn);
		response = (TextView) findViewById(R.id.responseID);
		
		handler = new Handler()
		{
			public void handleMessage(Message msg)
			{
				String words = (String)msg.obj;
				if(words.equals("-1"))
				{
					response.setText("没有找到这个人……");
				}
				else if( words.equals("net error") )
				{
					Toast.makeText(SearchActivity.this,"网络连接好像有问题", Toast.LENGTH_SHORT).show();
					finish();
				}
				else if( words.equals("1"))
				{
					response.setText("找到了！！\n这个人在紫荆食堂！");
				}
				else if( words.equals("2"))
				{
					response.setText("找到了！！\n这个人在桃李食堂！");
				}
				else if( words.equals("3") )
				{
					response.setText("找到了！！\n这个人在美术学院A区！");
				}
					
			}
		};
		
		okBtn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0) 
			{
				target = input.getText().toString();
				System.out.println(target);
				NetThread netThread = new NetThread();
				new Thread(netThread).start();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search, menu);
		return true;
	}

}
