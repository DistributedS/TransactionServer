package utils;

import java.io.Serializable;

public class Message implements MessageTypes, Serializable {

    // contains the type of message, types are defined in interface MessageTypes
    int type;
    // contains the content that is specific to a certain message type
    Object content;

    public Message(int type, Object content) {
        this.type = type;
        this.content = content;
    }

    public Message() {
    }
    
// getter and setter methods for message type
    public void setType(int type) {
        this.type = type;
    }

    // getter and setter methods for parameters
    public int getType() {
        return type;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public Object getContent() {
        return content;
    }
}
