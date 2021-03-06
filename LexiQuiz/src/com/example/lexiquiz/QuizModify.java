package com.example.lexiquiz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import database.DataBaseHandler;
import database.Quiz;

public class QuizModify extends Activity {

	private List<HashMap<String, Object>> aList = new ArrayList<HashMap<String, Object>>();
	private DataBaseHandler db = new DataBaseHandler(this);
	
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
					quizToModify((String) aList.get(position).get("id"));
				}
			};
			listQuiz.setOnItemClickListener(itemClickListener);
		}
	}
	
	/**
	 * Methode qui appelle l'activité permettant de modifier un quiz. (QuizMenu)
	 * @param idQuiz l'id du quiz à modifier.
	 */
	public void quizToModify(String idQuiz){
		Intent intent = new Intent(getApplicationContext(), QuizMenu.class);
        intent.putExtra("id", idQuiz);
		startActivity(intent);
	}
}
