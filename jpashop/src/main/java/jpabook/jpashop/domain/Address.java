package jpabook.jpashop.domain;

import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable//jpa의 내장 타입이므로
@Getter
public class Address {

    private String city;
    private String street;
    private String zipcode;

    //jpa 스펙상 그냥 만든 것, 값 타입은 변경 불가능하게 설계함
    protected Address() {
    }

    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
