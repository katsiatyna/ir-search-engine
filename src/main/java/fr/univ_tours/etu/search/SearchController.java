package fr.univ_tours.etu.search;

import fr.univ_tours.etu.pdf.DocFields;
import fr.univ_tours.etu.searcher.ResultObject;
import fr.univ_tours.etu.searcher.Searcher;
import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by Katherine on 20.03.2016.
 */
@Controller
@RequestMapping(value = "/search")
public class SearchController {

    private static Logger logger = Logger.getLogger("fr.univ_tours.etu");

    @RequestMapping( method = RequestMethod.GET)
    public String searchPage(Model model) {
        SearchForm searchForm = new SearchForm();
        model.addAttribute("searchForm", searchForm);
        return "search/search";
    }

    @RequestMapping(value = "/submit", method = RequestMethod.GET)
    public String submitQuery(@ModelAttribute("searchForm") SearchForm searchForm,
                              @RequestParam(value = "useQueryExp", required = false) String useQuryExp,
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
        if(mainQuery != null && !"".equals(mainQuery)){
            searchQueriesRequest.getQueriesDictionary().put(DocFields.CONTENTS, mainQuery);
        }
        if(titleQuery != null && !"".equals(titleQuery)){
            searchQueriesRequest.getQueriesDictionary().put(DocFields.TITLE, titleQuery);
        }
        if(authorsQuery != null && !"".equals(authorsQuery)){
            searchQueriesRequest.getQueriesDictionary().put(DocFields.AUTHOR, authorsQuery);
        }
        if(keywordsQuery != null && !"".equals(keywordsQuery)){
            searchQueriesRequest.getQueriesDictionary().put(DocFields.KEYWORDS, keywordsQuery);
        }
        logger.info(searchQueriesRequest.getQueriesDictionary().toString());

        List<ResultObject> results = new ArrayList<>();
        try{
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

        model.addAttribute("searchForm", searchForm);
        model.addAttribute("results", results);
        return "search/results";
    }
}
