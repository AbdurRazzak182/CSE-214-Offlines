package model;

import java.time.LocalDateTime;
import java.util.List;


/**
 * Represents a placed food order.
 *
 * Design note for the assignment:
 * This class works, but its construction API is intentionally awkward.
 * The long constructor mixes required fields, optional fields, defaults,
 * validation, and pricing flags. Students should refactor this design without
 * changing the observable behavior of the program.
 */

public class Order {
    public static final double DELIVERY_FEE = 80.0;
    public static final double RUSH_FEE = 120.0;
    public static final double GIFT_WRAP_FEE = 50.0;
    private final String orderId;
    private final String customerName;
    private final String phone;
    private final List<OrderItem> items;
    private final DeliveryType deliveryType;
    private final PaymentMethod paymentMethod;
    private final String couponCode;
    private final int loyaltyPointsToRedeem;
    private final String specialInstructions;
    private final String deliveryAddress;
    private final LocalDateTime scheduledTime;
    private final boolean giftWrap;
    private final boolean cutleryRequired;
    private final boolean rushOrder;



    public Order(OrderBuilder orderBuilder) {

        this.orderId =orderBuilder.orderId;
        this.customerName = orderBuilder.customerName;
        this.phone = orderBuilder.phone;
        
        this.deliveryType = orderBuilder.deliveryType;
        this.paymentMethod = orderBuilder.paymentMethod;
        this.scheduledTime = orderBuilder.scheduledTime;
        this.couponCode = orderBuilder.couponCode;
        this.giftWrap = orderBuilder.giftWrap;
        this.cutleryRequired = orderBuilder.cutleryRequired;
        this.loyaltyPointsToRedeem = orderBuilder.loyaltyPointsToRedeem;
        this.rushOrder = orderBuilder.rushOrder;
        this.specialInstructions = orderBuilder.specialInstructions;
        this.items = orderBuilder.items;
        this.deliveryAddress = orderBuilder.deliveryAddress;
    }


    public String getOrderId() {
        return orderId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getPhone() {
        return phone;
    }

    public DeliveryType getDeliveryType() {
        return deliveryType;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public LocalDateTime getScheduledTime() {
        return scheduledTime;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public boolean isGiftWrap() {
        return giftWrap;
    }

    public boolean isCutleryRequired() {
        return cutleryRequired;
    }

    public int getLoyaltyPointsToRedeem() {
        return loyaltyPointsToRedeem;
    }

    public boolean isRushOrder() {
        return rushOrder;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public String getSpecialInstructions() {
        return specialInstructions;
    }

    public double getSubtotal() {
        return items.stream().mapToDouble(OrderItem::getSubtotal).sum();
    }

    public double getDiscount() {
        double couponDiscount = 0.0;
        if ("WELCOME10".equals(couponCode)) {
            couponDiscount = getSubtotal() * 0.10;
        } else if ("FAMILY15".equals(couponCode) && getSubtotal() >= 1000.0) {
            couponDiscount = getSubtotal() * 0.15;
        }

        double loyaltyDiscount = Math.min(loyaltyPointsToRedeem, 100);
        return couponDiscount + loyaltyDiscount;
    }

    public double getServiceCharges() {
        double charges = 0.0;
        if (deliveryType == DeliveryType.DELIVERY) {
            charges += DELIVERY_FEE;
        }
        if (rushOrder) {
            charges += RUSH_FEE;
        }
        if (giftWrap) {
            charges += GIFT_WRAP_FEE;
        }
        return charges;
    }

    public double getTotal() {
        return Math.max(0.0, getSubtotal() + getServiceCharges() - getDiscount());
    }


}
