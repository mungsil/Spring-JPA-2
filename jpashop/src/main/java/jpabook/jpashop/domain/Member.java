package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter @Getter
public class Member {

    @Id @GeneratedValue
    @Column(name="member_id")
    private Long id;

    private String name;

    @Embedded
    private Address address;

    @JsonIgnore
    //@JsonIgnore //회원을 조회할 때 회원의 주문정보는 필요하지 않을 때
    @OneToMany(mappedBy = "member") //일대다 관계
    //컬렉션은 필드에서 초기화하자. null 문제에서 안전하다.
    private List<Order> orders = new ArrayList<>();
}
