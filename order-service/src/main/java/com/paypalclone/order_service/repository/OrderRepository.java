package com.paypalclone.order_service.repository;

import com.paypalclone.order_service.entity.Order;
import com.paypalclone.order_service.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {


    List<Order> findByBuyerId(Long buyerId);

    List<Order> findByMerchantId(Long merchantId);

    List <Order> findByBuyerIdAndStatus(Long buyerId, OrderStatus status);

    List<Order> findByMerchantIdAndStatus(Long merchantId, OrderStatus status);
}
