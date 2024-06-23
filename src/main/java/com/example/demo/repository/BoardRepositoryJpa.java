package com.example.demo.repository;

import java.util.List;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import com.example.demo.domain.Board;

@Repository
public class BoardRepositoryJpa implements BoardRepository {

    private final EntityManager entityManager;

    public BoardRepositoryJpa(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Board> findAll() {
        return entityManager.createQuery("""
                SELECT b
                FROM Board b
                """, Board.class)
                .getResultList();
    }

    @Override
    public Board findById(Long id) {
        return entityManager.createQuery("""
                SELECT b 
                FROM Board b 
                WHERE b.id = :id
                """, Board.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    @Override
    public void deleteById(Long id) {
        entityManager.remove(findById(id));
    }

    @Override
    public Board update(Board board) {
        entityManager.persist(board);
        return findById(board.getId());
    }
}
