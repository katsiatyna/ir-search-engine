package fr.univ_tours.etu.search;

/**
 * Created by Katherine on 20.03.2016.
 */
public class SearchForm {

    protected String mainQuery;
    protected String titleQuery;
    protected String authorQuery;
    protected String keywordsQuery;
    protected boolean useQueryExpansion = true;
    protected boolean useWordNet = true;
    protected Integer year;
    protected String operator;

    public String getMainQuery() {
        return mainQuery;
    }

    public void setMainQuery(String mainQuery) {
        this.mainQuery = mainQuery;
    }

    public String getTitleQuery() {
        return titleQuery;
    }

    public void setTitleQuery(String titleQuery) {
        this.titleQuery = titleQuery;
    }

    public String getAuthorQuery() {
        return authorQuery;
    }

    public void setAuthorQuery(String authorQuery) {
        this.authorQuery = authorQuery;
    }

    public String getKeywordsQuery() {
        return keywordsQuery;
    }

    public void setKeywordsQuery(String keywordsQuery) {
        this.keywordsQuery = keywordsQuery;
    }

    public boolean isUseQueryExpansion() {
        return useQueryExpansion;
    }

    public void setUseQueryExpansion(boolean useQueryExpansion) {
        this.useQueryExpansion = useQueryExpansion;
    }

    public boolean isUseWordNet() {
        return useWordNet;
    }

    public void setUseWordNet(boolean useWordNet) {
        this.useWordNet = useWordNet;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }
}
