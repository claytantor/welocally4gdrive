package com.welocally.gdrive.spreadsheet.template;

import java.util.List;

public class WorksheetGrid {
    
    private String name;
    private String id;
    
    private List<ExportRow> rows;
    //private Map<Integer,String> colNames;
    private WorksheetMetadata metadata;
    

    public WorksheetGrid() {
        super();
        //colNames = new HashMap<Integer,String>();
        metadata = new WorksheetMetadata();
    }

    public List<ExportRow> getRows() {
        return rows;
    }

    public void setRows(List<ExportRow> rows) {
        this.rows = rows;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public WorksheetMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(WorksheetMetadata metadata) {
        this.metadata = metadata;
    }

/*    public Map<Integer, String> getColNames() {
        return colNames;
    }

    public void setColNames(Map<Integer, String> colNames) {
        this.colNames = colNames;
    }*/


    
}
