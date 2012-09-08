package com.welocally.gdrive.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.welocally.gdrive.security.UserPrincipal;

@Entity
@JsonIgnoreProperties(ignoreUnknown=true)

@Table(name="ws_index")
public class Index extends BaseEntity  {
    
    public enum PublishStatus { 
        UNPUBLISHED, QUEUED, PUBLISHED, CHANGED; 
    } 
    
    @Column(name = "index_id")
    private String indexId;
    
    @Column(name = "ss_key")
    private String spreadsheetKey;
    
    @Column(name = "ws_id")
    private String worksheetFeed;

    @Column(name = "pk_field")
    private String primaryKey;
    
    @Column(name = "search_fields")
    private String searchFields;
    
    @Column(name = "lat_field")
    private String lat;
    
    @Column(name = "lng_field")
    private String lng;
    
    @Column(name = "recs_published")
    private Integer recordsPublished;
    
    @Column(name = "publish_status")
    @Enumerated(EnumType.STRING) 
    private PublishStatus publishStatus;
    
    private String name;
    
    @Transient
    @JsonIgnore
    private boolean indexExists;
    
    @OneToOne
    @JoinColumn(name="owner_user_principal_id")
    private UserPrincipal owner;
    
    public Index() {
        super();
    }
    
    public Index(String indexId) {
        super();
        this.indexId = indexId;
    }
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getWorksheetFeed() {
        return worksheetFeed;
    }
    public void setWorksheetFeed(String worksheetFeed) {
        this.worksheetFeed = worksheetFeed;
    }
    public UserPrincipal getOwner() {
        return owner;
    }
    public void setOwner(UserPrincipal owner) {
        this.owner = owner;
    }
    public String getPrimaryKey() {
        return primaryKey;
    }
    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }
    public String getSearchFields() {
        return searchFields;
    }
    public void setSearchFields(String searchFields) {
        this.searchFields = searchFields;
    }
    public String getLat() {
        return lat;
    }
    public void setLat(String lat) {
        this.lat = lat;
    }
    public String getLng() {
        return lng;
    }
    public void setLng(String lng) {
        this.lng = lng;
    }
    public String getIndexId() {
        return indexId;
    }
    public void setIndexId(String indexId) {
        this.indexId = indexId;
    }
    public String getSpreadsheetKey() {
        return spreadsheetKey;
    }
    public void setSpreadsheetKey(String spreadsheetKey) {
        this.spreadsheetKey = spreadsheetKey;
    }

    public Integer getRecordsPublished() {
        return recordsPublished;
    }

    public void setRecordsPublished(Integer recordsPublished) {
        this.recordsPublished = recordsPublished;
    }

    public PublishStatus getPublishStatus() {
        return publishStatus;
    }

    public void setPublishStatus(PublishStatus publishStatus) {
        this.publishStatus = publishStatus;
    }

    public boolean isIndexExists() {
        return indexExists;
    }

    public void setIndexExists(boolean indexExists) {
        this.indexExists = indexExists;
    }
    
    
    
       
}
