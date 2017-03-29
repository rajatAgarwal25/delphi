package com.proptiger.delphi.model.meta;

import org.springframework.data.annotation.Id;

public class SerializedLeadInfo {

    @Id
    private String id;
    private Long   startLeadId;
    private Long   endLeadId;
    private String uri;

    public SerializedLeadInfo(Long startLeadId, Long endLeadId, String uri) {
        super();
        this.startLeadId = startLeadId;
        this.endLeadId = endLeadId;
        this.uri = uri;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getStartLeadId() {
        return startLeadId;
    }

    public void setStartLeadId(Long startLeadId) {
        this.startLeadId = startLeadId;
    }

    public Long getEndLeadId() {
        return endLeadId;
    }

    public void setEndLeadId(Long endLeadId) {
        this.endLeadId = endLeadId;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

}
