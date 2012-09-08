package com.welocally.gdrive.spreadsheet.template;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import com.welocally.gdrive.template.JSONDeserializer;

@Component
public class JsonDeserializationTemplate {
    
    public List<DocumentItem> docItems(JSONObject ofeed) 
    throws JsonParseException, JsonMappingException, IOException, JSONException
    {
        Object oentry = ofeed.get("items");
        
        JSONDeserializer<DocumentItem> template = 
            new JSONDeserializer<DocumentItem>(DocumentItem.class);
                
        if(oentry instanceof org.json.JSONObject){
            List<DocumentItem> entry = new ArrayList<DocumentItem>();
            entry.add(template.make((org.json.JSONObject)oentry));
            return entry;
                      
        } else if(oentry instanceof org.json.JSONArray){    
            List<DocumentItem> entries = template.makeResponseAsList((org.json.JSONArray)oentry);            
            return entries;         
        }
        
        return null;
    }
    
    public List<Entry> entries(JSONObject ofeed) 
    throws JsonParseException, JsonMappingException, IOException, JSONException {
        
       
        Object oentry = ofeed.get("entry");
                
        JSONDeserializer<Entry> template = 
            new JSONDeserializer<Entry>(Entry.class);
                
        if(oentry instanceof org.json.JSONObject){
            List<Entry> entry = new ArrayList<Entry>();
            entry.add(template.make((org.json.JSONObject)oentry));
            return entry;
                      
        } else if(oentry instanceof org.json.JSONArray){    
            List<Entry> entries = template.makeResponseAsList((org.json.JSONArray)oentry);            
            return entries;         
        }
                    
        throw new RuntimeException("No Entry Found");
        
    }
    
    public DocsFeed docsfeed(JSONObject ofeed) 
    throws JsonParseException, JsonMappingException, IOException, JSONException {
       
        JSONDeserializer<DocumentItem > template = 
            new JSONDeserializer<DocumentItem>(DocumentItem.class);
        DocsFeed feed = new DocsFeed();

        feed.setItems(template.makeResponseAsList(ofeed.getJSONArray("items")));
        
        return feed;
              
    }
    
    public SpreadsheetFeed spreadsheetfeed(JSONObject object) 
    throws JsonParseException, JsonMappingException, IOException, JSONException {
       
        JSONObject ofeed = object.getJSONObject("feed");
        JSONDeserializer<SpreadsheetFeed> template = 
            new JSONDeserializer<SpreadsheetFeed>(SpreadsheetFeed.class);
        SpreadsheetFeed feed = template.make((org.json.JSONObject)ofeed);        
        feed.setEntry(entries(ofeed));        
        return feed;
              
    }
    
    
    public WorksheetCellFeedEntry cellfeedentry(JSONObject object) 
    throws JsonParseException, JsonMappingException, IOException, JSONException {
       
        JSONObject oentry = object.getJSONObject("entry");
        JSONObject ocell = oentry.getJSONObject("gs$cell");
               
        JSONDeserializer<WorksheetCellFeedEntry> template = 
            new JSONDeserializer<WorksheetCellFeedEntry>(WorksheetCellFeedEntry.class);
        WorksheetCellFeedEntry cell = template.make((org.json.JSONObject)ocell);        
         
        return cell;
              
    }
    
    
    public WorksheetCellsFeed cellfeed(JSONObject object) 
    throws JsonParseException, JsonMappingException, IOException, JSONException {
       
        JSONObject ofeed = object.getJSONObject("feed");
        JSONDeserializer<WorksheetCellsFeed> template = 
            new JSONDeserializer<WorksheetCellsFeed>(WorksheetCellsFeed.class);
        WorksheetCellsFeed feed = template.make((org.json.JSONObject)ofeed);        
        feed.setEntry(cellentries(ofeed));        
        return feed;

              
    }
    
    public List<CellEntry> cellentries(JSONObject ofeed) 
    throws JsonParseException, JsonMappingException, IOException, JSONException {
        
       
        Object oentry = ofeed.get("entry");
                
        JSONDeserializer<CellEntry> template = 
            new JSONDeserializer<CellEntry>(CellEntry.class);
                
        if(oentry instanceof org.json.JSONObject){
            List<CellEntry> entry = new ArrayList<CellEntry>();
            entry.add(template.make((org.json.JSONObject)oentry));
            return entry;
                      
        } else if(oentry instanceof org.json.JSONArray){    
            List<CellEntry> entries = template.makeResponseAsList((org.json.JSONArray)oentry);            
            return entries;         
        }
                    
        throw new RuntimeException("No Entry Found");
        
    }
    
    

}
