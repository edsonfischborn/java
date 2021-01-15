package fastPg3.types;

import java.util.Date;

/**
 *
 * @author Édson Fischborn
 */
public class Message {
    private Long id = null;
    private String name;
    private String email;
    private String message;
    private int read;

    // Date
    private Date createdAt;
    private Date updatedAt;
    
    public Message(String name, String email, String message){
        this.setName(name);
        this.setEmail(email);
        this.setMessage(message);
    }
  
    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public int getRead() {
        return read;
    }

    public void setRead(int read) {
        this.read = read;
    }
}
