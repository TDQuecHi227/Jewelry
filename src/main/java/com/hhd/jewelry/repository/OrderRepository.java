package com.hhd.jewelry.repository;

import com.hhd.jewelry.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer> {
}
