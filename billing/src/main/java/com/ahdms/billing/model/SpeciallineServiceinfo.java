package com.ahdms.billing.model;

public class SpeciallineServiceinfo {
    private String id;

    private String specialLineId;

    private String serviceInfoId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getSpecialLineId() {
        return specialLineId;
    }

    public void setSpecialLineId(String specialLineId) {
        this.specialLineId = specialLineId == null ? null : specialLineId.trim();
    }

    public String getServiceInfoId() {
        return serviceInfoId;
    }

    public void setServiceInfoId(String serviceInfoId) {
        this.serviceInfoId = serviceInfoId == null ? null : serviceInfoId.trim();
    }
}