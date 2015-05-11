package database;

public class Quiz {
	
		private int id;
		private String titre;
		private String auteur;
		private int idcategorie;
		private byte[] icon;
		
		public Quiz(String titre, String auteur, int idcategorie) {
			this.titre = titre;
			this.auteur = auteur;
			this.idcategorie = idcategorie;		
		}

		public String toString(){
			return "id: " + this.id + " Titre: " + this.titre + " Auteur: " + this.auteur + "\n" ;
		}

		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}

		public String getAuteur() {
			return this.auteur;
		}

		public void setAuteur(String auteur) {
			this.auteur = auteur;
		}

		public String getTitre() {
			return this.titre;
		}

		public void setTitre(String titre) {
			this.titre = titre;
		}

		public byte[] getIcon() {
			
			 return this.icon;
		}

		public void setIcon(byte[] imageInByte) {
			this.icon = imageInByte;
		}

		public int getIdcategorie() {
			return idcategorie;
		}

		public void setIdcategorie(int idcategorie) {
			this.idcategorie = idcategorie;
		}
		
	}