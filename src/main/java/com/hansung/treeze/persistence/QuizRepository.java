package com.hansung.treeze.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.hansung.treeze.model.Quiz;

public interface QuizRepository extends JpaRepository<Quiz, Long> ,JpaSpecificationExecutor<Quiz>{

}

