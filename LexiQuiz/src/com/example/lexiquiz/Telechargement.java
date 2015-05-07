package com.example.lexiquiz;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import database.DataBaseHandler;
import database.Question;
import database.Quiz;

public class Telechargement extends Activity {
	
	private DataBaseHandler db = new DataBaseHandler(this);
	
	private String idQuiz;
	private String titre;
	private String auteur;
	byte[] icon;
	
	private int newIdQuiz;
	
	TextView txt_title_in_confirmation;
	TextView txt_auteur_in_confirmation;
	TextView txt_nbquestion_in_confirmation;
	Button btn_confirmation;
	
	private String jsonResult;
	private String url = "http://lexiquiz.alwaysdata.net/download/GET/get.php?id=";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.confirmation);
        Bundle extras = getIntent().getExtras();
        idQuiz = extras.getString("id");
        auteur = extras.getString("auteur");
        titre = extras.getString("titre");

    	setButtonAndText();
    	
    	this.url = this.url + idQuiz;
   	}
	
	/**
	 * Methode qui permet de lancer le menu de telechargement d'un quiz
	 * @param view le bouton telecharger le quiz d'un ami
	 */
    public void supprimer(View view){
        insertIcon(R.drawable.smallshare);
		Quiz quiz = new Quiz(titre, auteur);
		quiz.setIcon(icon);
    	this.newIdQuiz = this.db.addQuiz(quiz);
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
			
			progressDialog.setMessage(getString(string.downloading_quiz));
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
				jsonResult = inputStreamToString(response.getEntity().getContent()).toString();
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
		try {
			JSONObject jsonResponse = new JSONObject(jsonResult);
			JSONArray jsonMainNode = jsonResponse.optJSONArray("questions");

			Question q = new Question(newIdQuiz, "", "", 1);
			
			for (int i = 0; i < jsonMainNode.length(); i++) {
				JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
				q.setEnonce(jsonChildNode.optString("enonce"));
				q.setReponse(jsonChildNode.optString("reponse"));
				this.db.addQuestion(q);
			}
			btn_confirmation.setVisibility(View.INVISIBLE);
			txt_auteur_in_confirmation.setVisibility(View.INVISIBLE);
			txt_title_in_confirmation.setText("Telechargement effectue !");
		} catch (JSONException e) {
			Toast.makeText(getApplicationContext(), "Error" + e.toString(), Toast.LENGTH_SHORT).show();
		}
	}
	
    public void insertIcon(int idDrawable){
    	Resources res = getResources();
        Drawable drawable = res.getDrawable(idDrawable);
        BitmapDrawable bitDw = ((BitmapDrawable) drawable);
        Bitmap bitmap = bitDw.getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        icon = stream.toByteArray();
    }
	
	/**
	 * Methode qui initialise les elements de la vue.
	 * Appelee des la creation de Play.
	 */
	public void setButtonAndText(){
		txt_title_in_confirmation = (TextView) findViewById(R.id.txt_title_in_confirmation);
		txt_auteur_in_confirmation = (TextView) findViewById(R.id.txt_auteur_in_confirmation);
		txt_nbquestion_in_confirmation = (TextView) findViewById(R.id.txt_nbquestion_in_confirmation);
		btn_confirmation = (Button) findViewById(R.id.btn_confirmation_YES);
		btn_confirmation.setText(string.download_quiz);
		txt_title_in_confirmation.setText(this.titre);
		txt_auteur_in_confirmation.setText(this.auteur);
		txt_nbquestion_in_confirmation.setVisibility(View.INVISIBLE);
	}
}