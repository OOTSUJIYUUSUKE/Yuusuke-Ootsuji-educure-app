package jp.co.example.dao;

import jp.co.example.entity.Contact;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
@DBRider
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AdminContactDaoTest {

    @Autowired
    private AdminContactDao adminContactDao;

    @Test
    @DataSet("datasets/contact.yml")
    @Transactional
    public void findByContactIdAndStatus_特定のIDとステータスで正常に検索できる() {
        List<Contact> contacts = adminContactDao.findByContactIdAndStatus(1L, "対応中");
        assertThat(contacts).isNotEmpty();
        assertThat(contacts.get(0).getUserName()).isEqualTo("田中太郎");
        assertThat(contacts.get(0).getStatus()).isEqualTo("対応中");
    }

    @Test
    @DataSet("datasets/contact.yml")
    @Transactional
    public void findByContactIdAndStatus_存在しないIDとステータスで結果が空であること() {
        List<Contact> contacts = adminContactDao.findByContactIdAndStatus(999L, "存在しないステータス");
        assertThat(contacts).isEmpty();
    }

    @Test
    @DataSet("datasets/contact.yml")
    @Transactional
    public void findByContactId_特定のIDで正常に検索できる() {
        List<Contact> contacts = adminContactDao.findByContactId(2L);
        assertThat(contacts).isNotEmpty();
        assertThat(contacts.get(0).getUserName()).isEqualTo("佐藤二郎");
    }

    @Test
    @DataSet("datasets/contact.yml")
    @Transactional
    public void findByStatus_特定のステータスで正常に検索できる() {
        List<Contact> contacts = adminContactDao.findByStatus("対応中");
        assertThat(contacts).isNotEmpty();
        assertThat(contacts).hasSize(2); // "対応中"が2件あることを確認
    }

    @Test
    @DataSet("datasets/contact.yml")
    @Transactional
    public void findContactByContactId_特定のIDで1件のコンタクトが取得できる() {
        Contact contact = adminContactDao.findContactByContactId(3L);
        assertThat(contact).isNotNull();
        assertThat(contact.getUserName()).isEqualTo("山田一郎");
    }
}
