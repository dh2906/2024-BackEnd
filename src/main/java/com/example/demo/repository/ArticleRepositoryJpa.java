package com.example.demo.repository;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.example.demo.domain.Article;

@Repository
public class ArticleRepositoryJpa implements ArticleRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public ArticleRepositoryJpa(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Article> findAll() {
        return entityManager.createQuery("""
                SELECT a
                FROM Article a
                """, Article.class).getResultList();
    }

    @Override
    public List<Article> findAllByBoardId(Long boardId) {
        return entityManager.createQuery("""
                SELECT a
                FROM Article a
                WHERE a.boardId = :boardId
                """, Article.class).setParameter("boardId", boardId).getResultList();
    }

    @Override
    public List<Article> findAllByMemberId(Long memberId) {
        return entityManager.createQuery("""
                SELECT a
                FROM Article a
                WHERE a.authorId = :memberId
                """, Article.class).setParameter("memberId", memberId).getResultList();
    }

    @Override
    public Article findById(Long id) {
        return entityManager.find(Article.class, id);
    }

    @Override
    public Article insert(Article article) {
        entityManager.persist(article);
        return findById(article.getId());
    }

    @Override
    public Article update(Long id, Article article) {
        Article temp = entityManager.find(Article.class, id);

        temp.setBoardId(article.getBoardId());
        temp.setTitle(article.getTitle());
        temp.setContent(article.getContent());

        return temp;
    }

    @Override
    public void deleteById(Long id) {
        entityManager.remove(findById(id));
    }
}
