package com.studyAll.test0;

import android.app.*;
import android.os.*;
import android.widget.*;
import android.text.method.*;
import android.text.*;
import android.view.*;

public class MainActivity extends Activity 
{
	public static EditText shareConsole;
	public Console m_console;
	public static MainActivity instance;
	boolean firstInit = true;
	
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		instance = this;
		shareConsole = findViewById(R.id.console);
    }

	@Override
	protected void onStart()
	{
		if(firstInit){
			firstInit = false;
			m_console = new Console(shareConsole);
			m_console.CTS("main.c++\n");
			m_console.setStopInputingToken('\n');
			m_console.setOnEndInputListener(new Console.OnEndInputListener(){

					@Override
					public void perform(String result)
					{
						m_console.append("你输入了:");
						m_console.append(result);
						m_console.newLine();
						m_console.Input();
					}

				
			});
			m_console.Input();
		}
		super.onStart();
	}
	
	public static void sToast(String s){
		sToast(s,Toast.LENGTH_SHORT);
	}

	public static void sToast(String s,int t){
		Handler handler = new Handler(Looper.getMainLooper());
		final String as = s;
		final int ft = t;
		handler.post(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(MainActivity.instance,as,ft).show();
				}
			});
	}
}
