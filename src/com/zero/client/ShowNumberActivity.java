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
import android.view.Menu;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;


public class ShowNumberActivity extends Activity 
{
	final int FILES = 3;
	
    TextView texts[] = new TextView[FILES*2];
    String ans[];
    Socket socket;
    
    String answers[];
    
    NetThread netThread;
    
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
				out.println("3");
				
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				String ans = in.readLine();
				
				if(ans != null)
				{
					answers = ans.split(" ");
					Message msg = new Message();
					msg.obj = "update";
					ShowNumberActivity.this.handler.sendMessage(msg);
				}
				
			} catch (IOException e) 
			{
				Message msg = new Message();
				msg.obj = "net error";
				ShowNumberActivity.this.handler.sendMessage(msg);
			}
			
		}
    	
    }
    
    
    void buildTexts()
    {
    	/*int i;
    	for(i=1;i<=FILES;i++)
    	{
    		texts[i] = new TextView(this);
    	}*/
    	texts[1] =(TextView) findViewById(R.id.numberText1ID);
		texts[2] =(TextView) findViewById(R.id.numberText2ID);
		texts[3] =(TextView) findViewById(R.id.numberText3ID);
    }
    
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_show_number);
		
		the_app = (THUClient) getApplicationContext();
		socket = the_app.socket;
		
		buildTexts();
		
		handler = new Handler()
		{
			public void handleMessage(Message msg)
			{
				String words = (String)msg.obj;
				if(words.equals("net error"))
				{
					Toast.makeText(ShowNumberActivity.this,"网络连接好像有问题", Toast.LENGTH_SHORT).show();
					finish();
				}
				else
				{
					texts[1].setText("紫荆食堂的人数："+answers[0]);
					texts[2].setText("桃李食堂的人数："+answers[1]);
					texts[3].setText("美术学院A区的人数："+answers[2]);
				}
			}
		};
		
		netThread = new NetThread();
		new Thread(netThread).start();
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.show_number, menu);
		return true;
	}

}
