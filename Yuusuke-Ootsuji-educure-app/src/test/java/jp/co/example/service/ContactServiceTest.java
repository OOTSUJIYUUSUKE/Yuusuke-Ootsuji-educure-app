package jp.co.example.service;

import jp.co.example.dao.ContactDao;
import jp.co.example.entity.Contact;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
public class ContactServiceTest {

    @Mock
    private ContactDao contactDao;

    @InjectMocks
    private ContactService contactService;

    private Contact contact;

    @BeforeEach
    public void setUp() {
        contact = new Contact();
        contact.setContactId(1L);
        contact.setUserName("田中太郎");
        contact.setEmail("tanaka_12345@example.com");
        contact.setSubject("サービスに関する問い合わせ");
        contact.setMessage("サービスについて質問があります。詳細を教えてください。");
    }

    @Test
    public void saveContact_正常にコンタクトを保存できる() {
        doReturn(contact).when(contactDao).save(contact);

        contactService.saveContact(contact);

        verify(contactDao, times(1)).save(contact);
    }
}
