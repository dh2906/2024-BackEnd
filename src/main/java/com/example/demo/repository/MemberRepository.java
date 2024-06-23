package com.example.demo.repository;

import java.util.List;

import com.example.demo.domain.Member;

public interface MemberRepository {

    List<Member> findAll();

    Member findById(Long id);

    Member update(Member member);

    void deleteById(Long id);
}
