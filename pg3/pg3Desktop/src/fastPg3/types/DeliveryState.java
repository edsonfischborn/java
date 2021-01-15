package fastPg3.types;

import java.util.Date;

/**
 *
 * @author Édson Fischborn
 */
public class DeliveryState {
    private long id;
    private String stateColor;
    private String stateName;

    // Date
    private Date createdAt;
    private Date updatedAt;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStateColor() {
        return stateColor;
    }

    public void setStateColor(String stateColor) {
        this.stateColor = stateColor;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString(){
        return this.stateName;
    }
}
