package com.zero.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.widget.Toast;

class Road
{
	int start_x;
	int start_y;
	int end_x;
	int end_y;
	int r;
	int g;
	int b;
	int small;
	
	Road(int sx, int sy, int ex, int ey, int s)
	{
		start_x = sx;
		start_y = sy;
		end_x = ex;
		end_y = ey;
		r=g=b=0;
		small = s;
	}
}



public class StatusActivity extends Activity
{
	public static Road[] road = new Road[20];
	int i;
	THUClient the_app;
	Handler handler;
	Socket socket;
	CanvasView canvasView;
	NetDriverThread netDriverThread;
	
	class NetThread implements Runnable
	{
		boolean end;
		NetThread()
		{
			super();
			end = false;
		}
		@Override
		public void run() 
		{
			socket = the_app.socket;
			try 
			{
				System.out.println("before sending " +end);
				PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
				out.println("2");

				//Scanner in = new Scanner(socket.getInputStream());
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				String words=in.readLine();
				
				System.out.println("after sending " +end);
				System.out.println(words);
				
				String ori[] = words.split(" ");
				int i;
				try
				{
					for(i=1;i<=19;i++)
					{
						road[i].r = Integer.parseInt(ori[3*(i-1)]);
						road[i].g = Integer.parseInt(ori[3*(i-1)+1]);
						road[i].b = Integer.parseInt(ori[3*(i-1)+2]);
					}
				}
				catch(NumberFormatException e)
				{
					System.out.println("caught number exception");
				}
				Message msg = new Message();
				msg.obj="redraw";
				StatusActivity.this.handler.sendMessage(msg);
				
					
			} 
			catch (IOException e) {
				Message msg = new Message();
				msg.obj = "net error";
				StatusActivity.this.handler.sendMessage(msg);
			}
			catch (NullPointerException e)
			{
				Message msg = new Message();
				msg.obj = "net error";
				StatusActivity.this.handler.sendMessage(msg);
			}
		}
		
		public void stop()
		{
			System.out.println("stop called");
			end = true;
		}
	}
	//一个netthtread只问并且接受回答一次就关掉，由另一个NetDriverThread负责定时开启NetThread询问，问完以后休息几秒再开一个即可避免停不下来的问题
	
	class NetDriverThread extends Thread
	{
		
		public void run()
		{
			while(true)
			{
				NetThread netThread = new NetThread();
				new Thread(netThread).start();
				try 
				{
					sleep(2000);
				} catch (InterruptedException e) 
				{
					break;
				}
				
			}
		}
	}
	
	void buildRoad()
	{
		road[1] = new Road(77,194,107,584,0);
		road[2] = new Road(107,641,122,809,0);
		road[3] = new Road(127,834,150,1110,0);
		road[4] = new Road(152,995,245,986,1);
		road[5] = new Road(263,989,368,980,1);
		
		road[6] = new Road(260,999,269,1112,1);
		road[7] = new Road(158,1128,262,1122,1);
		road[8] = new Road(279,1121,378,1116,1);
		road[9] = new Road(105,1123,8,1129,1);
		road[10] = new Road(143,1138,158,1256,0); 
		
		road[11] = new Road(122,627,337,611,0);
		road[12] = new Road(137,825,350,808,1);
		road[13] = new Road(2,636,68,631,0);
		road[14] = new Road(7,885,55,833,1);
		road[15] = new Road(425,1232,419,1130,0);
		
		road[16] = new Road(416,1097,407,992,0);
		road[17] = new Road(404,965,395,821,0);
		road[18] = new Road(392,791,380,620,0);
		road[19] = new Road(353,296,377,557,0);
	}
	
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		canvasView = new CanvasView(this); 
		//setContentView(R.layout.activity_status);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(canvasView);
		the_app = (THUClient) getApplicationContext();
		//canvasView = (CanvasView)findViewById(R.id.CanvasView);
		
		buildRoad();
		
		handler = new Handler()
		{
			public void handleMessage(Message msg)
			{
				String words = (String)msg.obj;
				if(words.equals("net error"))
				{
					Toast.makeText(StatusActivity.this,"网络连接好像有问题", Toast.LENGTH_SHORT).show();
					finish();
				}
				else
				{
					canvasView.invalidate();
				}
			}
		};
		
		netDriverThread = new NetDriverThread();
		netDriverThread.start();
	}
	
	protected void onDestroy()
	{
		netDriverThread.interrupt();
		super.onDestroy();
	}
	
}









