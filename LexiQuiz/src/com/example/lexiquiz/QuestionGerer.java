package com.example.lexiquiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class QuestionGerer extends Activity {
	
	private String idQuiz;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gerer);
        //ADAPTATION DE LA VUE        
        Bundle extras = getIntent().getExtras();
        this.idQuiz = extras.getString("id"); 
    }
    
	/**
	 * Methode qui permet de lancer la création d'une question
	 * @param view le bouton créer question 
	 */
	public void lancerCrea(View view){
    	Intent intent = new Intent(getApplicationContext(), QuestionCreate.class);
        intent.putExtra("id", this.idQuiz);
    	startActivity(intent);
    }
	
	/**
	 * Methode qui permet de lancer la modification de question
	 * @param view le bouton modifier question
	 */
    public void lancerModif(View view){
    	Intent intent = new Intent(getApplicationContext(), QuestionChange.class);
        intent.putExtra("id", this.idQuiz);
    	startActivity(intent);
    }
    
	/**
	 * Methode qui permet de lancer la suppression de question
	 * @param view le bouton supprimer question
	 */
    public void lancerSupp(View view){
    	Intent intent = new Intent(getApplicationContext(), QuestionRemove.class);
        intent.putExtra("id", this.idQuiz);
    	startActivity(intent);
    }
}
