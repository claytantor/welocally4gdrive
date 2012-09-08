package com.welocally.gdrive.mvc.controller;

import java.io.IOException;

import javax.servlet.ServletException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.welocally.gdrive.domain.Index;
import com.welocally.gdrive.domain.Search;
import com.welocally.gdrive.security.UserPrincipal;
import com.welocally.gdrive.security.UserPrincipalServiceException;
import com.welocally.gdrive.service.IndexService;
import com.welocally.gdrive.service.SearchService;

@Controller
@RequestMapping(value="/search")
@Scope("request")
public class SearchController extends AbstractAuthenticatedController {
       
    private static final Logger logger = Logger.getLogger(SearchController.class);
    
    @Autowired IndexService indexService;
    @Autowired SearchService searchService;
       
    @RequestMapping(value="/createform", method=RequestMethod.GET)
    public String createForm(
        @RequestParam(value = "id", required = true) String indexId, Model m) {       
        Index index = indexService.findByIndexId(indexId);
        Search form = new Search();
        form.setPublishStatus(Search.PublishStatus.UNPUBLISHED);
        form.setIndex(index);
        m.addAttribute("searchForm", form);
        
        //radius choices
        Float[] radius = {0.25f,0.50f,1.0f,2.0f,5.0f,10.0f,25.0f,50.0f };
        m.addAttribute("radiusChoices", radius);
        
        return "search/edit";   
    }
    
    @RequestMapping(value="/editform", method=RequestMethod.GET)
    public String editForm(
        @RequestParam(value = "id", required = true) Long id, Model m) {
        Search searchForm = searchService.findById(id);      
        m.addAttribute("searchForm", searchForm);
        
        //radius choices
        Float[] radius = {0.25f,0.50f,1.0f,2.0f,5.0f,10.0f,25.0f,50.0f };
        m.addAttribute("radiusChoices", radius);
        
        return "search/edit";   
    }
    
    @RequestMapping(value="/grid", method=RequestMethod.GET)
    public String grid(@RequestParam(value = "id", required = true) Long id, Model m) {
        Search search = searchService.findById(id); 
        m.addAttribute("search", search);
        return "search/grid";   
    }
    
    @RequestMapping(value="/map", method=RequestMethod.GET)
    public String map(@RequestParam(value = "id", required = true) Long id, Model m) {
        Search search = searchService.findById(id); 
        m.addAttribute("search", search);
        return "search/map";   
    }
    
    
    @RequestMapping(value="/list", method=RequestMethod.GET)
    public String list(Model m) {
        try {
            UserPrincipal up = super.getUserPrincipal();
            m.addAttribute("searches", searchService.findAllByOwner(up));
            return "search/list";
        } catch (ServletException e) {
            logger.error("problem", e);
            throw new RuntimeException("problem saving search");
        } catch (IOException e) {
            logger.error("problem", e);
            throw new RuntimeException("problem saving search");
        } catch (UserPrincipalServiceException e) {
            logger.error("problem", e);
            throw new RuntimeException("problem saving search");
        }
        

    }
     
    @RequestMapping(value="/save", method=RequestMethod.POST)
    public String save(Search searchForm, BindingResult result, Model m) {
        
        ValidationUtils.invokeValidator(new SearchValidator(), searchForm, result);
        if (result.hasErrors()) {
            m.addAttribute("searchForm", searchForm);
            m.addAttribute("errors", result.getAllErrors());
            return "search/edit";
        } else {
            try {
                searchService.save(searchForm);
                searchForm.setPublishStatus(Search.PublishStatus.UNPUBLISHED);
                UserPrincipal up = super.getUserPrincipal();
                m.addAttribute("searches", searchService.findAllByOwner(up));
                return "search/list";
            } catch (ServletException e) {
               logger.error("problem", e);
               throw new RuntimeException("problem saving search");
            } catch (IOException e) {
                logger.error("problem", e);
                throw new RuntimeException("problem saving search");
            } catch (UserPrincipalServiceException e) {
                logger.error("problem", e);
                throw new RuntimeException("problem saving search");
            }

        }
        
    }
    
    private class SearchValidator implements Validator{


        public boolean supports(Class<?> clazz) {
            return Search.class.equals(clazz);
        }


        public void validate(Object target,
                org.springframework.validation.Errors errors) {
            
            Search form = (Search)target;
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "queryRequired", "Please enter a name for the search.");            
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "query", "queryRequired", "Please enter a search query use * for all.");
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lat", "latRequired", "The Latitude of the location is required.");
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lng", "lngRequired", "The Longitude of the location is required.");
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "radius", "radiusRequired", "The Radius of the search is required.");
                     
        }
        
    }


}
