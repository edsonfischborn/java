package fastPg3.types;
import java.util.Date;

/**
 *
 * @author Édson Fischborn
 */
public class DeliveryDto {
    private long id;
    private long userId;
    private long deliveryManId;
    private DeliveryState deliveryState;
    private String description;

    // Address
    private String street;
    private String number;
    private String complement;
    private String zipCode;
    private String city;
    private String state;
    private String district;

    // Receiver
    private String receiverName;
    private String receiverCpf;
    private String receiverContact;

    // Date
    private Date createdAt;
    private Date updatedAt;

    public DeliveryDto(Delivery delivery, DeliveryState deliveryState) {
        this.setDeliveryData(delivery, deliveryState);
    }

    public void setDeliveryData(Delivery d, DeliveryState deliveryState){
        this.setId(d.getId());
        this.setDeliveryManId(d.getDeliveryManId());
        this.setUserId(d.getUserId());
        this.setDescription(d.getDescription());

        // State
        this.setDeliveryState(deliveryState);

        // Addres
        this.setStreet(d.getStreet());
        this.setNumber(d.getNumber());
        this.setComplement(d.getComplement());
        this.setZipCode(d.getZipCode());
        this.setCity(d.getCity());
        this.setState(d.getState());
        this.setDistrict(d.getDistrict());

        // Receiver
        this.setReceiverName(d.getReceiverName());
        this.setReceiverContact(d.getReceiverContact());
        this.setReceiverCpf(d.getReceiverCpf());

        // Date
        this.setUpdatedAt(d.getUpdatedAt());
        this.setCreatedAt(d.getCreatedAt());
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getDeliveryManId() {
        return deliveryManId;
    }

    public void setDeliveryManId(long deliveryManId) {
        this.deliveryManId = deliveryManId;
    }

    public DeliveryState getDeliveryState() {
        return deliveryState;
    }

    public void setDeliveryState(DeliveryState deliveryState) {
        this.deliveryState = deliveryState;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getComplement() {
        return complement;
    }

    public void setComplement(String complement) {
        this.complement = complement;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverCpf() {
        return receiverCpf;
    }

    public void setReceiverCpf(String receiverCpf) {
        this.receiverCpf = receiverCpf;
    }

    public String getReceiverContact() {
        return receiverContact;
    }

    public void setReceiverContact(String receiverContact) {
        this.receiverContact = receiverContact;
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

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }
}
