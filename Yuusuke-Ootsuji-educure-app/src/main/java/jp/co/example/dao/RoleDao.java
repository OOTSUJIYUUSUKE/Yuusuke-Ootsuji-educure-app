package jp.co.example.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jp.co.example.entity.Role;

@Repository
public interface RoleDao extends JpaRepository<Role, Integer> {
}

