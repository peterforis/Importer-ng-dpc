package hu.dpc.phee.importerng.db.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;

public class Event implements Persistable<Long> {

    @Id
    private Long key;
    private Long processDefinitionKey;
    private int version;
    private Long timeStamp;
    private String eventText;

    public Event() {
    }

    public Long getKey() {
        return key;
    }

    public void setKey(Long key) {
        this.key = key;
    }

    public Long getProcessDefinitionKey() {
        return processDefinitionKey;
    }

    public void setProcessDefinitionKey(Long processDefinitionKey) {
        this.processDefinitionKey = processDefinitionKey;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getEventText() {
        return eventText;
    }

    public void setEventText(String eventText) {
        this.eventText = eventText;
    }

    @Override
    public Long getId() {
        return key;
    }

    @Override
    public boolean isNew() {
        return true;
    }
}
