package com.example.lexiquiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class QuizMenu extends Activity {
	
	private String idQuiz;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gerer);
        //ADAPTATION DE LA VUE
        Button btn_for_create = (Button) findViewById(R.id.btn_for_create);
        Button btn_for_change = (Button) findViewById(R.id.btn_for_change);
        Button btn_for_remove = (Button) findViewById(R.id.btn_for_remove);
        btn_for_create.setText("Titre/Auteur");
        btn_for_change.setText("Icon");
        btn_for_remove.setText("Questions");

        Bundle extras = getIntent().getExtras();
        this.idQuiz = extras.getString("id"); 
    }
    
	/**
	 * Methode qui permet de lancer la création d'une question
	 * @param view le bouton créer question 
	 */
	public void lancerCrea(View view){
    	Intent intent = new Intent(getApplicationContext(), QuizInfo.class);
        intent.putExtra("id", this.idQuiz);
    	startActivity(intent);
    }
	
	/**
	 * Methode qui permet de lancer la modification de question
	 * @param view le bouton modifier question
	 */
    public void lancerModif(View view){
    	Intent intent = new Intent(getApplicationContext(), IconUpload.class);
        intent.putExtra("id", this.idQuiz);
    	startActivity(intent);
    }
    
	/**
	 * Methode qui permet de lancer la suppression de question
	 * @param view le bouton supprimer question
	 */
    public void lancerSupp(View view){
    	Intent intent = new Intent(getApplicationContext(), QuestionGerer.class);
        intent.putExtra("id", this.idQuiz);
    	startActivity(intent);
    }
    
	/**
	 * Methode qui permet de retourner en arriere.
	 * @param view le bouton back
	 */
    public void back(View view){
    	Intent intent = new Intent(getApplicationContext(), QuizGerer.class);
    	startActivity(intent);
    }
}
