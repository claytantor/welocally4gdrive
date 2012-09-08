package com.welocally.gdrive.spreadsheet.client;

import static ch.lambdaj.Lambda.filter;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.ServletException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.json.JSONException;
import org.json.JSONObject;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.GoogleApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.welocally.gdrive.security.UserPrincipal;
import com.welocally.gdrive.spreadsheet.template.DocsFeed;
import com.welocally.gdrive.spreadsheet.template.DocumentItem;
import com.welocally.gdrive.spreadsheet.template.JsonDeserializationTemplate;
import com.welocally.gdrive.spreadsheet.template.SpreadsheetFeed;
import com.welocally.gdrive.spreadsheet.template.WorksheetCellFeedEntry;
import com.welocally.gdrive.spreadsheet.template.WorksheetCellsFeed;

@Service
@Scope(value = "session")
public class GoogleOAuthFeedsClient {
    
    private static final Logger logger = Logger.getLogger(GoogleOAuthFeedsClient.class);
    
    public static final String DOCS_SCOPE = "https://www.googleapis.com/auth/drive.readonly";
    public static final String SPREADSHEET_SCOPE = "https://spreadsheets.google.com/feeds";
    
	private OAuthService service;
	
	private String key;   
    private String authSsUrl;
    private Token authSsToken;
    private Token accessSsToken;
    private String authGDocsUrl;
    private Token authGDocsToken;
    private Token accessGDocsToken;
    
    @Value("${google.oauth2.clientId}")
    String apiKey;
    @Value("${google.oauth2.clientSecret}")
    String apiSecret;
    
    @Autowired JsonDeserializationTemplate jsonDeserializationTemplate;
    
    
    @PostConstruct
    public synchronized void init(){
        try {
            Thread.sleep(1);
            this.key = "auth-"+Calendar.getInstance().getTimeInMillis();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }       
    }
    
    /**
     * auth settings for ss
     * 
     * @throws ServletException
     * @throws IOException
     */
    public void setSsUserAuthInfo(UserPrincipal up) throws ServletException, IOException{
        
        if(up.getAuthKey() != null){
            this.setKey(up.getAuthKey());
        }
        
            
        if(up.getSsAuthUrl() != null){
            this.setSsAuthUrl(up.getSsAuthUrl());
        }
    

        if(up.getSsAuthToken() != null && up.getSsAuthSecret() != null){
            Token auth = new Token(up.getSsAuthToken(), up.getSsAuthSecret() );
            this.setSsAuthToken(auth);
        } 
        
        if(up.getSsAccessToken() != null && up.getSsAccessSecret() != null){
            Token access = new Token(up.getSsAccessToken(), up.getSsAccessSecret() );
            this.setSsAccessToken(access);
        } 
  
    }
    
    public void setGdocsUserAuthInfo(UserPrincipal up) throws ServletException, IOException{
        
        if(up.getAuthKey() != null){
            this.setKey(up.getAuthKey());
        }
        
            
        if(up.getGdocsAuthUrl() != null){
            this.setAuthGDocsUrl(up.getGdocsAuthUrl());
        }
    

        if(up.getGdocsAuthToken() != null && up.getGdocsAuthSecret() != null){
            Token auth = new Token(up.getGdocsAuthToken(), up.getGdocsAuthSecret() );
            this.setAuthGDocsToken(auth);
        } 
        
        if(up.getGdocsAccessToken() != null && up.getGdocsAccessSecret() != null){
            Token access = new Token(up.getGdocsAccessToken(), up.getGdocsAccessSecret() );
            this.setAccessGDocsToken(access);
        } 
  
    }

    public String getOAuthorizationURL(String forUrl, String scope) {
        service = new ServiceBuilder().provider(GoogleApi.class)
                .apiKey(apiKey)
                .apiSecret(apiSecret)
                .scope(scope)
                .callback(forUrl)
                .build();
        if(scope.equals(DOCS_SCOPE)){
            authGDocsToken = service.getRequestToken();
            authGDocsUrl = service.getAuthorizationUrl(authGDocsToken);
            return authGDocsUrl;
            
        } else if(scope.equals(SPREADSHEET_SCOPE)){
            authSsToken = service.getRequestToken();
            authSsUrl = service.getAuthorizationUrl(authSsToken);
            return authSsUrl;
        }
        throw new RuntimeException("invalid scope");
        
    }
    
    
    public Token getSsAuthToken() {
        return authSsToken;
    }
    public void authorizeSsWith(String oauth_verifier) {
        accessSsToken = service.getAccessToken(authSsToken, new Verifier(oauth_verifier));      
    }
    
    public void authorizeGDocsWith(String oauth_verifier) {
        accessGDocsToken = service.getAccessToken(authGDocsToken, new Verifier(oauth_verifier));      
    }
    
    public Token getSsAccessToken() {
        return accessSsToken;
    }
    
    public DocsFeed retrieveDocsFeed() throws JSONException, JsonParseException, JsonMappingException, IOException {
        return jsonDeserializationTemplate.docsfeed(retrieveDocsFeedAsJSON());
    }
    
    public List<DocumentItem> retrieveDocumentItemTypeFromFeed(String mimeType) throws JSONException, JsonParseException, JsonMappingException, IOException {
        DocsFeed feed = jsonDeserializationTemplate.docsfeed(retrieveDocsFeedAsJSON());
        List<DocumentItem> items = 
            filter(org.hamcrest.Matchers.hasProperty("mimeType", org.hamcrest.Matchers.equalTo(mimeType)), 
                    feed.getItems());
        
        return items;
    }
    
    //-----------alldocs
    
    public JSONObject retrieveDocsFeedAsJSON() throws JSONException, JsonParseException, JsonMappingException, IOException {       
        if(service == null && authGDocsUrl != null){
            service = new ServiceBuilder().provider(GoogleApi.class)
                .apiKey(apiKey)
                .apiSecret(apiSecret)
                .scope(DOCS_SCOPE)
                .callback(authGDocsUrl)
                .build();
        }
        
        if(service != null){
            OAuthRequest request = new OAuthRequest(Verb.GET, "https://www.googleapis.com/drive/v2/files");
            service.signRequest(accessGDocsToken, request);
            Response response = request.send();
            String bodyString = response.getBody();
            JSONObject body = new JSONObject(bodyString);
            logger.debug(body.toString());
            return body;   
        } else
            throw new RuntimeException("Could not init service");
   
   }
    
    //-----------spreadsheet
    
    public SpreadsheetFeed retrieveSpreadsheetFeed(String spreadsheetName) throws JSONException, JsonParseException, JsonMappingException, IOException {
         return jsonDeserializationTemplate.spreadsheetfeed(retrieveSpreadsheetFeedAsJSON(spreadsheetName));      
    }
    
    
    public JSONObject retrieveSpreadsheetFeedAsJSON(String spreadsheetName) throws JSONException, JsonParseException, JsonMappingException, IOException {       
        if(service == null && authSsUrl != null){
            service = new ServiceBuilder().provider(GoogleApi.class)
                .apiKey(apiKey)
                .apiSecret(apiSecret)
                .scope(SPREADSHEET_SCOPE)
                .callback(authSsUrl)
                .build();
        }
        
        if(service != null){
            OAuthRequest request = new OAuthRequest(Verb.GET, "https://spreadsheets.google.com/feeds/worksheets/" + spreadsheetName + "/private/full?alt=json");
            service.signRequest(accessSsToken, request);
            Response response = request.send();
            JSONObject body = new JSONObject(response.getBody());
            logger.debug(body.toString());
            return body;   
        } else
            throw new RuntimeException("Could not init service");
   
   }
    
    
    
    public JSONObject retrieveSpreadsheetsAPIFeedAsJSON(String worksheetFeedUrl) throws JSONException, JsonParseException, JsonMappingException, IOException {       
        if(service == null && authSsUrl != null){
            service = new ServiceBuilder().provider(GoogleApi.class)
                .apiKey(apiKey)
                .apiSecret(apiSecret)
                .scope(SPREADSHEET_SCOPE)
                .callback(authSsUrl)
                .build();
        }
        
        if(service != null){
            OAuthRequest request = new OAuthRequest(Verb.GET, worksheetFeedUrl+"?alt=json");
            service.signRequest(accessSsToken, request);
            Response response = request.send();
            JSONObject body = new JSONObject(response.getBody());
            logger.debug(body.toString());
            return body;   
        } else
            throw new RuntimeException("Could not init service");
   
   }
    
    
    public Map<String,WorksheetCellFeedEntry> retrieveHeaderEntries(String worksheetCellEntryUrl)
            throws JsonParseException, JsonMappingException, IOException, JSONException{        
        Map<String,WorksheetCellFeedEntry> model = new HashMap<String,WorksheetCellFeedEntry>();
        
        Boolean empty = false;
        Integer index = 1;
        while(!empty){
            String cellname = "R1C"+index;
            WorksheetCellFeedEntry entry = retrieveWorksheetCellFeedEntry(worksheetCellEntryUrl+"/"+cellname);
            if(StringUtils.isEmpty(entry.getInputValue()))
            {
                empty = true;
            } else {
                model.put(cellname, entry );
                index++;
            }
            
        }
          
        return model;
    }
    
    public Map<String,WorksheetCellFeedEntry> retrieveWorksheetCellFeedEntries(
            List<String> cellnames, String worksheetCellEntryUrl)
            throws JsonParseException, JsonMappingException, IOException, JSONException{        
        Map<String,WorksheetCellFeedEntry> model = new HashMap<String,WorksheetCellFeedEntry>();
        for (String name : cellnames) {
            model.put(name, retrieveWorksheetCellFeedEntry(worksheetCellEntryUrl+"/"+name) );
        }       
        return model;
    }
    
    public WorksheetCellFeedEntry retrieveWorksheetCellFeedEntry(String worksheetFeedUrl) 
    throws JsonParseException, JsonMappingException, IOException, JSONException{
        return jsonDeserializationTemplate.cellfeedentry(
                retrieveSpreadsheetsAPIFeedAsJSON(worksheetFeedUrl));
    }
    
    
    public WorksheetCellsFeed retrieveWorksheetCellFeed(String worksheetFeedUrl) 
    throws JsonParseException, JsonMappingException, IOException, JSONException{
        return jsonDeserializationTemplate.cellfeed(
                retrieveSpreadsheetsAPIFeedAsJSON(worksheetFeedUrl));
    }
    
     
    
    public OAuthService getService() {
        return service;
    }



    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("key: "+key.toString());
        if(accessSsToken != null)
            buf.append(" accessToken: "+accessSsToken.toString());
        if(authSsUrl != null)
            buf.append(" authUrl: "+authSsUrl.toString());
        if(authSsToken != null)
            buf.append(" authToken: "+authSsToken.toString());
        return buf.toString();
    }

    public String getKey() {
        return key;
    }

    public String getSsAuthUrl() {
        return authSsUrl;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getApiSecret() {
        return apiSecret;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setSsAuthToken(Token authToken) {
        this.authSsToken = authToken;
    }

    public void setSsAccessToken(Token accessToken) {
        this.accessSsToken = accessToken;
    }

    public void setSsAuthUrl(String authUrl) {
        this.authSsUrl = authUrl;
    }

    public String getAuthGDocsUrl() {
        return authGDocsUrl;
    }

    public void setAuthGDocsUrl(String authGDocsUrl) {
        this.authGDocsUrl = authGDocsUrl;
    }

    public Token getAuthGDocsToken() {
        return authGDocsToken;
    }

    public void setAuthGDocsToken(Token authGDocsToken) {
        this.authGDocsToken = authGDocsToken;
    }

    public Token getAccessGDocsToken() {
        return accessGDocsToken;
    }

    public void setAccessGDocsToken(Token accessGDocsToken) {
        this.accessGDocsToken = accessGDocsToken;
    }



}
