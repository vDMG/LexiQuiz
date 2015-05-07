package com.example.lexiquiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import database.DataBaseHandler;
import database.Question;

public class QuestionModify extends Activity {
	
	private int idQuestion;	
	private String idQuiz;	
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
        this.idQuestion = Integer.parseInt(extras.getString("id"));
        this.idQuiz = extras.getString("back");
		txt_creez_votre_quiz.setText("Modifier votre Question");
		
		this.question = this.db.getQuestion(this.idQuestion);
		etxt_for_enonce.setText(this.question.getEnonce());
		etxt_for_reponse.setText(this.question.getReponse());
	}

	/**
	 * Methode qui modifie une question de la base de données.
	 * @param view le bouton submit
	 */
	public void creation(View view){
    	this.question.setEnonce(etxt_for_enonce.getText().toString());
    	this.question.setReponse(etxt_for_reponse.getText().toString());
		this.db.updateQuestion(question);
		txt_creez_votre_quiz.setText("Question modifiée !");
	}	
	
	/**
	 * Methode qui permet de revenir en arriere au click
	 * @param view le bouton retour
	 */
	public void back(View view){
    	this.db.close();
    	Intent intent = new Intent(getApplicationContext(), QuestionChange.class);
        intent.putExtra("id", this.idQuiz);
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
