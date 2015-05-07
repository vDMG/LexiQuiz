package com.example.lexiquiz;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.lexiquiz.R.string;



import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class AmisDownload extends Activity {
	
	private List<HashMap<String, Object>> aList = new ArrayList<HashMap<String, Object>>();
	private String id;
	private String titre;
	private String auteur;

	private String jsonResult;
	private String url = "http://lexiquiz.alwaysdata.net/download/GET/";
	private ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.quiz_choix);
		Bundle extras = getIntent().getExtras();
        if(extras.getString("type") != null) this.url = this.url + extras.getString("type");
		listView = (ListView) findViewById(R.id.lt_listQuiz);
		accessWebService();
	}

	private class JsonReadTask extends AsyncTask<String, Void, String> {
		
		ProgressDialog progressDialog;
		
		public JsonReadTask(Context context){
			progressDialog = new ProgressDialog(context, ProgressDialog.THEME_HOLO_DARK);
		}
		
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			
			progressDialog.setMessage(getString(string.loading_quiz));
			progressDialog.setIndeterminate(false);
			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progressDialog.setCancelable(false);
			progressDialog.show();
		}
		
		@Override
		protected String doInBackground(String... params) {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(params[0]);
			try {
				HttpResponse response = httpclient.execute(httppost);
				jsonResult = inputStreamToString(
						response.getEntity().getContent()).toString();
			}

			catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		private StringBuilder inputStreamToString(InputStream is) {
			String rLine = "";
			StringBuilder answer = new StringBuilder();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));

			try {
				while ((rLine = rd.readLine()) != null) {
					answer.append(rLine);
				}
			}

			catch (IOException e) {
				Toast.makeText(getApplicationContext(),
						"Error..." + e.toString(), Toast.LENGTH_LONG).show();
			}
			return answer;
		}

		@Override
		protected void onPostExecute(String result) {
			progressDialog.dismiss();
			ListDrwaer();
		}
	}

	public void accessWebService() {
		JsonReadTask task = new JsonReadTask(this);
		task.execute(new String[] { url });
	}

	public void ListDrwaer(){
		SimpleAdapter adapter = null;
		
		JSONObject jsonResponse;
		
		if(jsonResult == null){
			setContentView(R.layout.play_before);
			Button btn_launch = (Button) findViewById(R.id.btn_launch);
			View img_icon_quiz_before = findViewById(R.id.img_icon_quiz_before);
			TextView txt_nbquestion = (TextView) findViewById(R.id.txt_nbquestion);
			btn_launch.setVisibility(View.INVISIBLE);
			img_icon_quiz_before.setVisibility(View.INVISIBLE);
			txt_nbquestion.setVisibility(View.INVISIBLE);
			
			TextView txt_title = (TextView) findViewById(R.id.txt_title);
			txt_title.setText("Necessite une connection internet !");
		}
		else if(jsonResult.length() == 0 || jsonResult.equals("[]")){
			setContentView(R.layout.play_before);
			Button btn_launch = (Button) findViewById(R.id.btn_launch);
			View img_icon_quiz_before = findViewById(R.id.img_icon_quiz_before);
			TextView txt_nbquestion = (TextView) findViewById(R.id.txt_nbquestion);
			btn_launch.setVisibility(View.INVISIBLE);
			img_icon_quiz_before.setVisibility(View.INVISIBLE);
			txt_nbquestion.setVisibility(View.INVISIBLE);
			
			TextView txt_title = (TextView) findViewById(R.id.txt_title);
			txt_title.setText("Aucun résultat");
		}
		else{
			try {
				jsonResponse = new JSONObject(jsonResult);

				JSONArray jsonMainNode = jsonResponse.optJSONArray("quiz");

				for (int i = 0; i < jsonMainNode.length(); i++) {
					JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
					HashMap<String, Object> hm = new HashMap<String, Object>();
					hm.put("id", jsonChildNode.optString("idQuiz"));
					hm.put("titre", "Titre : " + jsonChildNode.optString("titre"));
					hm.put("fullTitre", jsonChildNode.optString("titre"));
					hm.put("auteur", "Auteur : " + jsonChildNode.optString("auteur"));
					hm.put("fullAuteur", jsonChildNode.optString("auteur"));
					
					Resources res = getResources();
			        Drawable drawable = res.getDrawable(R.drawable.smallshare);
			        BitmapDrawable bitDw = ((BitmapDrawable) drawable);
			        Bitmap bitmap = bitDw.getBitmap();
			        ByteArrayOutputStream stream = new ByteArrayOutputStream();
			        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
			        byte[] icon = stream.toByteArray();
					
					Bitmap bitMapImage = BitmapFactory.decodeByteArray(
							icon, 0,
							icon.length);
					BitmapDrawable img = new BitmapDrawable(getResources(), bitMapImage);
					
					hm.put("img", img);	
					aList.add(hm);
				}
			} catch (JSONException e) {
				Toast.makeText(getApplicationContext(), "Error" + e.toString(),
						Toast.LENGTH_SHORT).show();
			}
			String[] from = {"img", "titre", "auteur" };
			int[] to = {R.id.img_icon, R.id.txt_titre_simplerow, R.id.txt_auteur_simplerow };
			adapter = new SimpleAdapter(this, aList, R.layout.simplerow, from, to);
			adapter.setViewBinder(new SimpleAdapter.ViewBinder() {
			    @Override
			    public boolean setViewValue(View view, Object data, String textRepresentation) {
			        if(view.getId() == R.id.img_icon) {
			            ImageView imageView = (ImageView) view;
			            Drawable drawable = (Drawable) data;
			            imageView.setImageDrawable(drawable);
			            return true;
			        }
			        return false;
			    }
			});
			listView.setAdapter(adapter);
			OnItemClickListener itemClickListener = new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View container, int position, long id) {
					AmisDownload.this.id = (String) AmisDownload.this.aList.get(position).get("id");
					AmisDownload.this.titre = (String) AmisDownload.this.aList.get(position).get("fullTitre");
					AmisDownload.this.auteur = (String) AmisDownload.this.aList.get(position).get("fullAuteur");

			        AmisDownload.this.beginDownload();
				}
			};
			listView.setOnItemClickListener(itemClickListener);
		}
	}
	
	public void beginDownload(){
		Intent intent = new Intent(getApplicationContext(), Telechargement.class);
		intent.putExtra("id", this.id);
		intent.putExtra("titre", this.titre);
		intent.putExtra("auteur", this.auteur);
		startActivity(intent);
	}
}