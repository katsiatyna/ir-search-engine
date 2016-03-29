package fr.univ_tours.etu.home;

import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;

@Controller
public class HomeController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(Principal principal) {
        return principal != null ? "redirect:/search" : "home/homeNotSignedIn";
    }

    @RequestMapping(value = "/files/{file_name}", method = RequestMethod.GET)
    @ResponseBody
    public FileSystemResource getFile(
            @PathVariable("file_name") String fileName) {
        try {
            // get your file as InputStream
            String rootPath = "c:\\Users\\Katherine\\Documents\\2nd sem\\IR\\project\\ir-search-engine\\src\\main\\resources\\test_docs\\";
            File file = new File(rootPath + fileName);
            InputStream is = new FileInputStream(file);
            // copy it to response's OutputStream
            //org.apache.commons.io.IOUtils.copy(is, response.getOutputStream());
            //response.setContentType("application/pdf");
            //response.flushBuffer();
            return new FileSystemResource(file);
        } catch (IOException ex) {
            System.out.println("Error writing file to output stream.");
            throw new RuntimeException("IOError writing file to output stream");
        }

    }



}
