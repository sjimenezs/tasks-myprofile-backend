package myprofile.common.context;

import java.io.*;
import java.time.ZonedDateTime;
import java.util.Hashtable;
import java.util.Map;

public class ExecutionContext implements Serializable {
    private String username;
    private ZonedDateTime dateTime;
    private String TX;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ZonedDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(ZonedDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getTX() {
        return TX;
    }

    public void setTX(String TX) {
        this.TX = TX;
    }
}




