/*
 *  This work is licensed for the ULB Group13 BKT for the DBSA course.
 */
package fr.univ_tours.etu.evaluation;

import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author Anas Alba
 */
class EvalUnrankedQuery {

    private String query;
    private Set<String> expectedResults;

    public EvalUnrankedQuery(String query) {
        this.query = query;
        this.expectedResults = new TreeSet<>();
    }

    public EvalUnrankedQuery(String query, Set<String> results) {
        this.query = query;
        this.expectedResults = results;

    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Set<String> getResults() {
        return expectedResults;
    }

    public void setResults(Set<String> results) {
        this.expectedResults = results;
    }

    public boolean isEmpty() {
        return expectedResults.isEmpty();
    }

    public boolean add(String e) {
        return expectedResults.add(e);
    }
}
