package springboot.catchshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import springboot.catchshop.domain.Cart;

import java.util.List;

// Cart Repository
// author: soohyun, last modified: 22.02.13

public interface CartRepository extends JpaRepository<Cart, Long> {

    // 로그인한 사용자 id로 장바구니 목록 조회
    List<Cart> findByUserId(Long userId);
}
