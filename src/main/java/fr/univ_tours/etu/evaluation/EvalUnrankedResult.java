/*
 *  This work is licensed for the ULB Group13 BKT for the DBSA course.
 */
package fr.univ_tours.etu.evaluation;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author Anas Alba
 */
class EvalUnrankedResult {

    Set<String> resutls;

    public EvalUnrankedResult() {
        this.resutls = new TreeSet<>();
    }

    public EvalUnrankedResult(Set<String> resutls) {
        this.resutls = resutls;
    }

    public Set<String> getResutls() {
        return resutls;
    }

    public void setResutls(Set<String> resutls) {
        this.resutls = resutls;
    }

    public boolean add(String e) {
        return resutls.add(e);
    }

    public boolean isEmpty() {
        return resutls.isEmpty();
    }


}
