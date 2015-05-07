package com.example.lexiquiz;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import database.DataBaseHandler;

public class PlayBefore extends Activity{
	
	private int idQuiz;
	private DataBaseHandler db = new DataBaseHandler(this);
	private TextView txt_nbquestion;
    private TextView txt_title;
    private ImageView img_icon;
	
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        setContentView(R.layout.play_before);
        setButtonAndText();

        this.idQuiz = Integer.parseInt(extras.getString("id"));
        if(this.db.getQuiz(this.idQuiz).getIcon().equals("uneImage")){
        	
        }
        else{
            Bitmap bitMapImage = BitmapFactory.decodeByteArray(
            		this.db.getQuiz(this.idQuiz).getIcon(), 0,
            		this.db.getQuiz(this.idQuiz).getIcon().length);
            img_icon.setImageBitmap(bitMapImage);
        }

        double pourcentageRealise;
        double allquestions = this.db.countQuestion(this.idQuiz, 0);
        double questionsRealisees = this.db.countQuestion(this.idQuiz, 3);
        
        if(allquestions == 0){
            txt_nbquestion.setText("Oups ! Ce quiz ne contient aucune question !");
            findViewById(R.id.btn_launch).setVisibility(View.INVISIBLE);
        }
        else if(questionsRealisees == 0) txt_nbquestion.setText("0% acquis");
        else if(allquestions == questionsRealisees) txt_nbquestion.setText("100% acquis");        
        else{
        	pourcentageRealise = (questionsRealisees / allquestions) * 100;
            java.text.DecimalFormat df = new java.text.DecimalFormat("0.##");        
            txt_nbquestion.setText(df.format(pourcentageRealise) + "% connaissances acquises");
        }
        txt_title.setText(this.db.getQuiz(this.idQuiz).getTitre());
    }

	/**
	 * Methode qui permet de lancer le quiz
	 * @param view le bouton lancer
	 */
	public void lancer(View view){
    	this.db.close();
    	Intent intent = new Intent(getApplicationContext(), Play.class);
        intent.putExtra("id", Integer.toString(this.idQuiz));
        startActivity(intent);
	}
		
	/**
	 * Methode qui initialise les elements de la vue.
	 * Appelee des la creation de PlayBefore.
	 */
	public void setButtonAndText(){
        txt_nbquestion = (TextView) findViewById(R.id.txt_nbquestion);
        txt_title = (TextView) findViewById(R.id.txt_title);
        img_icon = (ImageView) findViewById(R.id.img_icon_quiz_before);
	}
}
