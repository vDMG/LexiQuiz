package com.example.lexiquiz;

import java.io.ByteArrayOutputStream;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import database.DataBaseHandler;
import database.Quiz;

public class IconUpload extends Activity {
    
	private static int RESULT_LOAD_IMAGE = 1;
	private int idQuiz;	
	private Quiz quiz;	
	private DataBaseHandler db = new DataBaseHandler(this);
    private ImageView img_icon_in_icon_upload;
    private Button btn_upload_for_icon;
    private boolean action = false;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.icon_upload);
      	setButtonAndText();
        Bundle extras = getIntent().getExtras();
        
        this.idQuiz = Integer.parseInt(extras.getString("id")); 
        this.quiz = this.db.getQuiz(this.idQuiz);
		Bitmap bitMapImage = BitmapFactory.decodeByteArray(this.quiz.getIcon(), 0, this.quiz.getIcon().length);
        img_icon_in_icon_upload.setImageBitmap(bitMapImage);
	}

	/**
	 * Methode qui upload une photo de la galerie du telephone et l'insert dans le quiz.
	 * @param view le bouton upload
	 */
	public void upload(View view){
		if(!action){
	        Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
	        startActivityForResult(i, RESULT_LOAD_IMAGE);
		}
		else {
	    	Intent intent = new Intent(getApplicationContext(), QuizMenu.class);
	        intent.putExtra("id", Integer.toString(this.idQuiz));
	        startActivity(intent);
		}
	}	
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(!action){
	        super.onActivityResult(requestCode, resultCode, data);
	        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
	            Uri selectedImage = data.getData();
	            String[] filePathColumn = { MediaStore.Images.Media.DATA };
	            Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
	            cursor.moveToFirst();
	            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
	            String picturePath = cursor.getString(columnIndex);
	            cursor.close();
	            img_icon_in_icon_upload.setImageBitmap(BitmapFactory.decodeFile(picturePath));
	            
//	            Drawable d = img_icon_in_icon_upload.getBackground();
//	            BitmapDrawable bitDw = ((BitmapDrawable) d);
//	            Bitmap bitmap = bitDw.getBitmap();
	            Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
	            ByteArrayOutputStream stream = new ByteArrayOutputStream();
	            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
	            byte[] imageInByte = stream.toByteArray();
	            this.quiz.setIcon(imageInByte);
	            this.db.updateQuiz(quiz);
	            btn_upload_for_icon.setText("Valider");
	        }
	        this.action = true;
		}	 
		else{
		}
    }
	
	/**
	 * Methode qui initialise les elements de la vue.
	 */
	public void setButtonAndText(){
		img_icon_in_icon_upload = (ImageView) findViewById(R.id.img_icon_in_icon_upload);
		btn_upload_for_icon = (Button) findViewById(R.id.btn_upload_for_icon);
	}
}
