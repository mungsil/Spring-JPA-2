package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;

    public void save(Order order) {
        em.persist(order);
    }

    public Order findOne(Long id) {
        return em.find(Order.class, id);
    }

    public List<Order> findAllByCriteria(OrderSearch orderSearch) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Order> cq = cb.createQuery(Order.class);
        Root<Order> o = cq.from(Order.class);
        Join<Order, Member> m = o.join("member", JoinType.INNER); //회원과 조인
        List<Predicate> criteria = new ArrayList<>();
        //주문 상태 검색
        if (orderSearch.getOrderStatus() != null) {
            Predicate status = cb.equal(o.get("status"),
                    orderSearch.getOrderStatus());
            criteria.add(status);
        }
        //회원 이름 검색
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            Predicate name =
                    cb.like(m.<String>get("name"), "%" +
                            orderSearch.getMemberName() + "%");
            criteria.add(name);
        }
        cq.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));
        TypedQuery<Order> query = em.createQuery(cq).setMaxResults(1000); //최대1000건
        return query.getResultList();
    }

    public List<Order> findAllWithMemberDelivery() {

            List<Order> resultList = em.createQuery("select o from Order o" +
                                    " join fetch o.member" +
                                    " join fetch o.delivery"
                            , Order.class) //결과로 가져온 데이터를 Order 엔티티 객체로 매핑하도록 지정, 지정하지 않을 시 object 배열 생성
                    .getResultList();
            return resultList;
    }

    //컬렉션 페치조인 사용(일대다 관계) 시에 페이징 사용 불가능 -> DB에서 테이블 row 뻥튀기 때문
    public List<Order> findAllWithItem() {

        List<Order> resultList
                = em.createQuery("select distinct o from Order o " +
                                "join fetch o.member m " + "join fetch o.delivery d " +
                                "join fetch o.orderItems oi " + "join fetch oi.item i "
                        , Order.class)
                .getResultList();

        return resultList;
    }

    public List<Order> findAllWithMemberDelivery(int offset, int limit) {

        List<Order> resultList = em.createQuery("select o from Order o" +
                                " join fetch o.member" +
                                " join fetch o.delivery"
                        , Order.class) //결과로 가져온 데이터를 Order 엔티티 객체로 매핑하도록 지정, 지정하지 않을 시 object 배열 생성
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();

        return resultList;
    }
}
