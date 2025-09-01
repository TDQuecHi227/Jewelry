package com.hhd.jewelry.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.*;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    private String gemstone;

    @Column(nullable = false)
    private String material;

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false, unique = true)
    private String serialNumber;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer discount;

    @Column(name = "`order`", columnDefinition = "INT DEFAULT 0")
    private Integer order;

    @Column(nullable = false)
    private String gender;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @ToString.Exclude
    private Category category;

    @ManyToOne
    @JoinColumn(name = "collection_id")
    @ToString.Exclude
    private Collection collection;

    @Column(nullable = false)
    private Integer stockQuantity;

    @ElementCollection
    @CollectionTable(name = "product_images", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "image_url")
    private List<String> imageUrls = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<OrderItem> orderItems;
}
