package com.welocally.gdrive.spreadsheet.template;

import java.util.HashMap;
import java.util.Map;

public class WorksheetMetadata {
    
    private Map<Integer,String> colNames;

    public WorksheetMetadata() {
        super();
        colNames = new HashMap<Integer,String>();
    }

    public Map<Integer, String> getColNames() {
        return colNames;
    }

    public void setColNames(Map<Integer, String> colNames) {
        this.colNames = colNames;
    }
    
    

}
