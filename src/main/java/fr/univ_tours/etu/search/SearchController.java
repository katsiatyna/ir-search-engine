package fr.univ_tours.etu.search;

import fr.univ_tours.etu.pdf.DocFields;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Map;

/**
 * Created by Katherine on 20.03.2016.
 */
@Controller
@RequestMapping(value = "/search")
public class SearchController {

    @RequestMapping( method = RequestMethod.GET)
    public String searchPage(Model model) {
        SearchForm searchForm = new SearchForm();
        model.addAttribute("searchForm", searchForm);
        return "search/search";
    }

    @RequestMapping(value = "/submit", method = RequestMethod.GET)
    public String submitQuery(@ModelAttribute("searchForm") SearchForm searchForm,
                              Map<String, Object> model, Principal principal) {
        String mainQuery = searchForm.getMainQuery();
        String titleQuery = searchForm.getTitleQuery();
        String authorsQuery = searchForm.getAuthorQuery();
        String keywordsQuery = searchForm.getKeywordsQuery();
        System.out.println("Main Query: " + mainQuery);
        System.out.println("Title Query: " + titleQuery);
        System.out.println("Author Query: " + authorsQuery);
        System.out.println("Keywords Query: " + keywordsQuery);
        System.out.println("Use Query Expansion: " + searchForm.isUseQueryExpansion());
        SearchQueriesRequest searchQueriesRequest = new SearchQueriesRequest();
        searchQueriesRequest.setUseQueryExpansion(searchForm.isUseQueryExpansion());
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
        System.out.println(searchQueriesRequest.getQueriesDictionary());
        //TODO send object to searcher


        return "search/results";
    }
}
