package springboot.catchshop.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import springboot.catchshop.domain.*;
import springboot.catchshop.dto.CartResponseDto;
import springboot.catchshop.repository.*;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * OrderService Test
 * author: soohyun, last modified: 22.03.07
 */

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired EntityManager em;
    @Autowired OrderRepository orderRepository;
    @Autowired OrderDetailRepository orderDetailRepository;
    @Autowired OrderService orderService;
    @Autowired CartService cartService;

    private Address address;
    private User user;
    private Product product1, product2;
    private Cart cart1, cart2;

    @BeforeEach
    public void berforeEach() {

        // 사용자 생성
        address = new Address("road1", "detail1", "11111");
        user = new User("user1", "user1", "user1", "01012345678", address, "USER", LocalDateTime.now());
        em.persist(user);

        // 상품 생성
        product1 = new Product();
        product1.changePrice(10000);
        product1.changeStock(10);
        em.persist(product1);

        product2 = new Product();
        product2.changePrice(10000);
        product2.changeStock(10);
        em.persist(product2);

        // 장바구니 생성
        cart1 = new Cart(product1, user.getId(), 1);
        em.persist(cart1);

        cart2 = new Cart(product2, user.getId(), 11);
        em.persist(cart2);
    }

    @Test
    @DisplayName("주문 생성")
    public void createOrder() {

        // given
        CartResponseDto carts = cartService.orderCartList(user.getId());
        Order order = new Order(user, "name1", "01012345678", address, carts.getTotalAllProductPrice(), carts.getShippingFee());
        Long userId = user.getId();

        // when
        Long saveId = orderService.createOrder(order, userId, carts.getCartList());

        // then
        Optional<Order> findOrder = orderRepository.findById(saveId);
        List<OrderDetail> findOrderDetail = orderDetailRepository.findByOrderId(saveId);

        assertEquals(findOrder.get().getId(), saveId);
        assertEquals(findOrder.get().getUser(), user);
        assertEquals(findOrder.get().getOrderName(), "name1");
        assertEquals(findOrder.get().getOrderTel(), "01012345678");
        assertEquals(findOrder.get().getAddress(), address);
        assertEquals(findOrder.get().getOrderStatus(), OrderStatus.READY);
        assertEquals(findOrder.get().getTotalPrice(), 10000);
        assertEquals(findOrder.get().getShippingFee(), 3000);

        assertEquals(findOrderDetail.size(), 1);
        assertEquals(findOrderDetail.get(0).getOrder(), findOrder.get());
        assertEquals(findOrderDetail.get(0).getProduct(), product1);
        assertEquals(findOrderDetail.get(0).getOrderCount(), 1);
        assertEquals(findOrderDetail.get(0).getOrderPrice(), 10000);
    }



}