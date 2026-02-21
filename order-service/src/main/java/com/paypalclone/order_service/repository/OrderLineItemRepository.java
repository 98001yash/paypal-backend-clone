package com.paypalclone.order_service.repository;

import com.paypalclone.order_service.entity.OrderLineItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderLineItemRepository extends JpaRepository<OrderLineItem, Long> {

    List<OrderLineItem> findByOrderId(Long orderId);
}
