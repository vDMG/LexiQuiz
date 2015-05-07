package com.example.lexiquiz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.lexiquiz.R.string;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import database.DataBaseHandler;
import database.Quiz;

public class QuizRemove extends Activity {

	private List<HashMap<String, Object>> aList = new ArrayList<HashMap<String, Object>>();
	private DataBaseHandler db = new DataBaseHandler(this);
	private Quiz quiz;
	private TextView txt_title_in_confirmation;
	private TextView txt_auteur_in_confirmation;
	private TextView txt_nbquestion_in_confirmation;
	private Button btn_confirmation_YES;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.quiz_choix);

		List<Quiz> all = db.getAllQuiz(false);
		if(all != null){
			for (Quiz quiz : all) {
				HashMap<String, Object> hm = new HashMap<String, Object>();
				hm.put("titre", "Titre : " + quiz.getTitre());
				hm.put("auteur", "Auteur : " + quiz.getAuteur());
				Bitmap bitMapImage = BitmapFactory.decodeByteArray(
						quiz.getIcon(), 0,
						quiz.getIcon().length);
				
				BitmapDrawable img = new BitmapDrawable(getResources(), bitMapImage);
				
				hm.put("img", img);
				hm.put("id", "" + quiz.getId());
				aList.add(hm);
			}
			String[] from = { "img", "titre", "auteur" };
			int[] to = { R.id.img_icon, R.id.txt_titre_simplerow, R.id.txt_auteur_simplerow };
			SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), aList,
					R.layout.simplerow, from, to);
			
			adapter.setViewBinder(new SimpleAdapter.ViewBinder() {

			    @Override
			    public boolean setViewValue(View view, Object data, String textRepresentation) {
			        if(view.getId() == R.id.img_icon) {
			            ImageView imageView = (ImageView) view;
			            Drawable drawable = (Drawable) data;
			            imageView.setImageDrawable(drawable);
			            return true;
			        }
			        return false;
			    }
			});
			
			ListView listQuiz = (ListView) findViewById(R.id.lt_listQuiz);
			listQuiz.setAdapter(adapter);
			OnItemClickListener itemClickListener = new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View container,
						int position, long id) {
					confirmRemove(Integer.parseInt((String) aList.get(position).get("id")));
				}
			};
			listQuiz.setOnItemClickListener(itemClickListener);
		}
	}
	
	/**
	 * Methode qui change de vue xml et affiche les infos du quiz a supprimer.
	 * @param id l'id du quiz
	 */
	public void confirmRemove(int id){
		setContentView(R.layout.confirmation);
		setButtonAndText();
		
		this.quiz = this.db.getQuiz(id);
		txt_title_in_confirmation.setText("Titre: " + quiz.getTitre());
		txt_auteur_in_confirmation.setText("Auteur: " + quiz.getAuteur());
		txt_nbquestion_in_confirmation.setText("Nombre de question: " + db.countQuestion(id, 0));
	}
	
	/**
	 * Methode qui permet de supprimer un quiz de la base de données
	 * @param view le bouton supprimer
	 */
	public void supprimer(View view){
    	this.db.deleteQuiz(quiz.getId());
    	txt_title_in_confirmation.setVisibility(View.INVISIBLE);
    	txt_auteur_in_confirmation.setVisibility(View.INVISIBLE);
    	btn_confirmation_YES.setVisibility(View.INVISIBLE);
    	txt_nbquestion_in_confirmation.setText(string.deleted_quiz);
	}
	
	/**
	 * Methode qui initialise les elements de la vue.
	 * Appelee des la creation de QuizRemove.
	 */
	public void setButtonAndText(){
		txt_title_in_confirmation = (TextView) findViewById(R.id.txt_title_in_confirmation);
		txt_auteur_in_confirmation = (TextView) findViewById(R.id.txt_auteur_in_confirmation);
		txt_nbquestion_in_confirmation = (TextView) findViewById(R.id.txt_nbquestion_in_confirmation);
		btn_confirmation_YES = (Button) findViewById(R.id.btn_confirmation_YES);
	}
}
