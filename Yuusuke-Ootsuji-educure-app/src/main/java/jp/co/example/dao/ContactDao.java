package jp.co.example.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import jp.co.example.entity.Contact;

public interface ContactDao extends JpaRepository<Contact, Long> {
}
