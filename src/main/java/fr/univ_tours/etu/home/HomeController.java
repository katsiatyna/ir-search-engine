package fr.univ_tours.etu.home;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;

@Controller
public class HomeController {

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String index(Principal principal) {
        return principal != null ? "home/homeSignedIn" : "home/homeNotSignedIn";
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String searchPage(Principal principal) {
        return principal != null ? "home/homeSignedIn" : "search/search";
    }


}
