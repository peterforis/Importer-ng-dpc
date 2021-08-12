package hu.dpc.phee.importerng.db.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ProcessDefinition {
    @Id
    private Long key;
    private Long timestamp;
    private int version;
    private String resourceName;
    private String bpmnProcessid;
    private Long processDefinitionKey;

    public Long getKey() {
        return key;
    }

    public void setKey(Long key) {
        this.key = key;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getBpmnProcessid() {
        return bpmnProcessid;
    }

    public void setBpmnProcessid(String bpmnProcessid) {
        this.bpmnProcessid = bpmnProcessid;
    }

    public Long getProcessDefinitionKey() {
        return processDefinitionKey;
    }

    public void setProcessDefinitionKey(Long processDefinitionKey) {
        this.processDefinitionKey = processDefinitionKey;
    }
}
