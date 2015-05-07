package com.example.lexiquiz;

import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import database.DataBaseHandler;
import database.Question;
import database.Quiz;
 
public class AmisUploadConfirmation extends Activity {

	private DataBaseHandler db = new DataBaseHandler(this);
	private String url = "http://lexiquiz.alwaysdata.net/upload/up.php";
	private Quiz quiz;
	private Quiz realQuiz;
	private List<Question> q;
	private String resFromServer;
	
	TextView txt_title_in_confirmation;
	TextView txt_auteur_in_confirmation;
	TextView txt_nbquestion_in_confirmation;
	Button btn_confirmation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
		setContentView(R.layout.confirmation);
        setButtonAndText();
        
		Bundle extras = getIntent().getExtras();
		quiz = db.getQuizForUpload(Integer.parseInt(extras.getString("idQuiz")));
		realQuiz = db.getQuiz(Integer.parseInt(extras.getString("idQuiz")));
        q = db.getQuestionsForUpload(Integer.parseInt(extras.getString("idQuiz")));
        txt_title_in_confirmation.setText("Titre: " + realQuiz.getTitre());
        txt_auteur_in_confirmation.setText("Auteur: " + realQuiz.getAuteur());
        if(q != null){
        	txt_nbquestion_in_confirmation.setText("Nb questions: " + q.size());
        }
        else {
        	txt_nbquestion_in_confirmation.setText("Aucune questions");
        }
    }
    
	/**
	 * Methode qui permet de lancer le menu de telechargement d'un quiz
	 * @param view le bouton telecharger le quiz d'un ami
	 */
    public void supprimer(View view){
    	accessWebService();
    }
    
    
    public void accessWebService(){
    	JSONObject toSend = new JSONObject();
		JSONObject jsonQuiz = new JSONObject();

		try {
    		if(q != null){
        		JSONArray jsonArray = new JSONArray();

    			for (Question questions : q) {
    	            JSONObject questionsObj = new JSONObject();
    	            questionsObj.put("enonce", questions.getEnonce());
    	            questionsObj.put("reponse", questions.getReponse());
    	            jsonArray.put(questionsObj);
    	        }
    	        toSend.put("listQuestions", jsonArray);

    		}
	        
	        jsonQuiz.put("titre", quiz.getTitre());
			jsonQuiz.put("auteur", quiz.getAuteur());
			jsonQuiz.put("nbQuestions", q.size());
			toSend.put("quiz", jsonQuiz);
                        
            JSONTransmitter transmitter = new JSONTransmitter(this);
            transmitter.execute(new JSONObject[] {toSend});
            db.close();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
 
    public class JSONTransmitter extends AsyncTask<JSONObject, JSONObject, JSONObject> {
    	
    	ProgressDialog progressDialog;
		
		public JSONTransmitter(Context context){
			progressDialog = new ProgressDialog(context, ProgressDialog.THEME_HOLO_DARK);
		}
		
		protected void onPreExecute() {
			super.onPreExecute();
			
			progressDialog.setMessage(getString(R.string.uploading_quiz));
			progressDialog.setIndeterminate(false);
			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progressDialog.setCancelable(false);
			progressDialog.show();
		}
    	      
        @Override
        protected JSONObject doInBackground(JSONObject... data) {
            JSONObject json = data[0];

            HttpClient client = new DefaultHttpClient();
            HttpConnectionParams.setConnectionTimeout(client.getParams(), 100000);
     
            JSONObject jsonResponse = null;
            HttpPost post = new HttpPost(url);
            try {
                System.out.println(json.toString());
                StringEntity se = new StringEntity("json="+json.toString());
                post.addHeader("content-type", "application/x-www-form-urlencoded");
                post.setEntity(se);
                 
                HttpResponse response;
                response = client.execute(post);
                String resFromServer = org.apache.http.util.EntityUtils.toString(response.getEntity());
                Log.i("Response from server", resFromServer);
                AmisUploadConfirmation.this.resFromServer = resFromServer;
            } catch (Exception e) {
            	System.out.println("Erreur 1");
            	e.printStackTrace();
            }
             
            return jsonResponse;
        }
        
        @Override
        protected void onPostExecute(JSONObject result) {
        	progressDialog.dismiss();
        	finish(result);
        }
	}
    
    public void finish(JSONObject resFromServer){
		btn_confirmation.setVisibility(View.INVISIBLE);
		txt_auteur_in_confirmation.setVisibility(View.INVISIBLE);
		txt_nbquestion_in_confirmation.setVisibility(View.INVISIBLE);
		System.out.println(resFromServer);
		System.out.println(this.resFromServer);
		if(this.resFromServer != null){
			txt_title_in_confirmation.setText("Partage effectue. Cle de votre quiz: " + this.resFromServer);
		}
		else {
			txt_title_in_confirmation.setText("Necessite une connexion internet !");
		}
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
		btn_confirmation.setText("Partager !");
	}
}