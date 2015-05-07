package com.example.lexiquiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class TriId extends Activity {
	
	EditText etxt_for_title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edition);
		etxt_for_title = (EditText) findViewById(R.id.etxt_for_title);
		EditText etxt_for_auteur = (EditText) findViewById(R.id.etxt_for_auteur);
		TextView txt_creez_votre_quiz = (TextView) findViewById(R.id.txt_creez_votre_quiz);
		Button btn_submit_in_create = (Button) findViewById(R.id.btn_submit_in_create);

		etxt_for_auteur.setVisibility(View.INVISIBLE);
		etxt_for_title.setHint("Entrez l'id du quiz:");
		txt_creez_votre_quiz.setText("Recherche:");
		btn_submit_in_create.setText("Rechercher");

	}
	
	/**
	 * Methode qui permet de lancer le jeu d'un quiz
	 * @param view le bouton jouer
	 */
	public void creation(View view){
		Intent intent = new Intent(getApplicationContext(), AmisDownload.class);
        intent.putExtra("type", "get.php?TriId=" + etxt_for_title.getText().toString());
    	startActivity(intent);
	}
}