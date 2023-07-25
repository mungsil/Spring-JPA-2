package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepositoryOld;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class) //junit 시작할 때 spring이랑 같이 시작할래
@SpringBootTest //스프링 부트를 띄운 상태로 테스트를 하기 위함
@Transactional //롤백
public class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired
    MemberRepositoryOld memberRepositoryOld;
    @Autowired EntityManager em;

    @Test
    public void 회원가입() throws Exception{
        //given 이런게이런게 주어졌을때
        Member member = new Member();
        member.setName("황윤정");

        //when 이걸 실행하면
        Long saveId = memberService.join(member);

        //then 결과가 이렇게 나와야해
        em.flush(); //insert 보여줌
        assertEquals(member, memberRepositoryOld.findOne(saveId));
        
    }

    @Test(expected = IllegalStateException.class) // 이걸 쓰므로 try~catch문 안써도 됨
    public void 중복_회원_예외() throws Exception{
        //given
        Member member1 = new Member();
        member1.setName("kim1");

        Member member2 = new Member();
        member2.setName("kim1");

        //when
        memberService.join(member1);
        memberService.join(member2); //예외 발생

        //then
        fail("예외가 발생해야 한다.");
    }
}