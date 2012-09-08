package com.welocally.gdrive.mvc.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.hibernate.id.GUIDGenerator;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.welocally.gdrive.domain.Index;
import com.welocally.gdrive.jms.PublishIndexMessageProducer;
import com.welocally.gdrive.mvc.model.GeodbIndexModel;
import com.welocally.gdrive.security.UserPrincipal;
import com.welocally.gdrive.security.UserPrincipalServiceException;
import com.welocally.gdrive.service.IndexService;
import com.welocally.gdrive.spreadsheet.client.GoogleOAuthFeedsClient;
import com.welocally.gdrive.spreadsheet.template.SpreadsheetTransformationUtils;
import com.welocally.gdrive.spreadsheet.template.WorksheetCellFeedEntry;
import com.welocally.gdrive.spreadsheet.template.WorksheetGrid;


@Controller
@RequestMapping(value="/index")
@Scope("request")
public class IndexController extends AbstractAuthenticatedController {
       
    private static final Logger logger = Logger.getLogger(IndexController.class);
    
    @Autowired IndexService indexService;
    @Autowired SpreadsheetTransformationUtils spreadsheetTransformationUtils;
    @Autowired GoogleOAuthFeedsClient authService;
    @Autowired FormUtils formUtils;
    
    @Autowired PublishIndexMessageProducer publishIndexMessageProducer;
         
    @RequestMapping(value="/list", method=RequestMethod.GET)
    public String list(Model m) {
        try {
            UserPrincipal up = super.getUserPrincipal();           
            List<Index> indexes = indexService.findAllByOwner(up);
            m.addAttribute("indexes", indexes);
            return "index/list";
        } catch (ServletException e) {
            logger.error("problem with response", e);
            throw new RuntimeException(e);
        } catch (IOException e) {
            logger.error("problem with response", e);
            throw new RuntimeException(e);
        } catch (UserPrincipalServiceException e) {
            logger.error("problem with response", e);
            throw new RuntimeException(e);
        }
    }
    
    @RequestMapping(value="/createform", method=RequestMethod.GET)
    public String createForm(
            @RequestParam String url,
            @RequestParam String key,
            Model model, 
            HttpServletRequest request, 
            HttpServletResponse resp) {
        
        try {
            UserDetails user = 
                (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

             
            UserPrincipal up = userPrincipalService.findByUserName(user.getUsername());
            
            //see if the user is a member
            Index indexForm = indexService.findByWorksheetFeed(url);
            
            if(indexForm == null){            
                
                //indexForm = new Index(indexFormId);
                indexForm = new Index();
                indexForm.setPublishStatus(Index.PublishStatus.UNPUBLISHED);
                indexForm.setWorksheetFeed(url);
                indexForm.setSpreadsheetKey(key);
                indexForm.setOwner(up);
                indexForm.setIndexExists(false);
            } else {
                
                indexForm.setIndexExists(true);
            }
            
            model.addAttribute("indexForm", indexForm);

            authService.setSsUserAuthInfo(up);
                              
            Map<String,WorksheetCellFeedEntry> entries =
                authService.retrieveHeaderEntries(url);           
            model.addAttribute("fields", entries.values());
                       
            model.addAttribute("spreadsheetKey", key);
            model.addAttribute("worksheetFeed", url);
            
            return "index/edit";   
            
        } catch (JsonParseException e) {
            logger.error("problem with response", e);
            throw new RuntimeException(e);
        } catch (JsonMappingException e) {
            logger.error("problem with response", e);
            throw new RuntimeException(e);
        } catch (UserPrincipalServiceException e) {
            logger.error("problem with response", e);
            throw new RuntimeException(e);
        } catch (ServletException e) {
            logger.error("problem with response", e);
            throw new RuntimeException(e);
        } catch (IOException e) {
            logger.error("problem with response", e);
            throw new RuntimeException(e);
        } catch (JSONException e) {
            logger.error("problem with response", e);
            throw new RuntimeException(e);
        }
               
        
    }
    
    @RequestMapping(value="/editform", method=RequestMethod.GET)
    public String editForm(@RequestParam(value = "id", required = true) String indexId, Model m) {
        
        try {
            UserDetails user = 
                (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

             
            UserPrincipal up = userPrincipalService.findByUserName(user.getUsername());
            
            Index indexForm = indexService.findByIndexId(indexId);
            indexForm.setIndexExists(true);
            m.addAttribute("indexForm", indexForm);      
            
            authService.setSsUserAuthInfo(up);
            
            Map<String,WorksheetCellFeedEntry> entries =
                authService.retrieveHeaderEntries(indexForm.getWorksheetFeed());           
            m.addAttribute("fields", entries.values());
                       
            m.addAttribute("spreadsheetKey", indexForm.getSpreadsheetKey());
            m.addAttribute("worksheetFeed", indexForm.getWorksheetFeed());
            
            return "index/edit";
        } catch (JsonParseException e) {
            logger.error("problem with response", e);
            throw new RuntimeException(e);
        } catch (JsonMappingException e) {
            logger.error("problem with response", e);
            throw new RuntimeException(e);
        } catch (UserPrincipalServiceException e) {
            logger.error("problem with response", e);
            throw new RuntimeException(e);
        } catch (ServletException e) {
            logger.error("problem with response", e);
            throw new RuntimeException(e);
        } catch (IOException e) {
            logger.error("problem with response", e);
            throw new RuntimeException(e);
        } catch (JSONException e) {
            logger.error("problem with response", e);
            throw new RuntimeException(e);
        }   
        
        
    }
    
    @RequestMapping(value="/publish", method=RequestMethod.GET)
    public String publish(@RequestParam String id, Model m){
        
        try {
            UserPrincipal up = super.getUserPrincipal();  
            Index index = indexService.findByIndexId(id);            
            if((index.getPublishStatus().equals(Index.PublishStatus.UNPUBLISHED) || 
                     index.getPublishStatus().equals(Index.PublishStatus.CHANGED)) &&
                     up != null)
            {
                 
                 if(up != null)              
                     authService.setSsUserAuthInfo(up);
          
                 //get the feed
                 WorksheetGrid grid = spreadsheetTransformationUtils.makeWorksheetGridFromWorksheetCellFeed(
                 authService.retrieveWorksheetCellFeed(index.getWorksheetFeed()));
                   
                 publishIndexMessageProducer.generateMessage(grid, index, up);
            }
                      
            return this.list(m);
                       
        } catch (Exception e) {
            logger.error("problem with response", e);
            throw new RuntimeException(e);
        } 
        
    
    }
    
    @RequestMapping(value="/data", method=RequestMethod.GET)
    public @ResponseBody GeodbIndexModel data(@RequestParam String id, Model m){
        
        try {
            UserPrincipal up = super.getUserPrincipal();
            
            if(up != null)
                authService.setSsUserAuthInfo(up);
            
            //try to find the index
            Index index = indexService.findByIndexId(id);
            
            //get the feed
            WorksheetGrid grid = spreadsheetTransformationUtils.makeWorksheetGridFromWorksheetCellFeed(
                    authService.retrieveWorksheetCellFeed(index.getWorksheetFeed()));
                     
            return spreadsheetTransformationUtils.makeIndexModel(grid, index);
            
            
        } catch (ServletException e) {
            logger.error("problem with response", e);
            throw new RuntimeException(e);
        } catch (IOException e) {
            logger.error("problem with response", e);
            throw new RuntimeException(e);
        } catch (UserPrincipalServiceException e) {
            logger.error("problem with response", e);
            throw new RuntimeException(e);
        } catch (JSONException e) {
            logger.error("problem with response", e);
            throw new RuntimeException(e);
        }
          
    }
    
    @RequestMapping(value="/delete", method=RequestMethod.GET)
    public String delete(@RequestParam String id, Model m) {
                          
        Index index = indexService.findByIndexId(id);
        indexService.delete(index);
        return list(m);
 
    }
    
    @RequestMapping(value="/save", method=RequestMethod.POST)
    public String save(@RequestBody String indexFormString, Model m) {
        try {   
            
            String indexDecoded = URLDecoder.decode(indexFormString,"UTF-8");           
            
            Index indexForm = formUtils.deserializeIndex(indexDecoded);
                          
            Index index = indexService.findByIndexId(indexForm.getIndexId());
            
            if(index == null){
                index = indexForm;
                index.setIndexId(UUID.randomUUID().toString().split("-")[4].toLowerCase());     
                index.setPublishStatus(Index.PublishStatus.UNPUBLISHED);
            } else {
                index.setPublishStatus(Index.PublishStatus.CHANGED);
            }
                                      
            index.setName(indexForm.getName());
            index.setPrimaryKey(indexForm.getPrimaryKey());
            index.setSpreadsheetKey(indexForm.getSpreadsheetKey());
            index.setLat(indexForm.getLat());
            index.setLng(indexForm.getLng());
            index.setOwner(super.getUserPrincipal());
                                           
            indexService.save(index);   

            return publish(index.getIndexId(),m);
    
                
    
            //}
        } catch (ServletException e) {
            logger.error("problem", e);
            throw new RuntimeException("problem saving search");
         } catch (IOException e) {
             logger.error("problem", e);
             throw new RuntimeException("problem saving search");
         } catch (UserPrincipalServiceException e) {
             logger.error("problem", e);
             throw new RuntimeException("problem saving search");
         } catch (IllegalAccessException e) {
             logger.error("problem", e);
             throw new RuntimeException("problem saving search");
        } catch (InvocationTargetException e) {
            logger.error("problem", e);
            throw new RuntimeException("problem saving search");
        } catch (NoSuchMethodException e) {
            logger.error("problem", e);
            throw new RuntimeException("problem saving search");
        }
        
    }
    
    
    
    private class IndexValidator implements Validator{


        public boolean supports(Class<?> clazz) {
            return Index.class.equals(clazz);
        }


        public void validate(Object target,
                org.springframework.validation.Errors errors) {
            
            Index form = (Index)target;
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "nameRequired", "Please enter a name for the search.");
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "primaryKey", "primaryKeyRequired", "Please enter a primary key field.");
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "searchFields", "searchRequired", "Please enter a the search fields.");
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lat", "latRequired", "Please enter a latitude field.");
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lng", "lngRequired", "Please enter a longitude field.");
                     
        }
        
    }
   

}
