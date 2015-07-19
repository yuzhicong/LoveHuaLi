package com.yzc.lovehuali.tool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


import android.os.AsyncTask;
import android.widget.Toast;

public class Getjson {
		public static interface isLoadDataListener {
	        public void loadComplete(String infojson);
	    }
	isLoadDataListener loadLisneter;
	public void setLoadDataComplete(isLoadDataListener dataComplete) {
	        this.loadLisneter = dataComplete;
	    }

	public void news(String url){
    	new AsyncTask<String, Void, String>(){
			@Override
			protected String doInBackground(String... params) {
				try {
					URL url = new URL(params[0]);
					URLConnection connection = url.openConnection();
					InputStream ist = connection.getInputStream();
					InputStreamReader reader = new InputStreamReader(ist);
					BufferedReader br = new BufferedReader(reader);
					String jsonline;
					StringBuilder newsbuilder = new StringBuilder();
					while((jsonline = br.readLine()) != null)
					{
						newsbuilder.append(jsonline);
					}
					jsonline = newsbuilder.toString();
					reader.close();
					ist.close();
					return jsonline;
				} catch (MalformedURLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e) {
					return "NwError";
				}
				return null;
			}

			@Override
			protected void onCancelled() {
				// TODO Auto-generated method stub
				super.onCancelled();
			}

			@Override
			protected void onCancelled(String result) {
				// TODO Auto-generated method stub
				super.onCancelled(result);
			}

			@Override
			protected void onPostExecute(String result) {
				if (loadLisneter != null) {
	                loadLisneter.loadComplete(result);
	            }
			}

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
			}

			@Override
			protected void onProgressUpdate(Void... values) {
				// TODO Auto-generated method stub
				super.onProgressUpdate(values);
			} 
			
    	}.execute(url);
    }
}