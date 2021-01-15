package fastPg3.types;

/**
 *
 * @author Édson Fischborn
 */
public class DeliveryDetailDto extends DeliveryDto {
    private UserDto user;
    private DeliveryMan deliveryMan;
    private String receiverSignature;

    public DeliveryDetailDto(Delivery delivery, DeliveryState deliveryState, UserDto user, DeliveryMan deliveryMan) {
        super(delivery, deliveryState);
        setUser(user);
        setDeliveryMan(deliveryMan);
        setReceiverSignature(delivery.getReceiverSignature());
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public DeliveryMan getDeliveryMan() {
        return deliveryMan;
    }

    public void setDeliveryMan(DeliveryMan deliveryMan) {
        this.deliveryMan = deliveryMan;
    }

    public String getReceiverSignature() {
        return receiverSignature;
    }

    public void setReceiverSignature(String receiverSignature) {
        this.receiverSignature = receiverSignature;
    }
}
