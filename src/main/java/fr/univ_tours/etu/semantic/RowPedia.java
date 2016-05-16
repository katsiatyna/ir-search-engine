package fr.univ_tours.etu.semantic;

public class RowPedia {
	
	private String link;
	private String label;
	private String resource;
	private String summary;
	
	
	/**
	 * Row in DBPedia result table ->  1)link, 2)label, 3)resource, 4)summary
	 * @param link
	 * @param label
	 * @param resource
	 * @param summary
	 */
	public RowPedia(String link, String label, String resource, String summary) {
	
		this.link = link;
		this.label = label;
		this.resource = resource;
		this.summary = summary;
	}


	public String getLink() {
		return link;
	}


	public String getLabel() {
		return label;
	}


	public String getResource() {
		return resource;
	}


	public String getSummary() {
		return summary;
	}
	
	
	
	

}
