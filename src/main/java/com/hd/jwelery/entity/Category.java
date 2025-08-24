package com.hd.jwelery.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "category")
    private List<Product> products;
}
