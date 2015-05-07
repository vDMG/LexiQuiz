package com.example.lexiquiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import database.DataBaseHandler;
import database.Question;

public class QuestionCreate extends Activity {
	
	private int idQuiz;	
	private int nbQuestionsCrees;
	private Question question;	
	private DataBaseHandler db = new DataBaseHandler(this);
    private EditText etxt_for_enonce;
    private EditText etxt_for_reponse;
    private TextView txt_creez_votre_quiz;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.edition);
      	setButtonAndText();
        Bundle extras = getIntent().getExtras();
        this.idQuiz = Integer.parseInt(extras.getString("id")); 
		etxt_for_enonce.setHint("Enonce: ");
		etxt_for_reponse.setHint("Reponse: ");
		txt_creez_votre_quiz.setText("Creez votre Question");
	}

	/**
	 * Methode qui ajoute une question a la base de données.
	 * @param view le bouton submit
	 */
	public void creation(View view){
		String enonce = etxt_for_enonce.getText().toString();
		String reponse = etxt_for_enonce.getText().toString();

    	this.question = new Question(this.idQuiz, enonce, reponse, 1);
		this.db.addQuestion(question);
		etxt_for_enonce.setText(" ");
		etxt_for_reponse.setText(" ");
		this.nbQuestionsCrees ++;
		txt_creez_votre_quiz.setText(this.nbQuestionsCrees + " question cree(s) !");
	}	
	
	/**
	 * Methode qui permet de revenir en arriere au click
	 * @param view le bouton retour
	 */
	public void back(View view){
    	this.db.close();
    	Intent intent = new Intent(getApplicationContext(), QuestionGerer.class);
        intent.putExtra("id", Integer.toString(this.idQuiz));
        startActivity(intent);
	}
	
	/**
	 * Methode qui initialise les elements de la vue.
	 * Appelee des la creation de QuestionCreate.
	 */
	public void setButtonAndText(){
	    etxt_for_enonce = (EditText) findViewById(R.id.etxt_for_title);
	    etxt_for_reponse = (EditText) findViewById(R.id.etxt_for_auteur);
	    txt_creez_votre_quiz = (TextView) findViewById(R.id.txt_creez_votre_quiz);
	}
}
