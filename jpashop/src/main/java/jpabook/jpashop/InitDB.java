package jpabook.jpashop;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.item.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Component
@RequiredArgsConstructor
public class InitDB {

    private final InitService initService;

    //의존성 주입이 끝난 후 실행되어야 하는 메소드 - @PostConstruct
    @PostConstruct
    public void init() {
        initService.dbInit1();
        initService.dbInit2();
    }

    @Component //component를 다는 기준이 뭐고 :1. '의존성 주입을 하기 위함'도 일종의 이유임. 2. 스프링이 제공하는 기능인 트랜잭션 관리 등을 사용 가능
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        @PersistenceContext //강의에서 해당 어노테이션을 안다는 이유 -> @RequiredArgsConstructor으로 엔티티매니저를 생성자 주입
        private final EntityManager em; //엔티티를 조회하고 변경하기 위해서 필요함
        public void dbInit1() {

            Member member_D_O = createMember("도경수", "서울", "SM_신사옥", "957329");
            em.persist(member_D_O);

            Book itIsOk = createBook("느리게살아도 괜찮아_", 30000, 6);
            em.persist(itIsOk);
           
            Book fail_lol = createBook("당신이 실패하는 이유ㅋ", 25000, 30);
            em.persist(fail_lol);

            OrderItem orderItemA = OrderItem.createOrderItem(itIsOk, 20000, 5);
            OrderItem orderItemB = OrderItem.createOrderItem(fail_lol, 25000, 1);

            Order order = Order.createOrder(member_D_O,createDelivery(member_D_O), orderItemA,orderItemB);
            em.persist(order);

        }


        public void dbInit2() {

            Member member = createMember("김송은", "서울", "하이브_신사옥", "111111");
            em.persist(member);

            Book marry_you = createBook("도경수와 결혼하는법", 90000, 1);
            em.persist(marry_you);

            Book life_is_easy = createBook("인생 커비처럼 살기", 50000, 30);
            em.persist(life_is_easy);

            OrderItem orderItemA = OrderItem.createOrderItem(marry_you, 100000, 1);
            OrderItem orderItemB = OrderItem.createOrderItem(life_is_easy, 50000, 1);

            Order order = Order.createOrder(member,createDelivery(member), orderItemA,orderItemB);
            em.persist(order);

        }

        private Delivery createDelivery(Member member) {
             Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress());

            return delivery;
        }

        private Member createMember(String name, String city, String street, String zipcode) {
            Member member = new Member();
            member.setName(name);
            member.setAddress(new Address(city, street, zipcode));

            return member;
        }

        private Book createBook(String name, int price, int quantity) {
            Book book = new Book();
            book.setName(name);
            book.setPrice(price);
            book.setStockQuantity(quantity);
            
            return book;
        }
    }
}
