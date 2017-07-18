package org.mycore.oai.pmh;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Simple getter/setter implementation of the {@link Identify} interface.
 * 
 * @author Matthias Eichner
 */
public class SimpleIdentify implements Identify {

    protected String baseURL;

    protected List<String> adminEmailList = new ArrayList<String>();

    protected List<Description> descriptionList = new ArrayList<Description>();

    protected DeletedRecordPolicy deletedRecordPolicy;

    protected Instant earliestDatestamp;

    protected Granularity granularity;

    protected String repositoryName;

    protected String compression;

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
    public Instant getEarliestDatestamp() {
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

    @Override
    public String getCompression() {
        return this.compression;
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

    public void setEarliestDatestamp(Instant earliestDatestamp) {
        this.earliestDatestamp = earliestDatestamp;
    }

    public void setGranularity(Granularity granularity) {
        this.granularity = granularity;
    }

    public void setProtocolVersion(String protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    public void setCompression(String compression) {
        this.compression = compression;
    }

    public void setRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
    }

}
