package com.example.lexiquiz;

import java.io.ByteArrayOutputStream;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputFilter.LengthFilter;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import database.DataBaseHandler;
import database.Quiz;

@SuppressLint("DefaultLocale")
public class QuizCreate extends Activity {

	private Quiz quiz;	
	private DataBaseHandler db = new DataBaseHandler(this);
	private EditText etxt_for_title;
	private EditText etxt_for_auteur;
	private Button btn_submit_in_create;
	private TextView txt_creez_votre_quiz;

	/*UPLOAD ICON REQUIREMENTS*/
	private static int RESULT_LOAD_IMAGE = 1;
	private boolean action = false;
	private ImageView icon_quiz;
	String picturePath;

	/*
	 * ACTIONBAR 
	 * Validation 
	 */
	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.creation_activity_actions, menu);
		return super.onCreateOptionsMenu(menu);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.action_validation:
			System.out.println("createquiz");
			creation();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}


	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.creationquiz);
		setTitle("Nouveau Lexiquiz");
		setButtonAndText();
	}

	/**
	 * Création d'un quiz
	 * @param view le bouton submit
	 */
	public void creation(){
		Intent intent;
		this.quiz = new Quiz(etxt_for_title.getText().toString(), etxt_for_auteur.getText().toString(),1);

		String titre = etxt_for_title.getText().toString();
		if(this.db.getQuizByTitle(titre) == null){
			if(picturePath!=null){
				//            BitmapDrawable bitDw = ((BitmapDrawable) d);
	//            Bitmap bitmap = bitDw.getBitmap();
	            Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
	            ByteArrayOutputStream stream = new ByteArrayOutputStream();
	            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
	            byte[] imageInByte = stream.toByteArray();
	            this.quiz.setIcon(imageInByte);
			}else{
				Resources res = getResources();
				Drawable drawable = res.getDrawable(R.drawable.listviewquiz);
				BitmapDrawable bitDw = ((BitmapDrawable) drawable);
				Bitmap bitmap = bitDw.getBitmap();
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
				byte[] icon = stream.toByteArray();
				quiz.setIcon(icon);
			}
			
			
			
			if(quiz.getIcon().length>500000)
				Toast.makeText(getApplicationContext(), R.string.info_oversize,
						Toast.LENGTH_LONG).show();
			else{
				this.db.addQuiz(quiz);
				Toast.makeText(getApplicationContext(), R.string.done_createquiz,
						Toast.LENGTH_LONG).show();
				//Retour au menu des quiz.
				intent = new Intent(getApplicationContext(), QuizChoix.class);
				startActivity(intent);
			}

		}
		else txt_creez_votre_quiz.setText("Ce titre existe deja.");
	}	

	/**
	 * Methode qui change la vue. 
	 * Rend tout invisible sauf la notification de création.
	 */
	public void invisible(){
		etxt_for_title.setVisibility(View.INVISIBLE);
		etxt_for_auteur.setVisibility(View.INVISIBLE);
		//btn_submit_in_create.setVisibility(View.INVISIBLE);
	}

	/**
	 * Methode qui permet de revenir en arriere au click
	 * @param view le bouton retour
	 */
	public void back(View view){
		this.db.close();
		Intent intent = new Intent(getApplicationContext(), QuizChoix.class);
		startActivity(intent);
	}

	/**
	 * Methode qui initialise les elements de la vue.
	 * Appelee des la creation de QuizCreate.
	 */
	public void setButtonAndText(){
		etxt_for_title = (EditText) findViewById(R.id.etxt_for_title);
		etxt_for_auteur = (EditText) findViewById(R.id.etxt_for_auteur);
		icon_quiz = (ImageView) findViewById(R.id.icon_quiz);
		//btn_submit_in_create = (Button) findViewById(R.id.btn_submit_in_create);
		//txt_creez_votre_quiz = (TextView) findViewById(R.id.txt_creez_votre_quiz);
	}

	/**
	 * Methode qui upload une photo de la galerie du telephone et l'insert dans le quiz.
	 * @param view le bouton upload
	 */
	public void upload(View view){
		Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(i, RESULT_LOAD_IMAGE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };
			Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
			cursor.moveToFirst();
			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			picturePath = cursor.getString(columnIndex);
			cursor.close();
			
			Bitmap pq=Bitmap.createScaledBitmap(BitmapFactory.decodeFile(picturePath), 
					icon_quiz.getWidth(), icon_quiz.getHeight(), true);
		    
			icon_quiz.setImageBitmap(getRoundedCornerBitmap(pq));
			
			//icon_quiz.
			/*
//	            Drawable d = img_icon_in_icon_upload.getBackground();
//	            BitmapDrawable bitDw = ((BitmapDrawable) d);
//	            Bitmap bitmap = bitDw.getBitmap();
	            Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
	            ByteArrayOutputStream stream = new ByteArrayOutputStream();
	            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
	            byte[] imageInByte = stream.toByteArray();
	            //this.quiz.setIcon(imageInByte);
	            //this.db.updateQuiz(quiz);*/
		}
		this.action = true;

	}
	
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
	    Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
	            .getHeight(), Config.ARGB_8888);
	    Canvas canvas = new Canvas(output);

	    final int color = 0xff4242DB;
	    final Paint paint = new Paint();
	    final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
	    final RectF rectF = new RectF(rect);
	    final float roundPx = bitmap.getWidth()/2;

	    paint.setAntiAlias(true);
	    canvas.drawARGB(0, 0, 0, 0);
	    paint.setColor(color);
	    canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

	    paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
	        //canvas.drawCircle(0, 0, bitmap.getWidth(), paint);
	    canvas.drawBitmap(bitmap, rect, rect, paint);

	    return output;
	}
}
