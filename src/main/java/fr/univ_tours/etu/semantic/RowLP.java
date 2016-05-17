package fr.univ_tours.etu.semantic;

public class RowLP {
	
	private int year;
	private String publicationLink;
	private String publicationTitle;
	private String authorLink;
	private String authorName;
	
	
	public RowLP(int year, String publicationLink, String publicationTitle, String authorLink, String authorName) {
		super();
		this.year = year;
		this.publicationLink = publicationLink;
		this.publicationTitle = publicationTitle;
		this.authorLink = authorLink;
		this.authorName = authorName;
	}


	public int getYear() {
		return year;
	}


	public String getPublicationLink() {
		return publicationLink;
	}


	public String getPublicationTitle() {
		return publicationTitle;
	}


	public String getAuthorLink() {
		return authorLink;
	}


	public String getAuthorName() {
		return authorName;
	}
	
	
	
	
	

}
