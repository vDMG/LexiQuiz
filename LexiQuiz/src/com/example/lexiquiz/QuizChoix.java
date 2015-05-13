package com.example.lexiquiz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import database.DataBaseHandler;
import database.Quiz;

public class QuizChoix extends Activity {
	

	private List<HashMap<String, Object>> aList = new ArrayList<HashMap<String, Object>>();
	private DataBaseHandler db = new DataBaseHandler(this);
	
	public boolean onCreateOptionsMenu(android.view.Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main_activity_actions, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
		Intent intent;
	    switch (item.getItemId()) {
	        case R.id.action_createquiz:
	            System.out.println("createquiz");
	            intent = new Intent(getApplicationContext(), QuizCreate.class);
	            startActivity(intent);
	            return true;
	        case R.id.action_downloadquiz:
	            System.out.println("downloadquiz");
	            intent = new Intent(getApplicationContext(), AmisDownload.class);
	            intent.putExtra("type", "get.php");
	        	startActivity(intent);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.quiz_choix);
		
		setTitle("Liste des Lexiquiz");
		
		SimpleAdapter adapter = null;
		
		java.text.DecimalFormat df = new java.text.DecimalFormat("0.##");
		
		List<Quiz> all = db.getAllQuiz(true);
		if(all != null){
			for (Quiz quiz : all) {
				HashMap<String, Object> hm = new HashMap<String, Object>();
				hm.put("titre", quiz.getTitre());
				hm.put("auteur", quiz.getAuteur());
				double knowledgeLevel = db.getKnowledgeQuizz(quiz);
				if(knowledgeLevel==-1) hm.put("knowledgeLevel","(Aucune question)");
				else hm.put("knowledgeLevel", df.format(db.getKnowledgeQuizz(quiz))+"%");
				
				Bitmap bitMapImage = BitmapFactory.decodeByteArray(
						quiz.getIcon(), 0,
						quiz.getIcon().length);
				
				BitmapDrawable img = new BitmapDrawable(getResources(), getRoundedCornerBitmap(bitMapImage));
				
				hm.put("img", img);				
				
				hm.put("id", "" + quiz.getId());
				aList.add(hm);
			}
			String[] from = { "img", "titre", "auteur", "knowledgeLevel"};
			int[] to = { R.id.img_icon, R.id.txt_titre_simplerow, R.id.txt_auteur_simplerow , R.id.txt_knowledgeLevel_simplerow};
			adapter = new SimpleAdapter(getBaseContext(), aList, R.layout.simplerow, from, to);
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
				public void onItemClick(AdapterView<?> parent, View container, int position, long id) {
					String idQuiz = (String) aList.get(position).get("id");
					Intent intent = new Intent(getApplicationContext(), PlayBefore.class);
					intent.putExtra("id", idQuiz);
					startActivity(intent);
				}
			};
			listQuiz.setOnItemClickListener(itemClickListener);
		}
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
