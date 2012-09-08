package com.welocally.gdrive.spreadsheet.template;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;

public class DocsFeed extends Feed {
    
        @JsonIgnore
        List<DocumentItem> items;

        public List<DocumentItem> getItems() {
            return items;
        }

        public void setItems(List<DocumentItem> items) {
            this.items = items;
        }

        
           
}
