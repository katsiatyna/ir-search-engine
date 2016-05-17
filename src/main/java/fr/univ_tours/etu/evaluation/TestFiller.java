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

        EvalUnrankedQuery q2 = new EvalUnrankedQuery("programming language",  new TreeSet<>());
        Set<String> e2 = new TreeSet<>();
        e2.add("024");e2.add("025");e2.add("037");e2.add("005");e2.add("040");e2.add("061");e2.add("049");e2.add("057");e2.add("029");e2.add("001");
        q2.setResults(e2);
        testSet.add(q2);


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
