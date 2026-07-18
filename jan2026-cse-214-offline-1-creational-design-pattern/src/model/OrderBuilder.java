package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class OrderBuilder {
    // required fields 
    String orderId;
    String customerName;
    String phone;
    List<OrderItem> items;

    // optional fields with default values 
    DeliveryType deliveryType = DeliveryType.PICKUP;
    PaymentMethod paymentMethod = PaymentMethod.CASH;
    String couponCode = "";
    int loyaltyPointsToRedeem = 0;
    String specialInstructions = "";
    String deliveryAddress = "";
    LocalDateTime scheduledTime = null;
    boolean giftWrap = false;
    boolean cutleryRequired = true;
    boolean rushOrder = false;

    public OrderBuilder(String orderId,String customerName,String phone,List<OrderItem>items){
        this.orderId = orderId;
        this.customerName = customerName;
        this.phone = phone;
        Objects.requireNonNull(items, "Items cannot be null");
        if (items.isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one item");
        }
        this.items = Collections.unmodifiableList(new ArrayList<>(items));
    }

    public OrderBuilder deliveryType(DeliveryType deliveryType){
        if(deliveryType != null){
            this.deliveryType = deliveryType;
        }
        return this;
    }

    public OrderBuilder paymentMethod(PaymentMethod paymentMethod){
        if(paymentMethod != null){
            this.paymentMethod = paymentMethod;
        }
        return this;
           
    }
    public OrderBuilder couponCode(String couponCode){
        if(couponCode != null){
            this.couponCode = couponCode.trim().toUpperCase();
        }
        return this;
    }
    public OrderBuilder loyaltyPointsToRedeem(int loyaltyPointsToRedeem){
        this.loyaltyPointsToRedeem = Math.max(0, loyaltyPointsToRedeem);
        return this;
    }
    public OrderBuilder specialInstructions(String specialInstructions){
        if(specialInstructions != null){
            this.specialInstructions = specialInstructions.trim();
        }
        return this;
    }
    public OrderBuilder deliveryAddress(String deliveryAddress){
        if(deliveryAddress != null){
            this.deliveryAddress = deliveryAddress.trim();
        }
        return this;
    }
    public OrderBuilder scheduledTime(LocalDateTime scheduledTime ){
        this.scheduledTime = scheduledTime;
        return this;
    }
    public OrderBuilder giftWrap(boolean giftWrap ){
        this.giftWrap = giftWrap;
        return this;
    }
    public OrderBuilder cutleryRequired(boolean cutleryRequired ){
        this.cutleryRequired = cutleryRequired;
        return this;
    }
    public OrderBuilder rushOrder(boolean rushOrder ){
        this.rushOrder = rushOrder;
        return this;
    }

    public Order build(){
        if (this.deliveryType == DeliveryType.DELIVERY) {
            requireNonBlank(this.deliveryAddress, "Delivery address");
        }
        return new Order(this);
    }



    private static String requireNonBlank(String value, String fieldName) {
        Objects.requireNonNull(value, fieldName + " cannot be null");
        String trimmed = value.trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be blank");
        }
        return trimmed;
    }
}
