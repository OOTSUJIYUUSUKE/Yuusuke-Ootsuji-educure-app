package jp.co.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.example.entity.Contact;
import jp.co.example.dao.ContactDao;

@Service
public class ContactService {

    @Autowired
    private ContactDao contactDao;

    public void saveContact(Contact contact) {
        contactDao.save(contact);
    }
}
