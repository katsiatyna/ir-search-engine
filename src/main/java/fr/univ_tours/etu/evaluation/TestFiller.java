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
        e2.add("024");e2.add("025");e2.add("037");e2.add("061");e2.add("049");e2.add("057");e2.add("029");e2.add("001");
        q2.setResults(e2);
        testSet.add(q2);

        EvalUnrankedQuery q3 = new EvalUnrankedQuery("J. Smith",  new TreeSet<>());
        Set<String> e3 = new TreeSet<>();
        e3.add("021");e3.add("022");e3.add("023");
        q3.setResults(e3);
        testSet.add(q3);

        EvalUnrankedQuery q4 = new EvalUnrankedQuery("who invented the airplane",  new TreeSet<>());
        Set<String> e4 = new TreeSet<>();
        e4.add("120");e4.add("121");e4.add("087");e4.add("026");
        q4.setResults(e4);
        testSet.add(q4);

//        EvalUnrankedQuery q5 = new EvalUnrankedQuery("volcano active",  new TreeSet<>());
//        Set<String> e5 = new TreeSet<>();
//        e5.add("184");e5.add("180");e5.add("185");e5.add("187");e5.add("186");e5.add("183");
//        q5.setResults(e5);
//        testSet.add(q5);

        EvalUnrankedQuery q6 = new EvalUnrankedQuery("world bank affiliated agency",  new TreeSet<>());
        Set<String> e6 = new TreeSet<>();
        e6.add("035");e6.add("034");e6.add("036");e6.add("159");e6.add("160");e6.add("165");e6.add("111");e6.add("069");e6.add("032");e6.add("033");
        e6.add("062");
        q6.setResults(e6);
        testSet.add(q6);

        EvalUnrankedQuery q7 = new EvalUnrankedQuery("scientist who found ancient excavations",  new TreeSet<>());
        Set<String> e7 = new TreeSet<>();
        e7.add("069");        e7.add("082");        e7.add("056");        e7.add("021");        e7.add("022");
        e7.add("048");        e7.add("079");        e7.add("087");        e7.add("089");        e7.add("090");
        e7.add("095");        e7.add("096");        e7.add("020");        e7.add("023");        e7.add("043");
        e7.add("044");        e7.add("045");        e7.add("046");        e7.add("068");        e7.add("080");
        e7.add("081");        e7.add("088");
        q7.setResults(e7);
        testSet.add(q7);
        
        EvalUnrankedQuery q8 = new EvalUnrankedQuery("church servants titles",  new TreeSet<>());
        Set<String> e8 = new TreeSet<>();
        e8.add("022");
        q8.setResults(e8);
        testSet.add(q8);

        EvalUnrankedQuery q9 = new EvalUnrankedQuery("problem solving approach for computer science",  new TreeSet<>());
        Set<String> e9 = new TreeSet<>();
        e9.add("029");        e9.add("049");        e9.add("005");        e9.add("004");        e9.add("061");
        e9.add("025");        e9.add("006");        e9.add("039");        e9.add("042");        e9.add("055");
        e9.add("018");        e9.add("030");
        q9.setResults(e9);
        testSet.add(q9);

        EvalUnrankedQuery q10 = new EvalUnrankedQuery("problem solving OR method AND computer science",  new TreeSet<>());
        Set<String> e10 = new TreeSet<>();
        e10.add("029");        e10.add("049");        e10.add("005");        e10.add("004");        e10.add("061");
        e10.add("006");        e10.add("055");        e10.add("039");        e10.add("042");        e10.add("030");
        e10.add("018");
        q10.setResults(e10);
        testSet.add(q10);

        EvalUnrankedQuery q11 = new EvalUnrankedQuery("governmental defensive organizations",  new TreeSet<>());
        Set<String> e11 = new TreeSet<>();
        e11.add("032");        e11.add("014");        e11.add("033");
        q11.setResults(e11);
        testSet.add(q11);

        EvalUnrankedQuery q12 = new EvalUnrankedQuery("technologies and tools for information processing",  new TreeSet<>());
        Set<String> e12 = new TreeSet<>();
        e12.add("006");        e12.add("004");        e12.add("052");        e12.add("005");        e12.add("050");
        e12.add("001");
        q12.setResults(e12);
        testSet.add(q12);
        
        return testSet;
    }
    
}
