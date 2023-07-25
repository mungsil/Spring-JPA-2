package jpabook.jpashop.repository.order.simplequery;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderSimpleQueryDto {

        private Long orderId;
        private String name;
        private LocalDateTime orderDateTime;
        private OrderStatus orderStatus;
        private Address address;

        public OrderSimpleQueryDto(Long orderId, String name, LocalDateTime orderDateTime, OrderStatus status, Address address) {

            this.orderId = orderId;
            this.name = name; //Lazy 초기화
            this.orderDateTime = orderDateTime;
            this.orderStatus = status;
            this.address = address;//Lazy 초기화

    }

}