package com.example.lexiquiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class QuizGerer extends Activity {
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gerer);
    }
	
	/**
	 * Methode qui permet de lancer l'activité QuizRemove permettant la suppression de quiz
	 * @param view le bouton supprimer quiz
	 */
	public void lancerSupp(View view){
    	Intent intent = new Intent(getApplicationContext(), QuizRemove.class);
        startActivity(intent);
	}
	
	/**
	 * Methode qui permet de lancer l'activité QuizCreate permettant la creation de quiz
	 * @param view le bouton cree quiz
	 */
	public void lancerCrea(View view){
    	Intent intent = new Intent(getApplicationContext(), QuizCreate.class);
        startActivity(intent);
	}
	
	/**
	 * Methode qui permet de lancer l'activité QuizModify permettant le choix du quiz à modifier
	 * @param view le bouton modifier quiz
	 */
	public void lancerModif(View view){
    	Intent intent = new Intent(getApplicationContext(), QuizModify.class);
        startActivity(intent);
	}
}
