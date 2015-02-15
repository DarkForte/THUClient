package com.zero.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.NoSuchElementException;
import java.util.Scanner;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.view.View.OnClickListener; 

public class LoginActivity extends Activity 
{
	ImageButton btn;
	EditText editText;
	//EditText receiveBox;
	
	//SendThread sendThread;
	GetThread getThread;
	
	Handler handler;
	
	boolean stopGet=false;
	
	THUClient the_app;
	
	class GetThread implements Runnable
	{
		public void getMsg()
		{
			try 
			{
				Scanner in = new Scanner(the_app.socket.getInputStream());
				String gotmsg = in.nextLine();
				Message msg = new Message();
				msg.obj = gotmsg;
				LoginActivity.this.handler.sendMessage(msg);
				
			} catch (UnknownHostException e) 
			{
				Message msg = new Message();
				msg.obj = "net error";
				LoginActivity.this.handler.sendMessage(msg);
			} catch (IOException e) 
			{
				Message msg = new Message();
				msg.obj = "net error";
				LoginActivity.this.handler.sendMessage(msg);
			}
			
		}
		
		@Override
		public void run() 
		{
			try 
			{
				the_app.socket = new Socket();
				the_app.socket.connect(new InetSocketAddress(the_app.ip, the_app.port) , 5000);
			} catch (UnknownHostException e) 
			{
				Message msg = new Message();
				msg.obj = "did not login";
				LoginActivity.this.handler.sendMessage(msg);
			} catch (IOException e) {
				Message msg = new Message();
				msg.obj = "did not login";
				LoginActivity.this.handler.sendMessage(msg);
			}
			
			if( the_app.socket != null)
				getMsg();
			else
			{
				Message msg = new Message();
				msg.obj = "did not login";
				LoginActivity.this.handler.sendMessage(msg);
			}
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
		
		the_app = (THUClient) getApplicationContext();
		
		btn = (ImageButton)findViewById(R.id.buttonId);
		editText = (EditText) findViewById(R.id.editTextId);
		//receiveBox = (EditText) findViewById(R.id.receiveBoxId);
		
		Context ctx = LoginActivity.this;
		SharedPreferences sp = ctx.getSharedPreferences("SP", MODE_PRIVATE);
		
		final String lastIP = sp.getString("IP", "none");
		if(! lastIP.equals("none") )
		{
			the_app.ip = lastIP;
			editText.setText(lastIP);
		}
		final Editor editor = sp.edit();
		
		handler = new Handler()
		{
			public void handleMessage(Message msg)
			{
				String words = (String)msg.obj;
				if( words.equals("ok") == true )
				{
					Toast.makeText(LoginActivity.this, "来自服务器的问候：登陆成功！！欢迎使用~",Toast.LENGTH_LONG).show();
					finish();
				}
				else if(words.equals("did not login") )
				{
					Toast.makeText(LoginActivity.this,"ip输错了或者是主机没有开", Toast.LENGTH_SHORT).show();
				}
			}
		};
		
		btn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0) 
			{
				
				the_app.ip = editText.getText().toString();
				if(! lastIP.equals(the_app.ip))
				{
					editor.putString("IP",the_app.ip);
					editor.commit();
				}
				
				getThread = new GetThread();
				new Thread(getThread).start();
				
				//SendThread sendThread = new SendThread();
				//sendThread.start();
			}
		});
		
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}

