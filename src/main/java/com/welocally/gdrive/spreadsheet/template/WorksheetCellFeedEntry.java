package com.welocally.gdrive.spreadsheet.template;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/*
 * {
  "version" : "1.0",
  "encoding" : "UTF-8",
  "entry" : {
    "xmlns" : "http://www.w3.org/2005/Atom",
    "xmlns$gs" : "http://schemas.google.com/spreadsheets/2006",
    "xmlns$batch" : "http://schemas.google.com/gdata/batch",
    "xmlns$gd" : "http://schemas.google.com/g/2005",
    "gd$etag" : "\"YHpdFQlSSit7Ig..\"",
    "id" : {
      "$t" : "https://spreadsheets.google.com/feeds/cells/0Avb_MOw4lfVndDh2S0ZhTFdYa0Y3Qk9uNEhHZnFYVUE/od6/R1C1"
    },
    "updated" : {
      "$t" : "2012-08-21T02:01:33.340Z"
    },
    "app$edited" : {
      "xmlns$app" : "http://www.w3.org/2007/app",
      "$t" : "2012-08-21T02:01:33.340Z"
    },
    "category" : [ {
      "scheme" : "http://schemas.google.com/spreadsheets/2006",
      "term" : "http://schemas.google.com/spreadsheets/2006#cell"
    } ],
    "title" : {
      "$t" : "A1"
    },
    "content" : {
      "$t" : "place_name"
    },
    "link" : [ {
      "rel" : "self",
      "type" : "application/atom+xml",
      "href" : "https://spreadsheets.google.com/feeds/cells/0Avb_MOw4lfVndDh2S0ZhTFdYa0Y3Qk9uNEhHZnFYVUE/od6/private/full/R1C1"
    }, {
      "rel" : "edit",
      "type" : "application/atom+xml",
      "href" : "https://spreadsheets.google.com/feeds/cells/0Avb_MOw4lfVndDh2S0ZhTFdYa0Y3Qk9uNEhHZnFYVUE/od6/private/full/R1C1"
    } ],
    "gs$cell" : {
      "row" : "1",
      "col" : "1",
      "inputValue" : "place_name",
      "$t" : "place_name"
    }
  }
}
 */
@JsonIgnoreProperties({"$t"})
public class WorksheetCellFeedEntry {
    public String row;
    public String col;
    public String inputValue;
    public String getRow() {
        return row;
    }
    public void setRow(String row) {
        this.row = row;
    }
    public String getCol() {
        return col;
    }
    public void setCol(String col) {
        this.col = col;
    }
    public String getInputValue() {
        return inputValue;
    }
    public void setInputValue(String inputValue) {
        this.inputValue = inputValue;
    }
}
