package com.example.practicaltest02var01.gui;

import com.example.practicaltest02var01.utilities.*;
import com.example.practicaltest02var01.networkingthreads.*;


import com.example.practicaltest02var01.R;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PracticalTest02Var01MainActivity extends Activity {

		private EditText     serverPortEditText       = null;
		private Button       connectButton            = null;
		
		private EditText     clientAddressEditText    = null;
		private EditText     clientPortEditText       = null;


		private Button       sendText = null;

		private EditText     setText = null;
		private TextView     textAutoComplete = null;

		private ServerThread serverThread             = null;
		private ClientThread clientThread             = null;

		private ConnectButtonClickListener connectButtonClickListener = new ConnectButtonClickListener();
		private class ConnectButtonClickListener implements Button.OnClickListener {
			
			@Override
			public void onClick(View view) {
				String serverPort = serverPortEditText.getText().toString();
				if (serverPort == null || serverPort.isEmpty()) {
					Toast.makeText(
						getApplicationContext(),
						"Server port should be filled!",
						Toast.LENGTH_SHORT
					).show();
					return;
				}
				
				serverThread = new ServerThread(Integer.parseInt(serverPort));
				if (serverThread.getServerSocket() != null) {
					serverThread.start();
				} else {
					Log.e(Constants.TAG, "[MAIN ACTIVITY] Could not creat server thread!");
				}
				
			}
		}


		private ButtonClickListener getAutoCompleteButtonClickListener = new ButtonClickListener();

		private class ButtonClickListener implements Button.OnClickListener {
			
			@Override
			public void onClick(View view) {
				String clientAddress = clientAddressEditText.getText().toString();
				String clientPort    = clientPortEditText.getText().toString();
				
				if (clientAddress == null || clientAddress.isEmpty() ||
					clientPort == null || clientPort.isEmpty()) {
					Toast.makeText(
						getApplicationContext(),
						"Client connection parameters should be filled!",
						Toast.LENGTH_SHORT
					).show();
					return;
				}
				
				if (serverThread == null || !serverThread.isAlive()) {
					Log.e(Constants.TAG, "[MAIN ACTIVITY] There is no server to connect to!");
					return;
				}

                textAutoComplete.setText(Constants.EMPTY_STRING);

				String textInserted = null;
				 switch(view.getId()) {
				 case R.id.bSend:
					 textInserted = setText.toString();
					 break;
				 }

				clientThread = new ClientThread(
						clientAddress,
						Integer.parseInt(clientPort),
						textInserted,
                        textAutoComplete);
				clientThread.start();
			}
		}

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_practical_test02_var01_main);
			
			serverPortEditText = (EditText)findViewById(R.id.server_port_edit_text);
			connectButton = (Button)findViewById(R.id.connect_button);
			connectButton.setOnClickListener(connectButtonClickListener);
			
			clientAddressEditText = (EditText)findViewById(R.id.client_address_edit_text);
			clientPortEditText = (EditText)findViewById(R.id.client_port_edit_text);

			setText = (EditText) findViewById(R.id.etText);
			sendText = (Button) findViewById(R.id.bSend);

			sendText.setOnClickListener(getAutoCompleteButtonClickListener);


            textAutoComplete = (TextView) findViewById(R.id.tvResult);
			
		}


        //la fel
		@Override
		protected void onDestroy() {
			if (serverThread != null) {
				serverThread.stopThread();
			}
			super.onDestroy();
		}

		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			// Inflate the menu; this adds items to the action bar if it is present.
			getMenuInflater().inflate(R.menu.practical_test02_var01_main, menu);
			return true;
		}

		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			// Handle action bar item clicks here. The action bar will
			// automatically handle clicks on the Home/Up button, so long
			// as you specify a parent activity in AndroidManifest.xml.
			int id = item.getItemId();
			if (id == R.id.action_settings) {
				return true;
			}
			return super.onOptionsItemSelected(item);
		}
}
