package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryDto;
import jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


/**
 * xToOne(ManyToOne, OneToOne)
 * Order
 * Order -> Member
 * Order -> Delivery
 */

/**
 * dto나 result같은 얘들은 왜 static class로 선언할까?
 */

/**
 * map(a->b): a를 b로 바꾸는 것
 */

@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

//    private final OrderService orderService;

    private final OrderRepository orderRepository;
    private final OrderSimpleQueryRepository orderSimpleQueryRepository;

    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1() {
        List<Order> all = orderRepository.findAllByCriteria(new OrderSearch());

        //orderItems 정보는 필요하지 않음
        for (Order order : all) {
            order.getMember().getName(); //Lazy 강제 초기화
            order.getDelivery().getAddress(); //Lazy 강제 초기환
        }
        return all;
    }

    //List 반환말고 한 번 감싸기
    @GetMapping("/api/v2/simple-orders")
    public Result orderV2() {

        //엔티티 노출은 권장하지 않는다.
        List<Order> all = orderRepository.findAllByCriteria(new OrderSearch());

        //DTO 를 사용
        List<OrderSimpleDto> collect =
                all.stream()
                        .map(order -> new OrderSimpleDto(order))
                        .collect(Collectors.toList());

        return new Result<List>(collect);
    }

    @GetMapping("/api/v3/simple-orders")
    public Result orderV3() {
        List<Order> all = orderRepository.findAllWithMemberDelivery();

        List<OrderSimpleDto> collect = all.stream()
                .map(o -> new OrderSimpleDto(o))
                .collect(Collectors.toList());

        return new Result(collect);
    }

    @GetMapping("/api/v4/simple-orders")
    public Result orderV4() {
        return new Result(orderSimpleQueryRepository.findOrderDtos());
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }

    @Data
    static class OrderSimpleDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate; //주문시간
        private OrderStatus orderStatus;
        private Address address;
        public OrderSimpleDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName();
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();
        }
    }

}
