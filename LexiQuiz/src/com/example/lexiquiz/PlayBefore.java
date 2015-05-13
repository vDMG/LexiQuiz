package com.example.lexiquiz;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import database.DataBaseHandler;

public class PlayBefore extends Activity{
	
	private int idQuiz;
	private DataBaseHandler db = new DataBaseHandler(this);
	private TextView txt_nbquestion;
    private ImageView img_icon;
	
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        setContentView(R.layout.play_before);
        
        this.idQuiz = Integer.parseInt(extras.getString("id"));
        setTitle(this.db.getQuiz(this.idQuiz).getTitre());
        
        setButtonAndText();

        
        if(this.db.getQuiz(this.idQuiz).getIcon().equals("uneImage")){
        	
        }
        else{
            Bitmap bitMapImage = BitmapFactory.decodeByteArray(
            		this.db.getQuiz(this.idQuiz).getIcon(), 0,
            		this.db.getQuiz(this.idQuiz).getIcon().length);
            
            img_icon.setImageBitmap(getRoundedCornerBitmap(bitMapImage));
        }

        double pourcentageRealise;
        double allquestions = this.db.countQuestion(this.idQuiz, 0);
        double questionsRealisees = this.db.countQuestion(this.idQuiz, 3);
        
        if(allquestions == 0){
            txt_nbquestion.setText("Oups ! Ce quiz ne contient aucune question !");
        }
        else if(questionsRealisees == 0) txt_nbquestion.setText("0% acquis");
        else if(allquestions == questionsRealisees) txt_nbquestion.setText("100% acquis");        
        else{
        	pourcentageRealise = (questionsRealisees / allquestions) * 100;
            java.text.DecimalFormat df = new java.text.DecimalFormat("0.##");        
            txt_nbquestion.setText(df.format(pourcentageRealise) + "% connaissances acquises");
        }
        
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
        txt_nbquestion = (TextView) findViewById(R.id.textView1);
        img_icon = (ImageView) findViewById(R.id.img_icon_quiz_before);
	}
	
	public Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
	    Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
	    Canvas canvas = new Canvas(output);

	    final int color = 0xff4242DB;
	    final Paint paint = new Paint();
	    Toast.makeText(getApplicationContext(), Integer.toString(bitmap.getHeight())+"/"+Integer.toString(bitmap.getWidth()),
				Toast.LENGTH_LONG).show();
	    final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
	    final RectF rectF = new RectF(rect);
	    final float roundPx = 200;

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
