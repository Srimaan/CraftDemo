package com.intuit.nasa.api.planetary;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class Sounds {
    public final long count;
    public final Result results[];

    @JsonCreator
    public Sounds(@JsonProperty("count") long count, @JsonProperty("results") Result[] results){
        this.count = count;
        this.results = results;
    }
    
   

    public static final class Result {
        public final String description;
        public final String title;
        public final String download_url;
        public final long duration;
        public final String last_modified;
        public final String stream_url;
        public final String tag_list;
        public final long id;
        public final String license;

        @JsonCreator
        public Result(@JsonProperty("description") String description, @JsonProperty("license")  String license, @JsonProperty("title") String title, @JsonProperty("download_url") String download_url, @JsonProperty("duration") long duration, @JsonProperty("last_modified") String last_modified, @JsonProperty("stream_url") String stream_url, @JsonProperty("tag_list") String tag_list, @JsonProperty("id") long id){
            this.description = description;
            this.title = title;
            this.download_url = download_url;
            this.duration = duration;
            this.last_modified = last_modified;
            this.stream_url = stream_url;
            this.tag_list = tag_list;
            this.id = id;
            this.license=license;
        }
        public String getDescription(){
        	return description;
        }
    
       
    }
}

