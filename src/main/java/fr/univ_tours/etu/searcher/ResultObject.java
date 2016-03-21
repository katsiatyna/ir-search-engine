/*
 *  This work is licensed for the ULB Group13 BKT for the DBSA course.
 */

package fr.univ_tours.etu.searcher;

/**
 *
 * @author Anas Alba
 */
public class ResultObject {
   
    private Integer rank;
    private String title;
    private String authors;
    private String path;
    private String summary;

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public ResultObject(Integer rank, String title, String authors, String path, String summary) {
        this.rank = rank;
        this.title = title;
        this.authors = authors;
        this.path = path;
        this.summary = summary;
    }
}
