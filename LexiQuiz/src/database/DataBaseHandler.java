package database;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import au.com.bytecode.opencsv.CSVReader;

public class DataBaseHandler extends SQLiteOpenHelper {
	
	private static final int DATABASE_VERSION = 1;
	
	private static final String DATABASE_NAME = "TABLE_LEXIQUIZ";
	
	private static final String TABLE_QUIZ = "quiz";
	private static final String TABLE_QUESTION = "question";
	private static final String TABLE_CATEGORIE = "categorie";
	
	private static final String KEY_CATEGORIE = "categorie";
	
	private static final String KEY_ID = "id";
	private static final String KEY_TITRE = "titre";
	private static final String KEY_AUTEUR = "auteur";
	private static final String KEY_ID_CATEGORIE = "idCategorie";
	private static final String KEY_ICON = "icon";

	private static final String KEY_ID_QUIZ = "idQuiz";
	private static final String KEY_ENONCE = "enonce";
	private static final String KEY_REPONSE = "reponse";
	private static final String KEY_NIVEAU = "niveau";
	
	public DataBaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + TABLE_CATEGORIE + "("
	            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," 
	 			+ KEY_CATEGORIE +" TEXT)");
	 	db.execSQL("CREATE TABLE " + TABLE_QUIZ + "("
	            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," 
				+ KEY_TITRE +" TEXT," 
	            + KEY_AUTEUR +" TEXT,"
	            + KEY_ID_CATEGORIE +" INT,"
	 			+ KEY_ICON +" BLOB)");
	 	db.execSQL("CREATE TABLE " + TABLE_QUESTION + "("
	            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," 
	            + KEY_ID_QUIZ + " INT,"
	            + KEY_ENONCE +" TEXT," 
	            + KEY_REPONSE +" TEXT,"
	            + KEY_NIVEAU +" INT)");
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUIZ);
		onCreate(db);
	}
	
	/**
	 * Lit le fichier csv en parametre et retourne une liste contenant toutes les cellules.
	 * @param context le context est nécessaire pour récupperer les fichier csv.
	 * @param chemin_fichier le nom du fichier csv à lire
	 * @return List<String[]> une liste d'énoncé et de réponse.
	 */
	public final List<String[]> readCsv(Context context, String chemin_fichier) {
	  List<String[]> listeEnonce = new ArrayList<String[]>();
	  AssetManager assetManager = context.getAssets();

	  try {
	    InputStream csvStream = assetManager.open(chemin_fichier);
	    InputStreamReader csvStreamReader = new InputStreamReader(csvStream);
	    CSVReader csvReader = new CSVReader(csvStreamReader);
	    String[] line;
		    
	    csvReader.readNext();
		    
	    while ((line = csvReader.readNext()) != null) {
	    	listeEnonce.add(line);
	    }
	    csvReader.close();
	  } catch (IOException e) {
	    e.printStackTrace();
	  }
	  return listeEnonce;
	}
	
	
	/*
	 * 
	 * Toutes les méthodes sur QUESTION
	 * 
	 */
	
	/**
	 * Insert toutes les Questions dans la base de données.
	 * @param idQuiz l'id du quiz où il faut inserer les questions.
	 * @param l la liste des enonces et des réponses.
	 * @param niveau le niveau qui sera attribué a toutes les questions.
	 * @param ordreAttributs si dans la liste en param il y d'abord les enoncés puis les réponses ou l'inverse.
	 */
	public void insertList_Questions(int idQuiz, List<String[]> l, int niveau, boolean ordreAttributs){
		SQLiteDatabase db = this.getWritableDatabase();
	    ContentValues values = new ContentValues();
		String enonce = "";
		String reponse = "";
		for(int i = 0; i < l.size(); i++){
			for(int j = 0; j < l.get(i).length; j++){
				if(j < l.get(i).length ) reponse = l.get(i)[j];
				if(j  + 1 < l.get(i).length) System.out.println( l.get(i)[j + 1] );
				if(j  + 1 < l.get(i).length) enonce = l.get(i)[j + 1];
			    values.put(KEY_ID_QUIZ, idQuiz); 
			    if(ordreAttributs){
			    	values.put(KEY_ENONCE, enonce);
				    values.put(KEY_REPONSE, reponse);
			    }
			    else {
				    values.put(KEY_REPONSE, enonce);
			    	values.put(KEY_ENONCE, reponse);
			    }
			    values.put(KEY_NIVEAU, niveau);
			    db.insert(TABLE_QUESTION, null, values);
			    j += 1;
			}
		}
	}
	
	/**
	 * Modifie le niveau d'une question.
	 * @param idQuestion l'id de la question à modifié.
	 * @param niveau le niveau à mettre à la question.
	 * @return int 1 si l'update à correctement fonctionné.
	 */
	public int updateNiveau(int idQuestion, int niveau){
		SQLiteDatabase db = this.getWritableDatabase();
	    
		Question questionBefore = this.getQuestion(idQuestion);
		System.out.println("BEFORE: " + questionBefore.toString());
		
		ContentValues values = new ContentValues();
	    
	    values.put(KEY_NIVEAU, niveau);
	    int result = db.update(TABLE_QUESTION, values, KEY_ID + " = ?",
	            new String[] { String.valueOf(idQuestion) });
	    
	    Question questionAfter = this.getQuestion(idQuestion);
		System.out.println("AFTER: " + questionAfter.toString());
	    
	    return result;
	}
	
	/**
	 * Ajoute une Question à la base de données
	 * @param question la question à rajouter.
	 */
	public void addQuestion(Question question) {
		SQLiteDatabase db = this.getWritableDatabase();
	    
		ContentValues values = new ContentValues();
	    
	    values.put(KEY_ID_QUIZ, question.getIdQuiz());
	    values.put(KEY_ENONCE, question.getEnonce()); 
	    values.put(KEY_REPONSE, question.getReponse()); 
	    values.put(KEY_NIVEAU, question.getNiveau());
	    
	    db.insert(TABLE_QUESTION, null, values);
	}
	
	/**
	 * Renvoi une Question de la base de données selon l'id reçu en parametre.
	 * @param id l'id de la question à renvoyé.
	 * @return Question la question à renvoyé ou null si aucun résultat.
	 */
	public Question getQuestion(int id) {
		SQLiteDatabase db = this.getReadableDatabase();
	    
		Cursor cursor = db.query(TABLE_QUESTION, new String[] { KEY_ID, KEY_ID_QUIZ,
	            KEY_ENONCE, KEY_REPONSE, KEY_NIVEAU }, KEY_ID + "=?",
	            new String[] { String.valueOf(id) }, null, null, null, null);
	   
		if (cursor.getCount() == 0){
			cursor.close();
			return null;
		}
		cursor.moveToFirst();
	    
	    int idQuiz = Integer.parseInt(cursor.getString(1));
		String enonce = cursor.getString(2);
		String reponse = cursor.getString(3);
		int niveau = Integer.parseInt(cursor.getString(4));
		
    	Question question = new Question(idQuiz, enonce, reponse, niveau);
        
    	question.setId(Integer.parseInt(cursor.getString(0)));
    	
    	cursor.close();
    	return question;
	}
	 
	/**
	 * Renvoi toutes les Questions d'un quiz selon l'idQuiz reçu en parametre et le Niveau.
	 * @param idQuiz le quiz dont on prend toutes les questions.
	 * @param niveau le niveau que doit avoir les questions renvoyé.
	 * @return List<Question> une liste de toutes les questions à renvoyé. Ou null si aucun résultat.
	 */
	public List<Question> getAllQuestion(int idQuiz, int niveau) {
		List<Question> listeQuestions = new ArrayList<Question>();
	    
		String selectQuery = "SELECT  * FROM " + TABLE_QUESTION + " WHERE " + KEY_ID_QUIZ + " = " + idQuiz
	    		+ " AND " + KEY_NIVEAU + " = " + niveau;
	    
	    SQLiteDatabase db = this.getWritableDatabase();
	    
	    Cursor cursor = db.rawQuery(selectQuery, null);
	    
		if (cursor.getCount() == 0){
			cursor.close();
			return null;
		}

	    if (cursor.moveToFirst()) {
	        do {
				String enonce = cursor.getString(2);
				String reponse = cursor.getString(3);
	        	
				Question question = new Question(idQuiz, enonce, reponse, niveau);
	        	
				question.setId(Integer.parseInt(cursor.getString(0)));
	            
				listeQuestions.add(question);
	        } while (cursor.moveToNext());
	    }
	    cursor.close();
	    return listeQuestions;
	}
	
	/**
	 * Renvoi toutes les Questions d'un quiz selon l'idQuiz reçu en parametre.
	 * @param idQuiz le quiz dont on prend toutes les questions.
	 * @return List<Question> une liste de toutes les questions à renvoyé. Ou null si aucun résultat.
	 */
	public List<Question> getAllQuestionSansNiveau(int idQuiz) {
		List<Question> listeQuestions = new ArrayList<Question>();
	    
		String selectQuery = "SELECT  * FROM " + TABLE_QUESTION + " WHERE " + KEY_ID_QUIZ + " = " + idQuiz;
	    
	    SQLiteDatabase db = this.getWritableDatabase();
	    
	    Cursor cursor = db.rawQuery(selectQuery, null);
	    
		if (cursor.getCount() == 0){
			cursor.close();
			return null;
		}

	    if (cursor.moveToFirst()) {
	        do {
				String enonce = cursor.getString(2);
				String reponse = cursor.getString(3);
				int niveau = Integer.parseInt(cursor.getString(4));
	        	
				Question question = new Question(idQuiz, enonce, reponse, niveau);
	        	
				question.setId(Integer.parseInt(cursor.getString(0)));
	            
				listeQuestions.add(question);
	        } while (cursor.moveToNext());
	    }
	    cursor.close();
	    return listeQuestions;
	}
	
	/**
	 * Renvoi toutes les Questions d'un quiz selon l'idQuiz reçu en parametre.
	 * @param idQuiz le quiz dont on prend toutes les questions.
	 * @return List<Question> une liste de toutes les questions à renvoyé. Ou null si aucun résultat.
	 */
	public List<Question> getQuestionsForUpload(int idQuiz) {
		List<Question> listeQuestions = new ArrayList<Question>();
	    
		String selectQuery = "SELECT  * FROM " + TABLE_QUESTION + " WHERE " + KEY_ID_QUIZ + " = " + idQuiz;
	    
	    SQLiteDatabase db = this.getWritableDatabase();
	    
	    Cursor cursor = db.rawQuery(selectQuery, null);
	    
		if (cursor.getCount() == 0){
			cursor.close();
			return null;
		}

	    if (cursor.moveToFirst()) {
	        do {
				String enonce = cursor.getString(2);
				String reponse = cursor.getString(3);
				int niveau = Integer.parseInt(cursor.getString(4));
	        	
				Question question = new Question(idQuiz, enonce, reponse, niveau);
	        	
				question.setId(Integer.parseInt(cursor.getString(0)));
	            
				listeQuestions.add(question);
	        } while (cursor.moveToNext());
	    }
	    cursor.close();
	    
	    if(listeQuestions != null){
	    	for (Question question : listeQuestions) {
	    		String tmpEnonce = question.getEnonce();
				String tmpReponse = question.getReponse();
				try {
					tmpEnonce = URLEncoder.encode(tmpEnonce, "utf-8");
					tmpReponse = URLEncoder.encode(tmpReponse, "utf-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				question.setEnonce(tmpEnonce);
				question.setReponse(tmpReponse);
			}
	    }
	    return listeQuestions;
	}
	
	/**
	 * Renvoi le nombre de Questions dans un quiz selon le niveau reçu en parametre.
	 * Si reçoit 0 pour le niveau, renvoi toutes les questions.
	 * @param idQuiz l'id du quiz ou l'on calcule le nombre.
	 * @param niveau niveau des questions dont on calcule le nombre.
	 * @return int le nombre de questions.
	 */
	public int countQuestion(int idQuiz, int niveau) {
		int count = 0;
		if(niveau == 0){
			List<Question> all_1 = getAllQuestion(idQuiz, 1); 
			List<Question> all_2 = getAllQuestion(idQuiz, 2);
			List<Question> all_3 = getAllQuestion(idQuiz, 3);
			if(all_1 != null) count += all_1.size();
			if(all_2 != null) count += all_2.size();
			if(all_3 != null) count += all_3.size();
			return count;
		}
		List<Question> question = getAllQuestion(idQuiz, niveau);
		if(question != null) count = question.size();
		return count;
	}
	
	/**
	 * Modifie la question par celle reçu en parametre.
	 * @param question la question contenant les modification pour chaque attribut
	 * @return int 1 si l'update à correctement fonctionné.
	 */
	public int updateQuestion(Question question) {
		SQLiteDatabase db = this.getWritableDatabase();
		System.out.println("BEFORE: " + this.getQuestion(question.getId()).toString());
		
		ContentValues values = new ContentValues();
	    
	    values.put(KEY_ENONCE, question.getEnonce());
	    values.put(KEY_REPONSE, question.getReponse());
	    values.put(KEY_NIVEAU, question.getNiveau());
	    int result = db.update(TABLE_QUESTION, values, KEY_ID + " = ?",
	            new String[] { String.valueOf(question.getId()) });
		System.out.println("AFTER: " + this.getQuestion(question.getId()).toString());
	    return result;
	}
	
	/**
	 * Supprime une question de la base de données selon l'id reçu en parametre.
	 * @param id l'id de la question
	 */
	public void deleteQuestion(int id) {
	    SQLiteDatabase db = this.getWritableDatabase();
		System.out.println("BEFORE: " + this.getQuestion(id));	    
	    db.delete(TABLE_QUESTION, KEY_ID + " = ?",
	            new String[] { String.valueOf(id) });
		System.out.println("AFTER: " + this.getQuestion(id));
	}
	
	
	/*
	 * 
	 * Toutes les méthodes sur QUIZ
	 * 
	 */

	/**
	 * Ajoute un Quiz dans la base de données
	 * @param quiz Le quiz à ajouter à la base de données.
	 */
	public int addQuiz(Quiz quiz) {
		SQLiteDatabase db = this.getWritableDatabase();
	    
		ContentValues values = new ContentValues();
	    
	    values.put(KEY_TITRE, quiz.getTitre()); 
	    values.put(KEY_AUTEUR, quiz.getAuteur());
	    values.put(KEY_ID_CATEGORIE, quiz.getIdcategorie());
	    if(quiz.getIcon() == null) values.put(KEY_ICON, quiz.getIcon());
	    else values.put(KEY_ICON, quiz.getIcon());
	    
	    return (int) db.insert(TABLE_QUIZ, null, values);
	}
	
	/**
	 * Renvoi tous les quiz de la base de données.
	 * @param tous Si tous = false ne renvoi pas les quiz dont l'auteur est LexiQuiz
	 * @return Liste de quiz ou null si aucun résultat.
	 */
	public List<Quiz> getAllQuiz(boolean tous) {
		SQLiteDatabase db = this.getWritableDatabase();
		List<Quiz> quizList = new ArrayList<Quiz>();
		String selectQuery;
		
		if(tous == true) selectQuery = "SELECT  * FROM " + TABLE_QUIZ;
		else selectQuery = "SELECT  * FROM " + TABLE_QUIZ + " WHERE " + KEY_AUTEUR + " IS NOT 'LexiQuiz'";

		Cursor cursor = db.rawQuery(selectQuery, null);	
		
		if (cursor.getCount() == 0){
			cursor.close();
			return null;
		}
		if (cursor.moveToFirst()) {
			do {
				String titre = cursor.getString(1); 
				String auteur = cursor.getString(2);
				Quiz quiz = new Quiz(titre, auteur);
				quiz.setId(Integer.parseInt(cursor.getString(0)));
				quiz.setIcon(cursor.getBlob(3));
				quizList.add(quiz);
			} while (cursor.moveToNext());
		}
		cursor.close();
		return quizList;		
	}
	
	/**
	 * Renvoi un quiz selon l'id reçu en parametre.
	 * @param id l'id du quiz
	 * @return un quiz ou null si aucun quiz trouvé.
	 */
	public Quiz getQuiz(int id) {
		SQLiteDatabase db = this.getWritableDatabase();
	    
		Cursor cursor = db.query(TABLE_QUIZ, new String[] { KEY_ID, KEY_TITRE, KEY_AUTEUR, KEY_ID_CATEGORIE,
	            KEY_ICON}, KEY_ID + "=?", new String[] { String.valueOf(id) }, null, null, null, null);
	    
	    if (cursor.getCount() == 0){
	    	cursor.close();
	    	return null;
	    }
	    
	    cursor.moveToFirst();
	    String titre = cursor.getString(1); 
		String auteur = cursor.getString(2);
	 
		Quiz quiz = new Quiz(titre, auteur);	
		quiz.setId(Integer.parseInt(cursor.getString(0)));
		quiz.setIcon(cursor.getBlob(3));
		
		cursor.close();
		return quiz;
	}
	
	/**
	 * Renvoi un quiz selon l'id reçu en parametre.
	 * @param id l'id du quiz
	 * @return un quiz ou null si aucun quiz trouvé.
	 */
	public Quiz getQuizForUpload(int id) {
		SQLiteDatabase db = this.getWritableDatabase();
	    
		Cursor cursor = db.query(TABLE_QUIZ, new String[] { KEY_ID, KEY_TITRE, KEY_AUTEUR, KEY_ID_CATEGORIE,
	            KEY_ICON}, KEY_ID + "=?", new String[] { String.valueOf(id) }, null, null, null, null);
	    
	    if (cursor.getCount() == 0){
	    	cursor.close();
	    	return null;
	    }
	    
	    cursor.moveToFirst();
	    String titre = cursor.getString(1); 
		String auteur = cursor.getString(2);
	 
		Quiz quiz = new Quiz(titre, auteur);	
		quiz.setId(Integer.parseInt(cursor.getString(0)));
		quiz.setIcon(cursor.getBlob(3));
		
		String tmpTitre = quiz.getTitre();
		String tmpAuteur = quiz.getAuteur();
		try {
			tmpTitre = URLEncoder.encode(tmpTitre, "utf-8");
			tmpAuteur = URLEncoder.encode(tmpAuteur, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		quiz.setTitre(tmpTitre);
		quiz.setAuteur(tmpAuteur);
		
		cursor.close();
		return quiz;
	}
	
	/**
	 * Supprime un quiz de la base de données selon l'id reçu en parametre.
	 * @param id l'id du quiz
	 */
	public void deleteQuiz(int id) {
		System.out.println("BEFORE: " + this.getQuiz(id));
	    SQLiteDatabase db = this.getWritableDatabase();
	    
	    db.delete(TABLE_QUIZ, KEY_ID + " = ?",
	            new String[] { String.valueOf(id) });
		System.out.println("AFTER: " + this.getQuiz(id));
	}
	
	/**
	 * Renvoi un quiz selon son titre reçu en parametre.
	 * @param title le titre du quiz
	 * @return un quiz ou null si aucun quiz trouvé.
	 */
	public Quiz getQuizByTitle(String title) {
		SQLiteDatabase db = this.getWritableDatabase();
	    
		Cursor cursor = db.query(TABLE_QUIZ, new String[] { KEY_ID, KEY_TITRE, KEY_AUTEUR, KEY_ID_CATEGORIE,
	            KEY_ICON}, KEY_TITRE + "=?", new String[] { String.valueOf(title) }, null, null, null, null);
	    
	    if (cursor.getCount() == 0){
	    	cursor.close();
	    	return null;
	    }
	    
	    cursor.moveToFirst();
	    String titre = cursor.getString(1); 
		String auteur = cursor.getString(2);
	    
		Quiz quiz = new Quiz(titre, auteur);
		
		quiz.setId(Integer.parseInt(cursor.getString(0)));
		quiz.setIcon(cursor.getBlob(3));
		
		cursor.close();
		return quiz;
	}
	
	/**
	 * Modifie le quiz par celui reçu en parametre.
	 * @param quiz le quiz contenant les modification pour chaque attribut
	 * @return int 1 si l'update à correctement fonctionné.
	 */
	public int updateQuiz(Quiz quiz) {
		SQLiteDatabase db = this.getWritableDatabase();
		System.out.println("BEFORE: " + this.getQuiz(quiz.getId()).toString());
		
		ContentValues values = new ContentValues();
	    
	    values.put(KEY_TITRE, quiz.getTitre());
	    values.put(KEY_AUTEUR, quiz.getAuteur());
	    values.put(KEY_ID_CATEGORIE, quiz.getIdcategorie());
	    values.put(KEY_ICON, quiz.getIcon());
	    int result = db.update(TABLE_QUIZ, values, KEY_ID + " = ?",
	            new String[] { String.valueOf(quiz.getId()) });
		System.out.println("AFTER: " + this.getQuiz(quiz.getId()).toString());
	    return result;
	}
	
	/**
	 * Renvoie le pourcentage de connaissances d'un quiz
	 * @param quiz
	 * @return int, pourcentage de connaissance d'un quiz
	 */
	public double getKnowledgeQuizz(Quiz quiz){
		
		double pourcentageRealise;
        double nbAllquestions = this.countQuestion(quiz.getId(), 0);
        if(nbAllquestions==0)return -1;
        double questionsRealisees = this.countQuestion(quiz.getId(), 3);
        pourcentageRealise = (questionsRealisees / nbAllquestions) * 100;
        
        return pourcentageRealise;
        
	}
}

