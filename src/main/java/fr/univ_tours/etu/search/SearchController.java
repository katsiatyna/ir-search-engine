package fr.univ_tours.etu.search;

import fr.univ_tours.etu.pdf.DocFields;
import fr.univ_tours.etu.searcher.ResultObject;
import fr.univ_tours.etu.searcher.Searcher;
import fr.univ_tours.etu.semantic.DBLP;
import fr.univ_tours.etu.semantic.DBPedia;
import fr.univ_tours.etu.semantic.RowLP;
import fr.univ_tours.etu.semantic.RowPedia;
import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.*;
import java.util.logging.Logger;

/**
 * Created by Katherine on 20.03.2016.
 */
@Controller
@RequestMapping(value = {"/search", "/search/"})
public class SearchController {

    private static Logger logger = Logger.getLogger("fr.univ_tours.etu");
    private static Map<String, String> operatorsMap;
    static {
        Map<String, String> aMap = new HashMap<>();
        aMap.put("eq", "=");
        aMap.put("lt", "<");
        aMap.put("le", "<=");
        aMap.put("gt", ">");
        aMap.put("ge", ">=");
        operatorsMap = Collections.unmodifiableMap(aMap);
    }

    @RequestMapping(method = RequestMethod.GET)
    public String searchPage(Model model) {
        SearchForm searchForm = new SearchForm();
        model.addAttribute("searchForm", searchForm);
        return "search/search";
    }

    @RequestMapping(value = "/submit", method = RequestMethod.GET)
    public String submitQuery(@ModelAttribute("searchForm") SearchForm searchForm,
                              @RequestParam(value = "useQueryExp", required = false) String useQuryExp,
                              @RequestParam(value = "useWordNet", required = false) String useWordNet,
                              Model model) {
        String mainQuery = searchForm.getMainQuery();
        String titleQuery = searchForm.getTitleQuery();
        String authorsQuery = searchForm.getAuthorQuery();
        String keywordsQuery = searchForm.getKeywordsQuery();
        logger.info("Main Query: " + mainQuery);
        logger.info("Title Query: " + titleQuery);
        logger.info("Author Query: " + authorsQuery);
        logger.info("Keywords Query: " + keywordsQuery);
        logger.info("Use Query Expansion: " + useQuryExp);
        SearchQueriesRequest searchQueriesRequest = new SearchQueriesRequest();
        searchQueriesRequest.setUseQueryExpansion(useQuryExp != null && useQuryExp.equals("on"));

        searchForm.setUseQueryExpansion(searchQueriesRequest.isUseQueryExpansion());
        logger.info("UseQueryExpansion: " + searchForm.isUseQueryExpansion());
        if (mainQuery != null && !"".equals(mainQuery)) {
            searchQueriesRequest.getQueriesDictionary().put(DocFields.CONTENTS, mainQuery);
            if (useWordNet != null && useWordNet.equals("on")) {
                searchQueriesRequest.getQueriesDictionary().put(DocFields.SYNONYMS, mainQuery);
                searchForm.setUseWordNet(true);
            } else {
                searchForm.setUseWordNet(false);
            }
        }
        if (titleQuery != null && !"".equals(titleQuery)) {
            searchQueriesRequest.getQueriesDictionary().put(DocFields.TITLE, titleQuery);
        }
        if (authorsQuery != null && !"".equals(authorsQuery)) {
            searchQueriesRequest.getQueriesDictionary().put(DocFields.AUTHOR, authorsQuery);
        }
        if (keywordsQuery != null && !"".equals(keywordsQuery)) {
            searchQueriesRequest.getQueriesDictionary().put(DocFields.KEYWORDS, keywordsQuery);
        }
        logger.info(searchQueriesRequest.getQueriesDictionary().toString());

        List<ResultObject> results = new ArrayList<>();
        /*try{
            Searcher searcher = new Searcher();
            results = searcher.search(searchQueriesRequest);
            logger.info((results != null) ? String.valueOf(results.size()) : "null" );

        } catch (IOException e){
            logger.warning("Searcher cannot be created!");
            e.printStackTrace();
        } catch (ParseException e) {
            logger.warning("Queries cannot be parsed!");
            e.printStackTrace();
        }
*/
        model.addAttribute("searchForm", searchForm);
        //model.addAttribute("numDocs", (results != null) ? results.size() : 0);
        //show only 20 results
        /*if(results != null && results.size() > 20){
            results = results.subList(0,20);
        }*/
        //model.addAttribute("results", results);

        return "search/results";
    }

    @RequestMapping(value = "/submitAsync", method = RequestMethod.GET)
    public String submitQueryAsync(@ModelAttribute("searchForm") SearchForm searchForm,
                                   @RequestParam(value = "useQueryExp", required = false) String useQuryExp,
                                   @RequestParam(value = "useWordNet", required = false) String useWordNet,
                                   @RequestParam(value = "operator", required = false) String operator,
                                   Model model) {
        String mainQuery = searchForm.getMainQuery();
        String titleQuery = searchForm.getTitleQuery();
        String authorsQuery = searchForm.getAuthorQuery();
        String keywordsQuery = searchForm.getKeywordsQuery();
        Integer year = searchForm.getYear();
        logger.info("Main Query: " + mainQuery);
        logger.info("Title Query: " + titleQuery);
        logger.info("Author Query: " + authorsQuery);
        logger.info("Keywords Query: " + keywordsQuery);
        logger.info("Use Query Expansion: " + useQuryExp);
        logger.info("Year:" + year);
        logger.info("Operator:" + operator);

        SearchQueriesRequest searchQueriesRequest = new SearchQueriesRequest();
        searchQueriesRequest.setUseQueryExpansion(useQuryExp != null && useQuryExp.equals("on"));
        searchQueriesRequest.setOperator(operator != null ? operator : null);

        searchForm.setUseQueryExpansion(searchQueriesRequest.isUseQueryExpansion());
        searchForm.setOperator(operator != null ? operator : null);
        logger.info("UseQueryExpansion: " + searchForm.isUseQueryExpansion());
        if (mainQuery != null && !"".equals(mainQuery)) {
            searchQueriesRequest.getQueriesDictionary().put(DocFields.CONTENTS, mainQuery);
            if (useWordNet != null && useWordNet.equals("on")) {
                searchQueriesRequest.getQueriesDictionary().put(DocFields.SYNONYMS, mainQuery);
                searchForm.setUseWordNet(true);
            } else {
                searchForm.setUseWordNet(false);
            }
        }
        if (titleQuery != null && !"".equals(titleQuery)) {
            searchQueriesRequest.getQueriesDictionary().put(DocFields.TITLE, titleQuery);
        }
        if (authorsQuery != null && !"".equals(authorsQuery)) {
            searchQueriesRequest.getQueriesDictionary().put(DocFields.AUTHOR, authorsQuery);
        }
        if (keywordsQuery != null && !"".equals(keywordsQuery)) {
            searchQueriesRequest.getQueriesDictionary().put(DocFields.KEYWORDS, keywordsQuery);
        }
        logger.info(searchQueriesRequest.getQueriesDictionary().toString());

        List<ResultObject> results = new ArrayList<>();
        try {
            Searcher searcher = new Searcher();
            results = searcher.search(searchQueriesRequest);
            logger.info((results != null) ? String.valueOf(results.size()) : "null");

        } catch (IOException e) {
            logger.warning("Searcher cannot be created!");
            e.printStackTrace();
        } catch (ParseException e) {
            logger.warning("Queries cannot be parsed!");
            e.printStackTrace();
        }

        model.addAttribute("searchForm", searchForm);
        model.addAttribute("numDocs", (results != null) ? results.size() : 0);
        //show only 20 results
        if (results != null && results.size() > 20) {
            results = results.subList(0, 20);
        }
        model.addAttribute("results", results);

        return "search/results_fragment :: resultsList";
    }

    @RequestMapping(value = "/submitDBPediaAsync", method = RequestMethod.GET)
    public String submitQueryDBPediaAsync(@ModelAttribute("searchForm") SearchForm searchForm,
                                          @RequestParam(value = "useQueryExp", required = false) String useQuryExp,
                                          @RequestParam(value = "useWordNet", required = false) String useWordNet,
                                          Model model) {
        String mainQuery = searchForm.getMainQuery();
        String titleQuery = searchForm.getTitleQuery();
        String authorsQuery = searchForm.getAuthorQuery();
        String keywordsQuery = searchForm.getKeywordsQuery();
        String query = "";
        if (mainQuery != null && !"".equals(mainQuery)) {
            query += mainQuery;
        }
        if (titleQuery != null && !"".equals(titleQuery)) {

        }
        if (authorsQuery != null && !"".equals(authorsQuery)) {

        }
        if (keywordsQuery != null && !"".equals(keywordsQuery)) {
            query += " " + keywordsQuery;
        }

        HashMap<Integer, RowPedia> res = null;
        try {
            DBPedia d=new DBPedia(query);
            res = d.getResults();

            logger.info("DBPedia size: " + String.valueOf(res.size()));

        } catch (Exception e){

        }

        model.addAttribute("numDocs", (res != null) ? res.size() : 0);
        model.addAttribute("resultsDBPedia", res);

        return "search/results_fragment :: resultsListDBPedia";
    }

    @RequestMapping(value = "/submitDBLPAsync", method = RequestMethod.GET)
    public String submitQueryDBLPAsync(@ModelAttribute("searchForm") SearchForm searchForm,
                                       @RequestParam(value = "useQueryExp", required = false) String useQuryExp,
                                       @RequestParam(value = "useWordNet", required = false) String useWordNet,
                                       Model model) {
        String mainQuery = searchForm.getMainQuery();
        String titleQuery = searchForm.getTitleQuery();
        String authorsQuery = searchForm.getAuthorQuery();
        String keywordsQuery = searchForm.getKeywordsQuery();
        Integer yearQuery = searchForm.getYear();
        String operatorQuery = searchForm.getOperator();
        String query = "";
        String author = "";
        int year = 0;
        String operator = "";
        if (mainQuery != null && !"".equals(mainQuery)) {
            query += mainQuery;
        }
        if (titleQuery != null && !"".equals(titleQuery)) {

        }
        if (authorsQuery != null && !"".equals(authorsQuery)) {
            author = authorsQuery;
        }
        if (keywordsQuery != null && !"".equals(keywordsQuery)) {
            query += " " + keywordsQuery;
        }
        if(yearQuery != null){
            year = yearQuery;
        }
        if(operatorQuery != null && !"".equals(operatorQuery)){
            operator = operatorsMap.get(operatorQuery);
        }
        HashMap<Integer, RowLP> res = null;
        try {
            DBLP d = new DBLP(query, author, year, operator);
            System.out.println(d.getQuery());
            res = d.getResults();

            for (int i : res.keySet()) {
                System.out.println("Row ID is: " + i);
                System.out.println(res.get(i).getYear());
                System.out.println(res.get(i).getPublicationTitle());
                System.out.println(res.get(i).getPublicationLink());
                System.out.println(res.get(i).getAuthorName());
                System.out.println(res.get(i).getAuthorLink());
                System.out.println("-----------------------------------------");
            }
            logger.info(String.valueOf(res.size()));

            //model.addAttribute("searchForm", searchForm);
            model.addAttribute("numDocs", res.size());
            model.addAttribute("resultsDBLP", res);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "search/results_fragment :: resultsListDBLP";
    }

}
