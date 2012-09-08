package com.welocally.gdrive.mvc.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.json.JSONException;
import org.scribe.model.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.welocally.gdrive.security.UserPrincipal;
import com.welocally.gdrive.security.UserPrincipalServiceException;
import com.welocally.gdrive.service.IndexService;
import com.welocally.gdrive.spreadsheet.client.GoogleOAuthFeedsClient;
import com.welocally.gdrive.spreadsheet.template.SpreadsheetTransformationUtils;


@Controller
@RequestMapping(value="/user")
@Scope("request")
public class UserController extends AbstractAuthenticatedController {
       
    private static final Logger logger = Logger.getLogger(UserController.class);
    
    @Value("${google.oauth2.callbackPrefix}")
    private String callbackPrefix;
    
    @Autowired GoogleOAuthFeedsClient authService;
    
    @Autowired SpreadsheetTransformationUtils spreadsheetTransformationUtils;
     
    @Autowired IndexService indexService;
     
    
    @RequestMapping(value="/home", method=RequestMethod.GET)
    public String home(Model model) {
        
        logger.debug("home");
        
        //see if the user is a member
        UserDetails user = 
            (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        model.addAttribute("user", user);    
        
        return "home";
    }
    
    @RequestMapping(value="/docs", method=RequestMethod.GET)
    public String docs(
            @RequestParam(value = "oauth_verifier", required = false) String oauth_verifier,
            Model model, 
            HttpServletRequest request, 
            HttpServletResponse resp) {
                
        logger.debug("spreadsheet auth:"+authService.toString());
             
        try {
            //see if the user is a member
            UserDetails user = 
                (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

             
            UserPrincipal up = userPrincipalService.findByUserName(user.getUsername());
            if(up != null)
            {
                authService.setGdocsUserAuthInfo(up);
                saveGDocsAuthInfo(up);
            }
                       
            Token accessToken = authService.getAccessGDocsToken();
            if (accessToken == null) {                      
                //there is a better way
                String callbackURL = callbackPrefix+"/user/docs";
                
                if (oauth_verifier == null) {
                    String authUrl = authService.getOAuthorizationURL(callbackURL, GoogleOAuthFeedsClient.DOCS_SCOPE);                
                    return "redirect:" + authUrl;
                } else {
                    authService.authorizeGDocsWith(oauth_verifier);
                    return "redirect:" + callbackURL;
                }
            }
            
            model.addAttribute("items", 
                    authService.retrieveDocumentItemTypeFromFeed(
                            "application/vnd.google-apps.spreadsheet"));

            
        } catch (JSONException e) {
            logger.error("problem with response", e);
            throw new RuntimeException(e);
        } catch (JsonParseException e) {
            logger.error("problem with response", e);
            throw new RuntimeException(e);
        } catch (JsonMappingException e) {
            logger.error("problem with response", e);
            throw new RuntimeException(e);
        } catch (IOException e) {
            logger.error("problem with response", e);
            throw new RuntimeException(e);
        } catch (UserPrincipalServiceException e) {
            logger.error("could not find user", e);
            throw new RuntimeException(e);
        } 
        catch (ServletException e) {
            logger.error("problem with response", e);
            throw new RuntimeException(e);
        }

      
        return "spreadsheet/list";
              
    }
    
    @RequestMapping(value="/spreadsheet", method=RequestMethod.GET)
    public String spreadsheet(
            @RequestParam String key, 
            @RequestParam(value = "oauth_verifier", required = false) String oauth_verifier,
            Model model, 
            HttpServletRequest request, 
            HttpServletResponse resp) {
        
        logger.debug("spreadsheet auth:"+authService.toString());
             
        try {
            //see if the user is a member
            UserDetails user = 
                (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

             
            UserPrincipal up = userPrincipalService.findByUserName(user.getUsername());
            if(up != null)
            {
                authService.setSsUserAuthInfo(up);
                saveSsAuthInfo(up);
            }
                       
            Token accessToken = authService.getSsAccessToken();
            if (accessToken == null) {                      
                //there is a better way
                String callbackURL = callbackPrefix+"/user/spreadsheet?key="+key;
                
                if (oauth_verifier == null) {
                    String authUrl = authService.getOAuthorizationURL(callbackURL, GoogleOAuthFeedsClient.SPREADSHEET_SCOPE);                
                    return "redirect:" + authUrl;
                } else {
                    authService.authorizeSsWith(oauth_verifier);
                    return "redirect:" + callbackURL;
                }
            }
                      
            model.addAttribute("spreadsheet", authService.retrieveSpreadsheetFeed(key));
            model.addAttribute("key", key);

            
        } catch (JSONException e) {
            logger.error("problem with response", e);
            throw new RuntimeException(e);
        } catch (JsonParseException e) {
            logger.error("problem with response", e);
            throw new RuntimeException(e);
        } catch (JsonMappingException e) {
            logger.error("problem with response", e);
            throw new RuntimeException(e);
        } catch (IOException e) {
            logger.error("problem with response", e);
            throw new RuntimeException(e);
        } catch (UserPrincipalServiceException e) {
            logger.error("could not find user", e);
            throw new RuntimeException(e);
        } catch (ServletException e) {
            logger.error("problem with response", e);
            throw new RuntimeException(e);
        }
      
        return "spreadsheet/view";
              
    }
    
    
    //this needs to change to view only
    @RequestMapping(value="/worksheet", method=RequestMethod.GET)
    public String worksheet(
            @RequestParam String url,
            @RequestParam String key,
            @RequestParam(value = "oauth_verifier", required = false) String oauth_verifier,
            Model model, 
            HttpServletRequest request, 
            HttpServletResponse resp) {
        
        logger.debug("worksheet auth:"+authService.toString());
             
        try {
            //see if the user is a member
            UserDetails user = 
                (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

             
            UserPrincipal up = userPrincipalService.findByUserName(user.getUsername());
            if(up != null)
            {
                authService.setSsUserAuthInfo(up);
                saveSsAuthInfo(up);
            }
                       
            Token accessToken = authService.getSsAccessToken();
            if (accessToken == null) {                      
                //there is a better way
                String callbackURL = callbackPrefix+"/user/worksheet?url="+url+"&key="+key;
                
                if (oauth_verifier == null) {
                    String authUrl = authService.getOAuthorizationURL(callbackURL,GoogleOAuthFeedsClient.SPREADSHEET_SCOPE);                
                    return "redirect:" + authUrl;
                } else {
                    authService.authorizeSsWith(oauth_verifier);
                    return "redirect:" + callbackURL;
                }
            }
                                
            model.addAttribute("worksheet", 
                    spreadsheetTransformationUtils.makeWorksheetGridFromWorksheetCellFeed(
                            authService.retrieveWorksheetCellFeed(url)));
            model.addAttribute("key", key); 

            
        } catch (JSONException e) {
            logger.error("problem with response", e);
            throw new RuntimeException(e);
        } catch (JsonParseException e) {
            logger.error("problem with response", e);
            throw new RuntimeException(e);
        } catch (JsonMappingException e) {
            logger.error("problem with response", e);
            throw new RuntimeException(e);
        } catch (IOException e) {
            logger.error("problem with response", e);
            throw new RuntimeException(e);
        } catch (UserPrincipalServiceException e) {
            logger.error("could not find user", e);
            throw new RuntimeException(e);
        } catch (ServletException e) {
            logger.error("problem with response", e);
            throw new RuntimeException(e);
        }
      
        return "worksheet/view";
              
    }
    

    
    private void saveSsAuthInfo(UserPrincipal up) throws ServletException, IOException, UserPrincipalServiceException{
        
        String authT = null;
        String authS = null;
        String accessT = null;
        String accessS = null;
        
        if(authService.getSsAuthToken() != null)
        {
            authT = authService.getSsAuthToken().getToken();
            authS = authService.getSsAuthToken().getSecret();
        }
        
        if(authService.getSsAccessToken() != null)
        {
            accessT = authService.getSsAccessToken().getToken();
            accessS = authService.getSsAccessToken().getSecret();
        }
                       
        userPrincipalService.saveSpreadsheetAuthInfo(
                up, 
                authService.getKey(), 
                authService.getSsAuthUrl(),
                authT, 
                authS,
                accessT,
                accessS);
    }
    
    private void saveGDocsAuthInfo(UserPrincipal up) throws ServletException, IOException, UserPrincipalServiceException{
        
        String authT = null;
        String authS = null;
        String accessT = null;
        String accessS = null;
        
        if(authService.getAuthGDocsToken() != null)
        {
            authT = authService.getAuthGDocsToken().getToken();
            authS = authService.getAuthGDocsToken().getSecret();
        }
        
        if(authService.getAccessGDocsToken() != null)
        {
            accessT = authService.getAccessGDocsToken().getToken();
            accessS = authService.getAccessGDocsToken().getSecret();
        }
          

        userPrincipalService.saveGdocsAuthInfo(
                up, 
                authService.getKey(), 
                authService.getAuthGDocsUrl(),
                authT, 
                authS,
                accessT,
                accessS);
    }    
    
    



   

}
