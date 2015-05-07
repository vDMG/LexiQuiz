package com.example.lexiquiz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import database.DataBaseHandler;
import database.Question;

public class QuestionRemove extends Activity {

	private int idQuiz;
	private List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();
	private DataBaseHandler db = new DataBaseHandler(this);
	private Question question;
	private TextView txt_title_in_confirmation;
	private TextView txt_auteur_in_confirmation;
	private TextView txt_nbquestion_in_confirmation;
	private Button btn_confirmation_YES;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.quiz_choix);
		
        Bundle extras = getIntent().getExtras();
        this.idQuiz = Integer.parseInt(extras.getString("id")); 
		
		List<Question> all = db.getAllQuestionSansNiveau(idQuiz);
		if(all != null){
			for (Question q : all) {
				HashMap<String, String> hm = new HashMap<String, String>();
				hm.put("enonce", "Enonce : " + q.getEnonce());
				hm.put("reponse", "Reponse : " + q.getReponse());
				hm.put("id", "" + q.getId());
				aList.add(hm);
			}
			String[] from = { "enonce", "reponse" };
			int[] to = { R.id.txt_enonce_simplerow, R.id.txt_reponse_simplerow };
			SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), aList, R.layout.simplerow_without_img, from, to);
			ListView listQuiz = (ListView) findViewById(R.id.lt_listQuiz);
			listQuiz.setAdapter(adapter);
			OnItemClickListener itemClickListener = new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View container,
						int position, long id) {
					confirmRemove(Integer.parseInt(aList.get(position).get("id")));
				}
			};
			listQuiz.setOnItemClickListener(itemClickListener);
		}
	}
	
	/**
	 * Methode qui change de vue xml et affiche les infos de la question à supprimer.
	 * @param id l'id de la question.
	 */
	public void confirmRemove(int id){
		setContentView(R.layout.confirmation);
		setButtonAndText();
		
		this.question = this.db.getQuestion(id);
		txt_title_in_confirmation.setText("Enonce: " + question.getEnonce());
		txt_auteur_in_confirmation.setText("Reponse: " + question.getReponse());
		txt_nbquestion_in_confirmation.setText(null);
	}
	
	/**
	 * Methode qui permet de supprimer une question de la base de données
	 * @param view le bouton supprimer
	 */
	public void supprimer(View view){
    	this.db.deleteQuestion((question.getId()));
    	txt_title_in_confirmation.setVisibility(View.INVISIBLE);
    	txt_auteur_in_confirmation.setVisibility(View.INVISIBLE);
    	btn_confirmation_YES.setVisibility(View.INVISIBLE);
    	txt_nbquestion_in_confirmation.setText("Cette question est supprimee.");
	}

	/**
	 * Methode qui permet de revenir en arriere au click
	 * @param view le bouton retour
	 */
	public void back(View view){
    	this.db.close();
		Intent intent = new Intent(getApplicationContext(), QuestionGerer.class);
		intent.putExtra("id", Integer.toString(idQuiz));
		startActivity(intent);
	}
	
	/**
	 * Methode qui initialise les elements de la vue.
	 * Appelee des la creation de QuizRemove.
	 */
	public void setButtonAndText(){
		txt_title_in_confirmation = (TextView) findViewById(R.id.txt_title_in_confirmation);
		txt_auteur_in_confirmation = (TextView) findViewById(R.id.txt_auteur_in_confirmation);
		txt_nbquestion_in_confirmation = (TextView) findViewById(R.id.txt_nbquestion_in_confirmation);
		btn_confirmation_YES = (Button) findViewById(R.id.btn_confirmation_YES);
	}
}
