package com.hhd.jewelry.service.specification;

import com.hhd.jewelry.entity.Category;
import com.hhd.jewelry.entity.Product;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public final class ProductSpecs {
    private ProductSpecs() {}

    /** Tìm theo tên (hỗ trợ cả displayName nếu tồn tại field này) */
    public static Specification<Product> keyword(String q) {
        if (q == null || q.isBlank()) return null;
        final String like = "%" + q.trim().toLowerCase() + "%";
        return (root, cq, cb) -> {
            Expression<String> nameExp = cb.lower(root.get("name"));
            // Nếu có field displayName thì OR thêm
            boolean hasDisplayName = root.getModel().getAttributes().stream()
                    .anyMatch(a -> a.getName().equals("displayName"));
            if (hasDisplayName) {
                Expression<String> dNameExp = cb.lower(root.get("displayName"));
                return cb.or(cb.like(nameExp, like), cb.like(dNameExp, like));
            }
            return cb.like(nameExp, like);
        };
    }

    /** Lọc theo danh sách ID danh mục */
    public static Specification<Product> categoryIds(List<Long> catIds) {
        if (catIds == null || catIds.isEmpty()) return null;
        return (root, cq, cb) -> {
            Join<Product, Category> cat = root.join("category", JoinType.LEFT);
            return cat.get("id").in(catIds);
        };
    }

    /** Lọc theo danh sách tên danh mục (khi bên UI gửi ?catName=...) */
    public static Specification<Product> categoryNames(List<String> names) {
        if (names == null || names.isEmpty()) return null;
        return (root, cq, cb) -> {
            Path<String> catName = root.join("category", JoinType.LEFT).get("name");
            CriteriaBuilder.In<String> in = cb.in(cb.lower(catName));
            names.forEach(n -> in.value(n.toLowerCase()));
            return in;
        };
    }

    /** Khoảng giá tự nhập (min/max) – dùng giá gốc */
    public static Specification<Product> priceBetween(BigDecimal min, BigDecimal max) {
        if (min == null && max == null) return null;
        return (root, cq, cb) -> {
            Path<BigDecimal> price = root.get("price");
            if (min != null && max != null) return cb.between(price, min, max);
            if (min != null) return cb.greaterThanOrEqualTo(price, min);
            return cb.lessThanOrEqualTo(price, max);
        };
    }

    /**
     * Mức giá cố định theo code từ URL:
     *  LE3   → ≤ 3,000,000
     *  B3_7  → 3,000,000 – 7,000,000
     *  B7_15 → 7,000,000 – 15,000,000
     *  GE15  → ≥ 15,000,000
     */
    public static Specification<Product> priceRange(String code) {
        if (code == null || code.isBlank()) return null;
        return switch (code) {
            case "LE3"   -> priceBetween(BigDecimal.ZERO, new BigDecimal("3000000"));
            case "B3_7"  -> priceBetween(new BigDecimal("3000000"), new BigDecimal("7000000"));
            case "B7_15" -> priceBetween(new BigDecimal("7000000"), new BigDecimal("15000000"));
            case "GE15"  -> priceBetween(new BigDecimal("15000000"), null);
            default      -> null;
        };
    }
    public static Specification<Product> keywordSafe(String q) {
        if (q == null || q.isBlank()) return null;
        final String like = "%" + q.trim().toLowerCase() + "%";
        return (root, cq, cb) -> {
            List<Predicate> ors = new ArrayList<>();
            try { ors.add(cb.like(cb.lower(root.get("name")), like)); } catch (IllegalArgumentException ignore) {}
            try { ors.add(cb.like(cb.lower(root.get("displayName")), like)); } catch (IllegalArgumentException ignore) {}
            return ors.isEmpty() ? cb.conjunction() : cb.or(ors.toArray(new Predicate[0]));
        };
    }

}
