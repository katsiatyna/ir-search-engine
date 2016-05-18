/*
 *  This work is licensed for the ULB Group13 BKT for the DBSA course.
 */
package fr.univ_tours.etu.evaluation;

import fr.univ_tours.etu.index.Indexer;
import fr.univ_tours.etu.pdf.DocFields;
import fr.univ_tours.etu.search.SearchQueriesRequest;
import fr.univ_tours.etu.searcher.ResultObject;
import fr.univ_tours.etu.searcher.Searcher;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.queryparser.classic.ParseException;

/**
 *
 * @author Anas Alba
 */
public class Evalutaion {

    private List<EvalUnrankedQuery> testSet = new ArrayList<>();
    private List<EvalUnrankedResult> answersSet = new ArrayList<>();

    public static double B_factor = 1;

    public static void main(String[] args) {
        System.out.println("******************");
        System.out.println("Unranked Evaluation : "+unranked_evaluation()+"%");
        System.out.println("******************");
        System.out.println("******************");
        System.out.println("Ranked Evaluation : "+ranked_evaluation(20)+"%");
        System.out.println("******************");
    }
    
    public static double unranked_evaluation() {

        try {
            Searcher searcher = new Searcher(200);
            List<EvalUnrankedQuery> testSet = TestFiller.fillUnranked();
            double finalP = 0;
            double finalR = 0;
            int counter = 0;
            for (EvalUnrankedQuery evalUnrankedQuery : testSet) {
                SearchQueriesRequest queriesRequest = new SearchQueriesRequest();
                Map<String, String> map = new HashMap<>();
                map.put(DocFields.CONTENTS, evalUnrankedQuery.getQuery());
                queriesRequest.setQueriesDictionary(map);
                List<ResultObject> resultObjects = searcher.search(queriesRequest);
                EvalUnrankedResult result = new EvalUnrankedResult();
                for (ResultObject resultObject : resultObjects) {
                    result.add(resultObject.getFileName().split("\\.")[0]);
                    System.out.print(resultObject.getFileName().split("\\.")[0] + ",");
                }
                System.out.println();
                finalP += (double)intersection(evalUnrankedQuery.getResults(), result.getResutls()).size() / (double)result.getResutls().size();
                finalR += (double)intersection(evalUnrankedQuery.getResults(), result.getResutls()).size() / (double)evalUnrankedQuery.getResults().size();
                counter++;
            }
            finalP /= counter;
            finalR /= counter;
            //F-measure
            return ((B_factor*B_factor+1)*finalP*finalR) / (B_factor*B_factor*finalP+finalR);

        } catch (IOException ex) {
            Logger.getLogger(Evalutaion.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(Evalutaion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    public static double ranked_evaluation(int topK) {

        try {
            Searcher searcher = new Searcher(topK);
            List<EvalUnrankedQuery> testSet = TestFiller.fillUnranked();
            double finalP = 0;
            double finalR = 0;
            int counter = 0;
            for (EvalUnrankedQuery evalUnrankedQuery : testSet) {
                SearchQueriesRequest queriesRequest = new SearchQueriesRequest();
                Map<String, String> map = new HashMap<>();
                map.put(DocFields.CONTENTS, evalUnrankedQuery.getQuery());
                queriesRequest.setQueriesDictionary(map);
                List<ResultObject> resultObjects = searcher.search(queriesRequest);
                EvalUnrankedResult result = new EvalUnrankedResult();
                for (ResultObject resultObject : resultObjects) {
                    result.add(resultObject.getFileName().split("\\.")[0]);
                }
                finalP += (double)intersection(evalUnrankedQuery.getResults(), result.getResutls()).size() / (double)result.getResutls().size();
                finalR += (double)intersection(evalUnrankedQuery.getResults(), result.getResutls()).size() / (double)evalUnrankedQuery.getResults().size();
                counter++;
            }
            finalP /= counter;
            finalR /= counter;
            //F-measure
            return ((B_factor*B_factor+1)*finalP*finalR) / (B_factor*B_factor*finalP+finalR);

        } catch (IOException ex) {
            Logger.getLogger(Evalutaion.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(Evalutaion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }
    
    public static void index() {
        String indexDir = "C:\\Users\\anas\\Documents\\NetBeansProjects\\IR\\Test\\index";
        String dataDir = "C:\\Users\\anas\\Documents\\NetBeansProjects\\IR\\Test";
        try {
            Indexer indexer = new Indexer(indexDir);
            int numIndexed;
            long startTime = System.currentTimeMillis();
            numIndexed = indexer.createIndex(dataDir, 200);
            long endTime = System.currentTimeMillis();
            indexer.close();
            System.out.println(numIndexed + " File indexed, time taken: "
                    + (endTime - startTime) + " ms");

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static <T> Set<T> union(Set<T> setA, Set<T> setB) {
        Set<T> tmp = new TreeSet<T>(setA);
        tmp.addAll(setB);
        return tmp;
    }

    public static <T> Set<T> intersection(Set<T> setA, Set<T> setB) {
        Set<T> tmp = new TreeSet<T>();
        for (T x : setA) {
            if (setB.contains(x)) {
                tmp.add(x);
            }
        }
        return tmp;
    }

    public static <T> Set<T> difference(Set<T> setA, Set<T> setB) {
        Set<T> tmp = new TreeSet<T>(setA);
        tmp.removeAll(setB);
        return tmp;
    }

    public static <T> Set<T> symDifference(Set<T> setA, Set<T> setB) {
        Set<T> tmpA;
        Set<T> tmpB;

        tmpA = union(setA, setB);
        tmpB = intersection(setA, setB);
        return difference(tmpA, tmpB);
    }

    public static <T> boolean isSubset(Set<T> setA, Set<T> setB) {
        return setB.containsAll(setA);
    }

    public static <T> boolean isSuperset(Set<T> setA, Set<T> setB) {
        return setA.containsAll(setB);
    }

    public List<EvalUnrankedQuery> getTestSet() {
        return testSet;
    }

    public void setTestSet(List<EvalUnrankedQuery> testSet) {
        this.testSet = testSet;
    }

    public List<EvalUnrankedResult> getAnswersSet() {
        return answersSet;
    }

    public void setAnswersSet(List<EvalUnrankedResult> answersSet) {
        this.answersSet = answersSet;
    }

    public double getB_factor() {
        return B_factor;
    }

    public void setB_factor(double B_factor) {
        this.B_factor = B_factor;
    }

}
