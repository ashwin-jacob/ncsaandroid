package com.example.ncsamobile;

import java.net.MalformedURLException;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.util.Log;
import com.example.ncsamobile.R;
import io.socket.IOAcknowledge;
import io.socket.IOCallback;
import io.socket.SocketIO;
import io.socket.SocketIOException;


public class MainActivity extends Activity {
		
	private static final String TAG = "de.myfirstapp.test1";
	private View backgroundColorView;
	
	private class ConnectToServerTask extends AsyncTask<Void, String, String> {
		
		@Override
		protected String doInBackground(Void... unused) {
			SocketIO socket = null;
			try {
				socket = new SocketIO("http://ec2-23-20-189-169.compute-1.amazonaws.com:8080/");
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
			}
			
			socket.connect(new IOCallback() {
				@Override
				public void onMessage(JSONObject json, IOAcknowledge ack) {
					try {
						Log.d(TAG, "Server said: " + json.toString(2));
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}

				@Override
				public void onDisconnect() {
					Log.d(TAG, "Connection Terminated: ");
				}

				@Override
				public void onConnect() {
					Log.d(TAG, "Connection Established: ");
				}

				@Override
				public void onMessage(String data, IOAcknowledge ack) {
					Log.d(TAG, "Server said: " + data);
					
				}

				@Override
				public void on(String event, IOAcknowledge ack, Object... args) {
					Log.d(TAG, "Server triggered event '"+event+"'");
					Log.d(TAG, "Objects: '"+args[0]+"'");
					publishProgress((String)args[0]);
					
				}

				@Override
				public void onError(SocketIOException socketIOException) {
					Log.d(TAG, "an Error occured: ");
					Log.e(TAG, socketIOException.toString());
					socketIOException.printStackTrace();
				}
			});
			socket.send("Hello Server!");
			return null;
		}
		
		@Override
		protected void onProgressUpdate(String...colorValue) {
			backgroundColorView.setBackgroundColor(Color.parseColor(colorValue[0]));
			Log.d(TAG, "Got To onprogress");
		}
			
	}
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        backgroundColorView = findViewById(R.id.backgroundColor);
        
        new ConnectToServerTask().execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
  
    
}
