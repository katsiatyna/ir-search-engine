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
        e2.add("024");e2.add("025");e2.add("037");e2.add("019");e2.add("027");e2.add("057");e2.add("006");e2.add("001");
        q2.setResults(e2);
        testSet.add(q2);

        EvalUnrankedQuery q3 = new EvalUnrankedQuery("J. Smith",  new TreeSet<>());
        Set<String> e3 = new TreeSet<>();
        e3.add("021");e3.add("022");e3.add("023");e3.add("111");
        q3.setResults(e3);
        testSet.add(q3);

        EvalUnrankedQuery q4 = new EvalUnrankedQuery("who invented the airplane",  new TreeSet<>());
        Set<String> e4 = new TreeSet<>();
        e4.add("120");e4.add("121");e4.add("087");e4.add("026");
        q4.setResults(e4);
        testSet.add(q4);

        EvalUnrankedQuery q5 = new EvalUnrankedQuery("volcano NOT active",  new TreeSet<>());
        Set<String> e5 = new TreeSet<>();
        e5.add("184");e5.add("180");e5.add("185");
        q5.setResults(e5);
        testSet.add(q5);

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

        EvalUnrankedQuery q13 = new EvalUnrankedQuery("network protocol",  new TreeSet<>());
        Set<String> e13 = new TreeSet<>();
        e13.add("018");        e13.add("028");        e13.add("030");       e13.add("040");
        q13.setResults(e13);
        testSet.add(q13);

        EvalUnrankedQuery q14 = new EvalUnrankedQuery("leaders of America",  new TreeSet<>());
        Set<String> e14 = new TreeSet<>();
        e14.add("100");        e14.add("075");        e14.add("085");
        e14.add("099");        e14.add("147");        e14.add("155");
        e14.add("069");        e14.add("012");        e14.add("103");
        e14.add("156");        e14.add("142");        e14.add("022");
        e14.add("126");        e14.add("150");        e14.add("089");
        e14.add("096");        e14.add("079");        e14.add("114");
        e14.add("154");        e14.add("164");
        q14.setResults(e14);
        testSet.add(q14);

        EvalUnrankedQuery q15 = new EvalUnrankedQuery("states in America",  new TreeSet<>());
        Set<String> e15 = new TreeSet<>();
        e15.add("007");        e15.add("189");        e15.add("145");       e15.add("173");         e15.add("142");
        e15.add("071");        e15.add("156");        e15.add("103");       e15.add("158");         e15.add("160");
        e15.add("177");        e15.add("140");        e15.add("012");       e15.add("137");         e15.add("167");
        e15.add("136");        e15.add("174");        e15.add("171");       e15.add("139");         e15.add("175");
        e15.add("178");        e15.add("199");        e15.add("170");       e15.add("162");         e15.add("176");
        q15.setResults(e15);
        testSet.add(q15);

        EvalUnrankedQuery q16 = new EvalUnrankedQuery("Williams NOT Theodore",  new TreeSet<>());
        Set<String> e16 = new TreeSet<>();
        e16.add("074");        e16.add("070");        e16.add("073");       e16.add("127");         e16.add("072");
        e16.add("071");        e16.add("035");        e16.add("161");       e16.add("111");         e16.add("034");
        e16.add("069");        e16.add("062");
        q16.setResults(e16);
        testSet.add(q16);

        EvalUnrankedQuery q17 = new EvalUnrankedQuery("Universities in Washington",  new TreeSet<>());
        Set<String> e17 = new TreeSet<>();
        e17.add("188");        e17.add("078");        e17.add("136");       e17.add("132");         e17.add("134");
        e17.add("007");        e17.add("009");
        q17.setResults(e17);
        testSet.add(q17);

        EvalUnrankedQuery q18 = new EvalUnrankedQuery("index of religious honorifics titles",  new TreeSet<>());
        Set<String> e18 = new TreeSet<>();
        e18.add("022");        e18.add("071");        e18.add("142");
        q18.setResults(e18);
        testSet.add(q18);

        EvalUnrankedQuery q19 = new EvalUnrankedQuery("technologies tools for information processing",  new TreeSet<>());
        Set<String> e19 = new TreeSet<>();
        e19.add("032");        e19.add("014");        e19.add("033");
        q19.setResults(e19);
        testSet.add(q19);

        EvalUnrankedQuery q20 = new EvalUnrankedQuery("american university",  new TreeSet<>());
        Set<String> e20 = new TreeSet<>();
        e20.add("004");        e20.add("006");        e20.add("149");       e20.add("005");         e20.add("016");
        e20.add("052");        e20.add("035");        e20.add("161");       e20.add("111");         e20.add("034");
        e20.add("069");        e20.add("050");        e20.add("062");
        q20.setResults(e20);
        testSet.add(q20);

        EvalUnrankedQuery q21 = new EvalUnrankedQuery("presidents who were killed",  new TreeSet<>());
        Set<String> e21 = new TreeSet<>();
        e21.add("100");        e21.add("099");        e21.add("147");
        e21.add("155");        e21.add("075");        e21.add("126");
        e21.add("085");        e21.add("096");
        q21.setResults(e21);
        testSet.add(q21);

        EvalUnrankedQuery q22 = new EvalUnrankedQuery("international war",  new TreeSet<>());
        Set<String> e22 = new TreeSet<>();
        e22.add("097");        e22.add("035");        e22.add("034");           e22.add("085");        e22.add("032");
        e22.add("161");        e22.add("111");        e22.add("014");           e22.add("036");
        q22.setResults(e22);
        testSet.add(q22);

        EvalUnrankedQuery q23 = new EvalUnrankedQuery("natural science researcher",  new TreeSet<>());
        Set<String> e23 = new TreeSet<>();
        e23.add("006");        e23.add("090");        e23.add("061");           e23.add("151");
        e23.add("004");        e23.add("113");        e23.add("029");           e23.add("005");
        q23.setResults(e23);
        testSet.add(q23);

        return testSet;
    }
    
}
