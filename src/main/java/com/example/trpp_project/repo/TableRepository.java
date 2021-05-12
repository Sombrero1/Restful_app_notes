package com.example.trpp_project.repo;

import com.example.trpp_project.models.Table;
import com.example.trpp_project.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TableRepository  extends JpaRepository<Table, Long> {
}
