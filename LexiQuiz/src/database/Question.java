package database;

public class Question {
	
	private int id;
	private int idQuiz;
	private String enonce;
	private String reponse;
	private int niveau;

	public Question(int idQuiz, String enonce, String reponse, int niveau) {
		this.idQuiz = idQuiz;
		this.enonce = enonce;
		this.reponse = reponse;
		this.niveau = niveau;
	}
	
	public String toString(){
		return "id: " + this.id + " idQuiz: " + this.idQuiz + "\n"
				+ "Enonce: " + this.enonce + " Reponse: " + this.reponse + "\n" 
				+ "Niveau: " + this.niveau + "\n";
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getNiveau() {
		return niveau;
	}
	public void setNiveau(int niveau) {
		this.niveau = niveau;
	}
	public String getEnonce() {
		return enonce;
	}
	public void setEnonce(String enonce) {
		this.enonce = enonce;
	}
	public String getReponse() {
		return reponse;
	}
	public void setReponse(String reponse) {
		this.reponse = reponse;
	}
	public int getIdQuiz() {
		return idQuiz;
	}
	public void setIdQuiz(int idQuiz) {
		this.idQuiz = idQuiz;
	}
}
