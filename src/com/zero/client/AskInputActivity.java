package com.zero.client;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

class Place
{
	String name;
	int num;
	Place(String s, int n)
	{
		name=s;
		num=n;
	}
	@Override
	public String toString()
	{
		return name;
	}
}

public class AskInputActivity extends Activity
{
    Spinner from;
    Spinner to;
    ImageButton btn;
    TextView ansText;
    Button toMap;
    
    Socket socket;
    NetThread netThread;
    Handler handler;
    
    THUClient the_app;
    
    List <Place> place;
    ArrayAdapter <Place> adapter;
    int nowFrom, nowTo;
    String mapName;
    
    class NetThread implements Runnable
    {
		@Override
		public void run() 
		{
			try 
			{
				PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
				out.println("1 "+ nowFrom +" "+ nowTo);
				
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				String ans = in.readLine();
				if(ans != null)
				{
					Message msg = new Message();
					msg.obj = ans;
					AskInputActivity.this.handler.sendMessage(msg);
				}
				
			} catch (IOException e) {
				Message msg = new Message();
				msg.obj = "net error";
				AskInputActivity.this.handler.sendMessage(msg);
			}
			
		}
    	
    }
    
    void buildList()
    {
    	place = new ArrayList<Place>();
    	place.add(new Place("C楼",1));
    	place.add(new Place("听涛",2));
    	place.add(new Place("三教",3));
    	place.add(new Place("综体",4));
    	place.add(new Place("六教",5));
    	place.add(new Place("校史馆",6));
    	adapter = new ArrayAdapter<Place>(this, android.R.layout.simple_spinner_item,place);
    	from.setAdapter(adapter);
    	to.setAdapter(adapter);
    }
	
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_askinput);
		
		the_app = (THUClient) getApplicationContext();
		socket = the_app.socket;
		
		from = (Spinner) findViewById(R.id.fromSpinnerID);
		to = (Spinner) findViewById(R.id.toSpinnerID);
		btn = (ImageButton) findViewById(R.id.button2Id);
		ansText = (TextView) findViewById(R.id.ansTextID);
		toMap = (Button) findViewById(R.id.toMapID);
		
		buildList();
		
		handler = new Handler()
		{
			public void handleMessage(Message msg)
			{
				String words = (String)msg.obj;
				if(words.equals("net error"))
				{
					Toast.makeText(AskInputActivity.this,"网络连接好像有问题", Toast.LENGTH_SHORT).show();
					finish();
				}
				else
				{
					ansText.setText(words);
					mapName = words;
					toMap.setVisibility(View.VISIBLE);
				}
			}
		};
		
		btn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0) 
			{
				if(nowFrom == nowTo)
				{
					Toast.makeText(AskInputActivity.this, "起终点怎么能一样呢？" , Toast.LENGTH_SHORT).show();
				}
				else if(nowFrom == 0 || nowTo == 0)
				{
					Toast.makeText(AskInputActivity.this, "起终点有一个没有选定" , Toast.LENGTH_SHORT).show();
				}
				else
				{
					NetThread sendThread = new NetThread();
					new Thread(sendThread).start();
				}
			}
		});
		
		from.setOnItemSelectedListener(new OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> parent, View arg1,int position, long arg3) 
			{
				nowFrom =( (Place)parent.getItemAtPosition(position) ) . num;	
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) 
			{
				nowFrom = 0;
			}
		} );
		
		to.setOnItemSelectedListener(new OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> parent, View arg1,int position, long arg3) 
			{
				nowTo =( (Place)parent.getItemAtPosition(position) ) . num;	
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) 
			{
				nowTo = 0;
			}
		} );
		
		toMap.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0) 
			{
			    Intent intent = new Intent();
			    intent.putExtra("mapName", mapName);
			    intent.setClass(AskInputActivity.this, MapDisplayActivity.class);
			    AskInputActivity.this.startActivity(intent);
			}
			
		});
		
	}
	
	@Override
	protected void onDestroy() 
	{
		super.onDestroy();
	}

	protected void onResume()
	{
		super.onResume();
	}
}






