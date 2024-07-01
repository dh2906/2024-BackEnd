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
                """, Board.class).getResultList();
    }

    @Override
    public Board findById(Long id) {
        return entityManager.find(Board.class, id);
    }

    @Override
    public void deleteById(Long id) {
        entityManager.remove(findById(id));
    }

    @Override
    public Board insert(Board board) {
        entityManager.persist(board);
        return findById(board.getId());
    }

    @Override
    public Board update(Long id, Board board) {
        Board temp = entityManager.find(Board.class, id);

        temp.update(board.getName());

        return temp;
    }
}
