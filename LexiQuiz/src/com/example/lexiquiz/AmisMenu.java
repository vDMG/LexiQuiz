package com.example.lexiquiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AmisMenu extends Activity {
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gerer);
        //ADAPTATION DE LA VUE
        Button btn_for_create = (Button) findViewById(R.id.btn_for_create);
        Button btn_for_change = (Button) findViewById(R.id.btn_for_change);
        Button btn_for_remove = (Button) findViewById(R.id.btn_for_remove);
        btn_for_create.setText(R.string.download_quiz);
        btn_for_change.setText(R.string.upload_quiz);
        btn_for_remove.setText(R.string.forum_quiz);
        btn_for_remove.setVisibility(View.INVISIBLE);
    }
    
	/**
	 * Methode qui permet de lancer le jeu d'un quiz
	 * @param view le bouton jouer
	 */
	public void lancerCrea(View view){
    	Intent intent = new Intent(getApplicationContext(), AmisDownloadMenu.class);
    	startActivity(intent);
    }
	
	/**
	 * Methode qui permet de lancer le menu gerer
	 * @param view le bouton gerer mes quiz
	 */
    public void lancerModif(View view){
    	Intent intent = new Intent(getApplicationContext(), AmisUploadAsync.class);
    	startActivity(intent);
    }
    
	/**
	 * Methode qui permet de lancer le menu de telechargement d'un quiz
	 * @param view le bouton telecharger le quiz d'un ami
	 */
    public void lancerSupp(View view){
    	Intent intent = new Intent(getApplicationContext(), AmisForum.class);
    	startActivity(intent);
    }
    
	/**
	 * Methode qui permet de lancer le menu de telechargement d'un quiz
	 * @param view le bouton telecharger le quiz d'un ami
	 */
    public void back(View view){
    	Intent intent = new Intent(getApplicationContext(), Menu.class);
    	startActivity(intent);
    }
}
