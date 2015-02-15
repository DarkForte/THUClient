package com.zero.client;

import com.zero.client.R.drawable;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

public class MapDisplayActivity extends Activity
{
	ImageView map;
	
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mapdisplay);
		
		Intent intent = getIntent();
		String mapName = intent.getStringExtra("mapName");
		
		map = (ImageView)findViewById(R.id.mapID);
		
		Class<drawable> cls = R.drawable.class;
		int value = 0;
        try {
            value = cls.getDeclaredField(mapName).getInt(null);
        } catch (Exception e) 
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
		
		map.setImageResource(value);
		map.setOnTouchListener(new OnTouchListener()
	    {
			int tempx=0;
			int tempy=0;
			int left=0;
			int top=0;
			public boolean onTouch(View view, MotionEvent event) 
			{
				int actionType = event.getAction();
				int x = (int) event.getRawX();
				int y = (int) event.getRawY();
				
				if(actionType == MotionEvent.ACTION_MOVE)
				{
					int dx= x-tempx;
					int dy= y-tempy;

					view.layout(left+dx,top+dy,left+dx+view.getWidth(),top+dy+view.getHeight());
					view.postInvalidate();
				}
				else if(actionType == MotionEvent.ACTION_DOWN)
				{
					tempx = x;
					tempy = y;
					left = view.getLeft();
					top = view.getTop();
				}
				return true;
			}
		 });
		
	}
}






