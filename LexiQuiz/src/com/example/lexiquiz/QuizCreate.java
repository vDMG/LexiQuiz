package com.example.lexiquiz;

import java.io.ByteArrayOutputStream;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import database.DataBaseHandler;
import database.Quiz;

@SuppressLint("DefaultLocale")
public class QuizCreate extends Activity {
	
	private Quiz quiz;	
	private DataBaseHandler db = new DataBaseHandler(this);
    private EditText etxt_for_title;
    private EditText etxt_for_auteur;
    private Button btn_submit_in_create;
    private TextView txt_creez_votre_quiz;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.edition);
      	setButtonAndText();
	}

	/**
	 * Methode qui ajoute un quiz a la base de données et change la vue.
	 * @param view le bouton submit
	 */
	public void creation(View view){
    	this.quiz = new Quiz(etxt_for_title.getText().toString(), etxt_for_auteur.getText().toString());
    	
    	String titre = etxt_for_title.getText().toString();
		if(this.db.getQuizByTitle(titre) == null){
			
			Resources res = getResources();
	        Drawable drawable = res.getDrawable(R.drawable.listviewquiz);
	        BitmapDrawable bitDw = ((BitmapDrawable) drawable);
	        Bitmap bitmap = bitDw.getBitmap();
	        ByteArrayOutputStream stream = new ByteArrayOutputStream();
	        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
	        byte[] icon = stream.toByteArray();
	        quiz.setIcon(icon);
	        
			this.db.addQuiz(quiz);
			txt_creez_votre_quiz.setText("Vous venez de creer votre quiz !");
			invisible();
		}
		else txt_creez_votre_quiz.setText("Ce titre existe deja.");
	}	
	
	/**
	 * Methode qui change la vue. 
	 * Rend tout invisible sauf la notification de création.
	 */
	public void invisible(){
		etxt_for_title.setVisibility(View.INVISIBLE);
		etxt_for_auteur.setVisibility(View.INVISIBLE);
		btn_submit_in_create.setVisibility(View.INVISIBLE);
	}
	
	/**
	 * Methode qui permet de revenir en arriere au click
	 * @param view le bouton retour
	 */
	public void back(View view){
    	this.db.close();
    	Intent intent = new Intent(getApplicationContext(), QuizGerer.class);
        startActivity(intent);
	}
	
	/**
	 * Methode qui initialise les elements de la vue.
	 * Appelee des la creation de QuizCreate.
	 */
	public void setButtonAndText(){
	    etxt_for_title = (EditText) findViewById(R.id.etxt_for_title);
	    etxt_for_auteur = (EditText) findViewById(R.id.etxt_for_auteur);
	    btn_submit_in_create = (Button) findViewById(R.id.btn_submit_in_create);
	    txt_creez_votre_quiz = (TextView) findViewById(R.id.txt_creez_votre_quiz);
	}
}
