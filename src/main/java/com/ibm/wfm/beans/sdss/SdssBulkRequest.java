package com.ibm.wfm.beans.sdss;

import java.util.List;

public class SdssBulkRequest {

    private String action;
    private List<Long> changeIds;
    private String rejectionReason;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public List<Long> getChangeIds() {
        return changeIds;
    }

    public void setChangeIds(List<Long> changeIds) {
        this.changeIds = changeIds;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }
}