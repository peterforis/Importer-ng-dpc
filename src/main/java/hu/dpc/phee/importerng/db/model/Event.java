package hu.dpc.phee.importerng.db.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;

public class Event implements Persistable<Long> {

    @Id
    private Long key;
    private Long timeStamp;
    private String valueType;
    private String intent;
    private Long processDefinitionKey;
    private String eventText;

    public Event() {
    }

    public Long getKey() {
        return key;
    }

    public void setKey(Long key) {
        this.key = key;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getValueType() {
        return valueType;
    }

    public void setValueType(String valueType) {
        this.valueType = valueType;
    }

    public String getIntent() {
        return intent;
    }

    public void setIntent(String intent) {
        this.intent = intent;
    }

    public Long getProcessDefinitionKey() {
        return processDefinitionKey;
    }

    public void setProcessDefinitionKey(Long processDefinitionKey) {
        this.processDefinitionKey = processDefinitionKey;
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
