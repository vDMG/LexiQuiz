package com.example.lexiquiz;

import java.io.ByteArrayOutputStream;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import database.Categorie;
import database.DataBaseHandler;
import database.Quiz;
 
public class SplashScreen extends Activity {
	

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        new PrefetchData().execute(); 
    }
 
    private class PrefetchData extends AsyncTask<Void, Void, Void> {
    	
    	private byte[] icon;
 
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
 
        @Override
        protected Void doInBackground(Void... arg0) {
        	
        	DataBaseHandler db = new DataBaseHandler(SplashScreen.this);
			if( db.getAllQuiz(true) == null){
		    	Log.d("Insert: ", "Inserting quiz..");
		    	Categorie categorieLangue = new Categorie (1, "Langues");
		        Quiz anglais = new Quiz("Anglais", "LexiQuiz", categorieLangue.getId());
		        Quiz allemand = new Quiz("Allemand", "LexiQuiz", categorieLangue.getId());
		        Quiz espagnol = new Quiz("Espagnol", "LexiQuiz", categorieLangue.getId());
		        Quiz portugais = new Quiz("Portugais", "LexiQuiz", categorieLangue.getId());
		        Quiz italien = new Quiz("Italien", "LexiQuiz", categorieLangue.getId());
//		        Quiz latin = new Quiz("Latin", "LexiQuiz");
		        
		        insertIcon(R.drawable.drapeau_angleterre);
		        anglais.setIcon(icon);
		        
		        insertIcon(R.drawable.drapeau_allemand);
		        allemand.setIcon(icon);
		        
		        insertIcon(R.drawable.drapeau_espagne);
		        espagnol.setIcon(icon);
		        
		        insertIcon(R.drawable.drapeau_portugal);
		        portugais.setIcon(icon);
		        
		        insertIcon(R.drawable.drapeau_italie);
		        italien.setIcon(icon);
		        
//		        insertIcon(R.drawable.listviewquiz);
//		        latin.setIcon(icon);
		    	
		    	db.addQuiz(anglais);
		    	db.addQuiz(allemand);
		    	db.addQuiz(espagnol);
		    	db.addQuiz(portugais);
		    	db.addQuiz(italien);
//		    	db.addQuiz(latin);

//		    	db.insertList_Questions(6, db.readCsv(SplashScreen.this, "latin.csv"), 1, false); // Fonctionne sauf encodages
		        db.insertList_Questions(1, db.readCsv(SplashScreen.this, "anglais.csv"), 1, true);
		        db.insertList_Questions(1, db.readCsv(SplashScreen.this, "verbes-irreguliers-anglais.csv"), 1, false);
		        db.insertList_Questions(2, db.readCsv(SplashScreen.this, "verbes-irreguliers-allemands.csv"), 1, false);
		        db.insertList_Questions(5, db.readCsv(SplashScreen.this, "verbes-irreguliers-italiens.csv"), 1, false);
			}
            return null;
        }
        
        public void insertIcon(int idDrawable){
        	Resources res = getResources();
	        Drawable drawable = res.getDrawable(idDrawable);
	        BitmapDrawable bitDw = ((BitmapDrawable) drawable);
	        Bitmap bitmap = bitDw.getBitmap();
	        ByteArrayOutputStream stream = new ByteArrayOutputStream();
	        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
	        icon = stream.toByteArray();
        }
 
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            Intent i = new Intent(SplashScreen.this, Menu.class);
            startActivity(i);
            finish();
        }
    }
}