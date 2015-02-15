package com.zero.client;
import java.net.Socket;

import android.app.Application;

public class THUClient extends Application
{
	public String ip;
	public int port=54321;
	public Socket socket = null;
}
