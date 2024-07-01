package com.example.demo.repository;

import java.util.List;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import com.example.demo.domain.Member;

@Repository
public class MemberRepositoryJpa implements MemberRepository {

    private final EntityManager entityManager;

    public MemberRepositoryJpa(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Member> findAll() {
        return entityManager.createQuery("""
                SELECT m FROM Member m
                """, Member.class).getResultList();
    }

    @Override
    public Member findById(Long id) {
        return entityManager.find(Member.class, id);
    }

    @Override
    public Member insert(Member member) {
        entityManager.persist(member);
        return findById(member.getId());
    }

    @Override
    public Member update(Long id, Member member) {
        Member temp = entityManager.find(Member.class, id);

        temp.update(
                member.getName(),
                member.getEmail()
        );

        return temp;
    }

    @Override
    public void deleteById(Long id) {
        entityManager.remove(findById(id));
    }
}
