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
}
