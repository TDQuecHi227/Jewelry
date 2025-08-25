package com.hhd.jewelry.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;

    private BigDecimal subtotalPrice;
    private BigDecimal discountAmount;
    private BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    private Channel channel = Channel.WEB;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;

    public enum Status {
        PENDING, PAID, SHIPPED, COMPLETED, CANCELED
    }

    public enum Channel {
        WEB, STORE, MOBILE_APP
    }
}
