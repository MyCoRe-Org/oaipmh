package org.mycore.oai.pmh;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SimpleIdentify implements Identify {

    protected String baseURL;
    protected List<String> adminEmailList = new ArrayList<String>();
    protected List<Description> descriptionList = new ArrayList<Description>();
    protected DeletedRecordPolicy deletedRecordPolicy;
    protected Date earliestDatestamp;
    protected Granularity granularity;
    protected String repositoryName;
    protected String protocolVersion = "2.0";

    @Override
    public List<String> getAdminEmailList() {
        return this.adminEmailList;
    }

    @Override
    public String getBaseURL() {
        return this.baseURL;
    }

    @Override
    public DeletedRecordPolicy getDeletedRecordPolicy() {
        return this.deletedRecordPolicy;
    }

    @Override
    public Date getEarliestDatestamp() {
        return this.earliestDatestamp;
    }

    @Override
    public Granularity getGranularity() {
        return this.granularity;
    }

    @Override
    public String getProtocolVersion() {
        return this.protocolVersion;
    }

    @Override
    public String getRepositoryName() {
        return this.repositoryName;
    }

    @Override
    public List<Description> getDescriptionList() {
        return this.descriptionList;
    }

    public void setAdminEmailList(List<String> adminEmailList) {
        this.adminEmailList = adminEmailList;
    }
    public void setBaseURL(String baseURL) {
        this.baseURL = baseURL;
    }
    public void setDeletedRecordPolicy(DeletedRecordPolicy deletedRecordPolicy) {
        this.deletedRecordPolicy = deletedRecordPolicy;
    }
    public void setDescriptionList(List<Description> descriptionList) {
        this.descriptionList = descriptionList;
    }
    public void setEarliestDatestamp(Date earliestDatestamp) {
        this.earliestDatestamp = earliestDatestamp;
    }
    public void setGranularity(Granularity granularity) {
        this.granularity = granularity;
    }
    public void setProtocolVersion(String protocolVersion) {
        this.protocolVersion = protocolVersion;
    }
    public void setRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
    }

}
