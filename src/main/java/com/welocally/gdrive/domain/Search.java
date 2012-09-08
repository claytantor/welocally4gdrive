package com.welocally.gdrive.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@Entity
@JsonIgnoreProperties(ignoreUnknown=true)

@Table(name="search")
public class Search extends BaseEntity  {
    
    public enum PublishStatus { 
        UNPUBLISHED, QUEUED, PUBLISHED; 
    } 
            
    @Column(name = "q")
    private String query;
    
    @Column(name = "lat")
    private String lat;
    
    @Column(name = "lng")
    private String lng;
    
    @Column(name = "radius")
    private Float radius;
    
    @Column(name = "publish_status")
    @Enumerated(EnumType.STRING) 
    private PublishStatus publishStatus;
          
    private String name;
    
    @OneToOne
    @JoinColumn(name="index_id")
    private Index index;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Index getIndex() {
        return index;
    }

    public void setIndex(Index index) {
        this.index = index;
    }

    public PublishStatus getPublishStatus() {
        return publishStatus;
    }

    public void setPublishStatus(PublishStatus publishStatus) {
        this.publishStatus = publishStatus;
    }

    public Float getRadius() {
        return radius;
    }

    public void setRadius(Float radius) {
        this.radius = radius;
    }

       
}
