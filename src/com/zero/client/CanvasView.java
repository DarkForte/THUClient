package com.zero.client;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class CanvasView extends View
{
	 Paint paint;    
	 Bitmap bmp=BitmapFactory.decodeResource(this.getContext().getResources(), R.drawable.bottom_map);
	 
     public CanvasView(Context context) 
     {    
         super(context);    
         paint = new Paint();   
         paint.setStrokeJoin(Paint.Join.ROUND);    
         paint.setStrokeCap(Paint.Cap.ROUND);    
         paint.setStrokeWidth(15);    
     }    
       
     @Override    
     protected void onDraw(Canvas canvas) 
     {    
    	 canvas.drawBitmap(bmp, 0, 0, null);
    	 int i;
         for(i=1;i<=19;i++)
         {
        	 paint.setARGB(255,StatusActivity.road[i].r, StatusActivity.road[i].g, StatusActivity.road[i].b);
        	 if(StatusActivity.road[i].small == 1)
        		 paint.setStrokeWidth(10);
        	 else
        		 paint.setStrokeWidth(15);
        	 
        	 canvas.drawLine(StatusActivity.road[i].start_x, StatusActivity.road[i].start_y, StatusActivity.road[i].end_x, StatusActivity.road[i].end_y,paint);
         }
     }    
}