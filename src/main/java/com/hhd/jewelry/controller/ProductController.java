package com.hhd.jewelry.controller;

import com.hhd.jewelry.entity.Product;
import com.hhd.jewelry.repository.ProductRepository;
import com.hhd.jewelry.service.ProductService;
import com.hhd.jewelry.service.specification.ProductSpecs;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.*;

@Controller
@RequiredArgsConstructor
public class ProductController {

    private final ProductRepository productRepository;
    private final ProductService productService;

    @GetMapping("/products")
    public String products(
            @RequestParam(value = "q", required = false) String q,
            @RequestParam(value = "cat", required = false) List<Long> cat,
            @RequestParam(value = "catName", required = false) List<String> catName,
            @RequestParam(value = "priceRange", required = false) String priceRange,
            @RequestParam(value = "minPrice", required = false) BigDecimal minPrice,
            @RequestParam(value = "maxPrice", required = false) BigDecimal maxPrice,
            @RequestParam(value = "sort", defaultValue = "") String sort,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "12") int size,
            Model model
    ) {
        boolean initial = isInitialLoad(q, cat, catName, priceRange, minPrice, maxPrice, sort, page);

        // 1) LUÔN chuẩn bị params để view không lỗi
        Map<String, Object> params = new LinkedHashMap<>();
        if (q != null) params.put("q", q);
        if (cat != null) params.put("cat", cat);
        if (catName != null) params.put("catName", catName);
        if (priceRange != null) params.put("priceRange", priceRange);
        if (minPrice != null) params.put("minPrice", minPrice);
        if (maxPrice != null) params.put("maxPrice", maxPrice);
        if (!sort.isBlank()) params.put("sort", sort);
        model.addAttribute("params", params);

        if (initial) {
            // 2) Lần đầu: load toàn bộ (không phân trang)
            List<Product> products = productService.getAllProducts();
            model.addAttribute("products", products);
            model.addAttribute("pageData", Page.empty()); // để th:if="${pageData.totalPages > 1}" không hiển thị
            return "client/product/show"; // <-- ĐÚNG với cấu trúc bạn chụp
        }

        // 3) Có filter → Specification + Pageable
        Specification<Product> priceSpec = Optional
                .ofNullable(ProductSpecs.priceRange(priceRange))
                .orElse(ProductSpecs.priceBetween(minPrice, maxPrice));

        Specification<Product> spec = Specification.allOf(
                ProductSpecs.keywordSafe(q),     // an toàn với name/displayName
                ProductSpecs.categoryIds(cat),
                ProductSpecs.categoryNames(catName),
                priceSpec
        );

        Pageable pageable = PageRequest.of(page, size, parseSort(sort));
        Page<Product> result = (spec == null)
                ? productRepository.findAll(pageable)
                : productRepository.findAll(spec, pageable);

        model.addAttribute("products", result.getContent());
        model.addAttribute("pageData", result);
        return "client/product/show";
    }

    private boolean isInitialLoad(
            String q, List<Long> cat, List<String> catName, String priceRange,
            BigDecimal minPrice, BigDecimal maxPrice, String sort, int page
    ) {
        return (q == null || q.isBlank())
                && (cat == null || cat.isEmpty())
                && (catName == null || catName.isEmpty())
                && (priceRange == null || priceRange.isBlank())
                && minPrice == null
                && maxPrice == null
                && (sort == null || sort.isBlank())
                && page == 0;
    }

    private Sort parseSort(String sort) {
        return switch (sort) {
            case "priceAsc"  -> Sort.by(Sort.Direction.ASC, "price");
            case "priceDesc" -> Sort.by(Sort.Direction.DESC, "price");
            case "nameAsc"   -> Sort.by(Sort.Direction.ASC, "name");
            case "nameDesc"  -> Sort.by(Sort.Direction.DESC, "name");
            default          -> Sort.unsorted();
        };
    }
}
