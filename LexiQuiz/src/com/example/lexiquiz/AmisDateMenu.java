package com.example.lexiquiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AmisDateMenu extends Activity {
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gerer);
		Button btn_for_create = (Button) findViewById(R.id.btn_for_create);
		Button btn_for_change = (Button) findViewById(R.id.btn_for_change);
		Button btn_for_remove = (Button) findViewById(R.id.btn_for_remove);
		btn_for_create.setText("Les plus recents");
		btn_for_change.setText("Les plus anciens");
		btn_for_remove.setVisibility(View.INVISIBLE);
	}
	
	/**
	 * Methode qui permet de lancer le jeu d'un quiz
	 * @param view le bouton jouer
	 */
	public void lancerCrea(View view){
		Intent intent = new Intent(getApplicationContext(), AmisDownload.class);
        intent.putExtra("type", "get.php?TriDate=0");
    	startActivity(intent);
	}
	
	/**
	 * Methode qui permet de lancer le jeu d'un quiz
	 * @param view le bouton jouer
	 */
	public void lancerModif(View view){
		Intent intent = new Intent(getApplicationContext(), AmisDownload.class);
        intent.putExtra("type", "get.php?TriDate=1");
    	startActivity(intent);
	}
}