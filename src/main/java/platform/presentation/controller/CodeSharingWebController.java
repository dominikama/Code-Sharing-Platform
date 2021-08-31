package platform.presentation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;
import platform.business.module.Code;
import platform.business.service.CodeSharingService;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
public class CodeSharingWebController {
    @Autowired
    CodeSharingService code;
    @GetMapping("/")
    public String welcomePage(HttpServletResponse response, Model model) {
        response.setHeader("Content-Type", "text/html");
        model.addAttribute("code_snippet", code.getLatest());
        return "welcome";
    }

    @GetMapping("/more")
    public String moreInformation() {
        return "more";
    }

    @GetMapping("/getAll")
    public String getAll(Model model) {
        model.addAttribute("snippets",code.findAll());
        return "snippets-list";
    }


    /**
     * Method which allows to get HTML page with the code snippet.
     * @param UUID unique identifier of the code snippet
     * @param response used to set header
     * @param model used to set attributes and send values to HTML file.
     * @return code.ftlh file
     */
    @RequestMapping(value = "/code/{uuid}", method = GET, produces = MediaType.TEXT_HTML_VALUE )
    public String getCodeHTML(@PathVariable("uuid") String UUID, HttpServletResponse response, Model model) {
        response.setHeader("Content-Type", "text/html");
        try {
            //decrement amount of allowed views
            code.decrement(UUID);

            //get date and code of the snippet with send token and add it as an attribute
            model.addAttribute("date", code.getCode(UUID).getDate());
            model.addAttribute("code", code.getCode(UUID).getCode());

            //add number of views to the model if views restrictions was set.
            if (code.getCode(UUID).getViews() > 0 ||
                    (code.getCode(UUID).getViews() == 0 && code.getCode(UUID).getTriggered())) {
                model.addAttribute("views", code.getCode(UUID).getViews());
            }

            //add time to the model if time restriction was set
            if (code.checkTime(UUID, LocalDateTime.now()) > 0) {
                model.addAttribute("time", code.checkTime(UUID, LocalDateTime.now()));
            }

            //if any of the restrictions was triggered delete a code snippet from the database
            if (code.getCode(UUID).getTriggered()) {
                code.deleteTriggered(UUID);
            }
        } catch (NullPointerException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return "code";
    }

    /**
     * Method allowing to add code snippet via html page. It uses javascript function to send data to api/code/new
     * where code snippet is saved to the database and list.
     * @param response used to set header
     * @return code-new.html - represents page with form for creating new snippet.
     */
    @GetMapping("/code/new")
    public String postCode(HttpServletResponse response) {
        response.setHeader("Content-Type", "text/html");
        return "code-new";
    }

}
