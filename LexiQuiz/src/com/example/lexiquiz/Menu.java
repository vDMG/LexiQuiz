package com.example.lexiquiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Menu extends Activity {
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gerer);
        //ADAPTATION DE LA VUE
        Button btn_for_create = (Button) findViewById(R.id.btn_for_create);
        Button btn_for_change = (Button) findViewById(R.id.btn_for_change);
        Button btn_for_remove = (Button) findViewById(R.id.btn_for_remove);
        btn_for_create.setText(R.string.play_quiz);
        btn_for_change.setText(R.string.modify_quiz);
        btn_for_remove.setText(R.string.share_quiz);
    }
    
	/**
	 * Methode qui permet de lancer le jeu d'un quiz
	 * @param view le bouton jouer
	 */
	public void lancerCrea(View view){
    	Intent intent = new Intent(getApplicationContext(), QuizChoix.class);
    	startActivity(intent);
    }
	
	/**
	 * Methode qui permet de lancer le menu gerer
	 * @param view le bouton gerer mes quiz
	 */
    public void lancerModif(View view){
    	Intent intent = new Intent(getApplicationContext(), QuizGerer.class);
    	startActivity(intent);
    }
    
	/**
	 * Methode qui permet de lancer le menu de telechargement d'un quiz
	 * @param view le bouton telecharger le quiz d'un ami
	 */
    public void lancerSupp(View view){
    	Intent intent = new Intent(getApplicationContext(), AmisMenu.class);
    	startActivity(intent);
    }
}
