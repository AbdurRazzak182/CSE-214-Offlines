package service;

import model.DeliveryType;
import model.MenuItem;
import model.Order;
import model.OrderBuilder;
import model.OrderItem;
import model.PaymentMethod;
import model.Size;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Coordinates order creation.
 *
 * Several methods below repeat long Order constructor calls with many optional
 * parameters. That is intentional assignment material for refactoring.
 */


public class OrderService {
    private int nextNumber = 1001;

    public OrderItem createOrderItem(MenuItem item, int quantity, Size size, boolean extraCheese, boolean spicy, String note) {
        return new OrderItem(item, quantity, size, extraCheese, spicy, note);
    }

    public Order createDeliveryOrder(String customerName,
                                     String phone,
                                     String address,
                                     List<OrderItem> items,
                                     String couponCode,
                                     boolean rushOrder,
                                     String specialInstructions) {
        return new OrderBuilder(nextOrderId(), customerName, phone,items)
        .deliveryType(DeliveryType.DELIVERY)
        .deliveryAddress(address)
        .couponCode(couponCode)
        .rushOrder(rushOrder)
        .specialInstructions(specialInstructions)
        .build();
    }

    public Order createPickupOrder(String customerName, String phone, List<OrderItem> items) {
        return new OrderBuilder(nextOrderId(),customerName,phone,items).build();
        
    }

    public Order createScheduledGiftOrder(String customerName,
                                          String phone,
                                          String address,
                                          List<OrderItem> items,
                                          LocalDateTime scheduledTime) {
        return new OrderBuilder(nextOrderId(),customerName,phone,items)
        .deliveryType(DeliveryType.DELIVERY)
        .deliveryAddress(address)
        .paymentMethod(PaymentMethod.CARD)
        .scheduledTime(scheduledTime)
        .couponCode("WELCOME10")
        .giftWrap(true)
        .loyaltyPointsToRedeem(25)
        .cutleryRequired(false)
        .specialInstructions("Please call before delivery")
        .build();
    }

    public Order createSampleFamilyOrder(MenuCatalog catalog) {
        List<OrderItem> items = new ArrayList<>();
        items.add(new OrderItem(catalog.findByCode("P01"), 2, Size.LARGE, true, false, "half spicy"));
        items.add(new OrderItem(catalog.findByCode("B02"), 3, Size.MEDIUM, true, true, ""));
        items.add(new OrderItem(catalog.findByCode("D02"), 4, Size.MEDIUM, false, false, "less sugar"));
        items.add(new OrderItem(catalog.findByCode("S02"), 2, Size.LARGE, false, true, ""));

        return new OrderBuilder(nextOrderId(),"Sample Family", "01711111111", items)
         .deliveryType(DeliveryType.DELIVERY)
         .deliveryAddress("House 25, Road 4, Dhanmondi") 
         .paymentMethod(PaymentMethod.MOBILE_BANKING) 
         .couponCode("FAMILY15")
         .loyaltyPointsToRedeem(50)
         .rushOrder(true) 
         .specialInstructions("Deliver together")
        .build();
    }

    private String nextOrderId() {
        return "FF-" + nextNumber++;
    }
}

