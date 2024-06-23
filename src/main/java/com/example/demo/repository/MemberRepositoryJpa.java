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
            """, Member.class)
            .getResultList();
    }

    @Override
    public Member findById(Long id) {
        return entityManager.createQuery("""
                SELECT m
                FROM Member m
                WHERE m.id = :id
                """, Member.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    @Override
    public Member update(Member member) {
        entityManager.persist(member);
        return findById(member.getId());
    }

    @Override
    public void deleteById(Long id) {
        entityManager.remove(findById(id));
    }
}
