package com.example.lexiquiz;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import database.Categorie;
import database.DataBaseHandler;
import database.Quiz;

public class PlayBefore extends Activity{
	
	private static Quiz quiz;
	private DataBaseHandler db = new DataBaseHandler(this);
	private TextView textview_description;
	private TextView textview_author;
    private ImageView img_icon;
    
    @Override
    public void onWindowFocusChanged(boolean hasFocus){
    	
    	Bundle extras = getIntent().getExtras();
    	Quiz quiz = this.db.getQuiz(Integer.parseInt(extras.getString("id")));
    	img_icon = (ImageView) findViewById(R.id.img_icon_quiz_before);
    	
        Bitmap pq=Bitmap.createScaledBitmap(BitmapFactory.decodeByteArray(quiz.getIcon(), 0,
        		quiz.getIcon().length), 
				img_icon.getWidth(), img_icon.getHeight(), true);
        
        img_icon.setImageBitmap(getRoundedCornerBitmap(pq));
    }
	
    
 	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        setContentView(R.layout.play_before);
        
        Quiz quiz = this.db.getQuiz(Integer.parseInt(extras.getString("id")));
        Categorie categorie = this.db.getCategorie(quiz.getIdcategorie());
        
        setTitle(quiz.getTitre());
        setButtonAndText();
        textview_author.setText(quiz.getAuteur());
        textview_description.setText(categorie.getCategorie());
        


        double pourcentageRealise;
        double allquestions = this.db.countQuestion(quiz.getId(), 0);
        double questionsRealisees = this.db.countQuestion(quiz.getId(), 3);
        
        /*if(allquestions == 0){
            textview_description.setText("Oups ! Ce quiz ne contient aucune question !");
        }
        else if(questionsRealisees == 0) textview_description.setText("0% acquis");
        else if(allquestions == questionsRealisees) textview_description.setText("100% acquis");        
        else{
        	pourcentageRealise = (questionsRealisees / allquestions) * 100;
            java.text.DecimalFormat df = new java.text.DecimalFormat("0.##");        
            textview_description.setText(df.format(pourcentageRealise) + "% connaissances acquises");
        }*/
        

        
    }

	/**
	 * Methode qui permet de lancer le quiz
	 * @param view le bouton lancer
	 */
	public void lancer(View view){
    	this.db.close();
    	Intent intent = new Intent(getApplicationContext(), Play.class);
        intent.putExtra("id", Integer.toString(quiz.getId()));
        startActivity(intent);
	}
		
	/**
	 * Methode qui initialise les elements de la vue.
	 * Appelee des la creation de PlayBefore.
	 */
	public void setButtonAndText(){
        textview_description = (TextView) findViewById(R.id.textView1);
        textview_author = (TextView) findViewById(R.id.textView2);
        
	}
	
	public Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
	    Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
	    Canvas canvas = new Canvas(output);

	    final int color = 0xff4242DB;
	    final Paint paint = new Paint();
	    
	    final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
	    final RectF rectF = new RectF(rect);
	    final float roundPx = bitmap.getWidth()/2;

	    paint.setAntiAlias(true);
	    canvas.drawARGB(0, 0, 0, 0);
	    paint.setColor(color);
	    //canvas.drawCircle(bitmap.getWidth()/2, bitmap.getHeight()/2, bitmap.getWidth()/2, paint);
	    canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

	    paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
	        //canvas.drawCircle(0, 0, bitmap.getWidth(), paint);
	    canvas.drawBitmap(bitmap, rect, rect, paint);

	    return output;
	}
}
