package com.hhd.jwelery.repository;

import com.hhd.jwelery.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long>{
    Review findByProduct_ProductId(Long productId);
    Review save(Review review);
    void delete(Review review);
}
