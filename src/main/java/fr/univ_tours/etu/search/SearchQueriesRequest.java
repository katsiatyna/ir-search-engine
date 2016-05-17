package fr.univ_tours.etu.search;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Katherine on 21.03.2016.
 */
public class SearchQueriesRequest {

    protected Map<String, String> queriesDictionary;
    protected boolean useQueryExpansion;

    public SearchQueriesRequest(){
        queriesDictionary = new HashMap<>();
        useQueryExpansion = false;
    }

    public Map<String, String> getQueriesDictionary() {
        return queriesDictionary;
    }

    public void setQueriesDictionary(Map<String, String> queriesDictionary) {
        this.queriesDictionary = queriesDictionary;
    }

    public boolean isUseQueryExpansion() {
        return useQueryExpansion;
    }

    public void setUseQueryExpansion(boolean useQueryExpansion) {
        this.useQueryExpansion = useQueryExpansion;
    }
}
