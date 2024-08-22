package jp.co.example.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jp.co.example.entity.Contact;

@Repository
public interface AdminContactDao extends JpaRepository<Contact, Long> {

	@Query("SELECT c FROM Contact c WHERE (:contactId IS NULL OR c.contactId = :contactId) AND (:status IS NULL OR c.status = :status)")
	List<Contact> findByContactIdAndStatus(@Param("contactId") Long contactId, @Param("status") String status);

	List<Contact> findByContactId(Long contactId);

	List<Contact> findByStatus(String status);

	Contact findContactByContactId(Long contactId);

}
