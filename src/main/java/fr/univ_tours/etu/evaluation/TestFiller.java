/*
 *  This work is licensed for the ULB Group13 BKT for the DBSA course.
 */

package fr.univ_tours.etu.evaluation;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author Anas Alba
 */
public class TestFiller {
    public static List<EvalUnrankedQuery> fillUnranked() {
        
        List<EvalUnrankedQuery> testSet = new ArrayList<>();
        
        EvalUnrankedQuery q1 = new EvalUnrankedQuery("language",  new TreeSet<>());
        Set<String> e1 = new TreeSet<>();
        e1.add("006");e1.add("019");e1.add("024");e1.add("025");e1.add("027");e1.add("037");
        q1.setResults(e1);
        testSet.add(q1);
        
        
        
        return testSet;
    }
//    language
//6
//19
//24
//25
//27
//37

    
    
}
