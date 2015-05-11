package com.example.lexiquiz;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import database.DataBaseHandler;
import database.Question;
import database.Quiz;

public class Play extends Activity {
	
	private Quiz quiz;
	private int idAuHasard;
	private List<Question> questions = null;
	private DataBaseHandler db = new DataBaseHandler(this);
	
    private Button btn_next_question;
	private Button btn_ask;
    private Button btn_answer;
    private SeekBar sk_niveau;
    private TextView txt_niveau;
    private TextView txt_title;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        setContentView(R.layout.play);
    	setButtonAndText();
        this.quiz = this.db.getQuiz(Integer.parseInt(extras.getString("id")));
        txt_title.setText("Quiz : " + this.quiz.getTitre());
        next(new View(getApplicationContext()));
	}
	
	/**
	 * Methode qui pose une question du quiz.
	 */
	public void ask(){
		int niveauQuestion;
        
		//Choix aléatoire du niveau de question
        int hasard = (int) ((Math.random() * 100) + 1);
		if(hasard <= 70 ) niveauQuestion = 1;
		else if(hasard <= 95 ) niveauQuestion = 2;
		else niveauQuestion = 3;
		
		this.questions = db.getAllQuestion(this.quiz.getId(), niveauQuestion);
		
		niveauQuestion = 1;
		while(this.questions == null){
        	this.questions = this.db.getAllQuestion(this.quiz.getId(), niveauQuestion);
        	niveauQuestion ++;
		}
		
		if((niveauQuestion != 1) && (db.getAllQuestionSansNiveau(this.quiz.getId()).size() > 5)){
			if((db.getAllQuestion(this.quiz.getId(), 0) != null) && (db.getAllQuestion(this.quiz.getId(), 0).size() > 3)){
	        	this.questions = this.db.getAllQuestion(this.quiz.getId(), 0);
			}
		}
		
		this.idAuHasard = (int)(Math.random() * this.questions.size());
		
        btn_ask.setText(this.questions.get(this.idAuHasard).getEnonce());
	}
	
	/**
	 * Methode qui montre la réponse et gere la SeekBar, enrengistre le niveau.
	 */
	public void answer(){
		btn_answer.setText(this.questions.get(this.idAuHasard).getReponse());
		
		sk_niveau.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) { }
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {	}
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				if(progress < 33 && progress != 0) {
					txt_niveau.setText("Je ne le savais pas.");
					Play.this.db.updateNiveau(Play.this.questions.get(Play.this.idAuHasard).getId(), 1);
				}
				else if(progress < 66 && progress != 0) {
					txt_niveau.setText("Je vais m'en souvenir.");
					Play.this.db.updateNiveau(Play.this.questions.get(Play.this.idAuHasard).getId(), 2);
				}
				else if(progress != 0) {
					txt_niveau.setText("Je le savais !");
					Play.this.db.updateNiveau(Play.this.questions.get(Play.this.idAuHasard).getId(), 3);
				}
			}
		});
	}
	
	/**
	 * Methode qui change la vue (mode enonce) et lance ask()
	 * @param view le bouton suivant
	 */
	public void next(View view){
		//RE-INITIALISATION
		sk_niveau.setProgress(0);
		//TEXTE
		txt_niveau.setText("Je ne le savais pas.");
		btn_answer.setText("Voir la reponse !");
		//COULEUR
    	btn_answer.setBackgroundColor(Color.parseColor("#663366"));
        //INVISIBLE
        sk_niveau.setVisibility(View.INVISIBLE);
        txt_niveau.setVisibility(View.INVISIBLE);
		btn_next_question.setVisibility(View.INVISIBLE);
		
		ask();
	}
	
	/**
	 * Methode qui change la vue (mode reponse) et lance answer()
	 * @param view le bouton voir la réponse !
	 */
	public void showAnswer(View view){
		//COLOR
    	btn_answer.setBackgroundColor(Color.parseColor("#336699"));
        //VISIBLE
    	btn_next_question.setVisibility(View.VISIBLE);
        sk_niveau.setVisibility(View.VISIBLE);
        txt_niveau.setVisibility(View.VISIBLE);
        
        answer();
	}
	
	/**
	 * Methode qui permet de revenir en arriere au click
	 * @param view le bouton retour
	 */
	public void back(View view){
    	db.close();
    	Intent intent = new Intent(getApplicationContext(), Menu.class);
    	startActivity(intent);
	}
	
	/**
	 * Methode qui initialise les elements de la vue.
	 * Appelee des la creation de Play.
	 */
	public void setButtonAndText(){
	    sk_niveau = (SeekBar) findViewById(R.id.sk_niveau);
	    btn_next_question = (Button) findViewById(R.id.btn_next_question);
	    txt_niveau = (TextView) findViewById(R.id.txt_niveau);
	    btn_answer = (Button) findViewById(R.id.btn_answer);
		btn_ask = (Button) findViewById(R.id.btn_ask);
		txt_title = (TextView) findViewById(R.id.txt_titre_simplerow);
	}
}
