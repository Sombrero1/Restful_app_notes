package com.example.trpp_project.repo;

import com.example.trpp_project.models.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Long> {
}
