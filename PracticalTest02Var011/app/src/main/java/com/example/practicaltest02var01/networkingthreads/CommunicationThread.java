package com.example.practicaltest02var01.networkingthreads;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.example.practicaltest02var01.utilities.Constants;
import com.example.practicaltest02var01.utilities.TextAutoComplete;
import com.example.practicaltest02var01.utilities.Utilities;

public class CommunicationThread extends Thread {

	private ServerThread serverThread;
	private Socket socket;

	public CommunicationThread(ServerThread serverThread, Socket socket) {
		this.serverThread = serverThread;
		this.socket = socket;
	}

	@Override
	public void run() {
		if (socket != null) {
			try {
				BufferedReader bufferedReader = Utilities.getReader(socket);
				PrintWriter printWriter       = Utilities.getWriter(socket);

				if (bufferedReader != null && printWriter != null) {
					Log.i(Constants.TAG,
							"[COMMUNICATION THREAD] Waiting for parameters from client (city / information type)!");

					String informationType = bufferedReader.readLine();

					if (informationType != null && !informationType.isEmpty()) {

						TextAutoComplete textCompleted = null;
						TextAutoComplete insertedText = null; //data

						Log.i(Constants.TAG,
								"[COMMUNICATION THREAD] Getting the information from the webservice...");

						if (serverThread.getTextAutoComplete() != null) {
							System.out.println("Other connections");
                            textCompleted = serverThread.getTextAutoComplete();
						} else {
							System.out.println("First connection");

							HttpClient httpClient = new DefaultHttpClient();
							HttpGet httpGet = new HttpGet(
									Constants.WEB_SERVICE_ADDRESS);

							ResponseHandler<String> responseHandler = new BasicResponseHandler();
							String pageContent = httpClient.execute(httpGet,
									responseHandler);

							System.out.println(pageContent);

							if (pageContent != null) {

								JSONObject content = new JSONObject(pageContent);

								JSONObject mainInfo = content
										.getJSONObject(Constants.RESULT);



								String name = mainInfo
										.getString(Constants.NAME);


                                textCompleted = new TextAutoComplete();
								serverThread
										.setTextAutoComplete(insertedText);

							} else {
								Log.e(Constants.TAG,
										"[COMMUNICATION THREAD] Error getting the information from the webservice!");
							}
						}
//la feeeel adaptat
						if (textCompleted != null) {
							String result = null;

								result = "Wrong information type (all / temperature / wind_speed / condition / humidity / pressure)!";

							printWriter.println(result);
							printWriter.flush();
						} else {
							Log.e(Constants.TAG,
									"[COMMUNICATION THREAD] Weather Forecast information is null!");
						}

					} else {
						Log.e(Constants.TAG,
								"[COMMUNICATION THREAD] Error receiving parameters from client (information type)!");
					}




				} else {
					Log.e(Constants.TAG,
							"[COMMUNICATION THREAD] BufferedReader / PrintWriter are null!");
				}
				socket.close();
			} catch (IOException ioException) {
				Log.e(Constants.TAG,
						"[COMMUNICATION THREAD] An exception has occurred: "
								+ ioException.getMessage());
				if (Constants.DEBUG) {
					ioException.printStackTrace();
				}
			} catch (JSONException jsonException) {
				Log.e(Constants.TAG,
						"[COMMUNICATION THREAD] An exception has occurred: "
								+ jsonException.getMessage());
				if (Constants.DEBUG) {
					jsonException.printStackTrace();
				}
			}
		} else {
			Log.e(Constants.TAG, "[COMMUNICATION THREAD] Socket is null!");
		}
	}

}
