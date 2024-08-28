package jp.co.example.service;

import jp.co.example.dao.RoleDao;
import jp.co.example.entity.Role;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class RoleServiceTest {

    @Mock
    private RoleDao roleDao;

    @InjectMocks
    private RoleService roleService;

    private Role role1;
    private Role role2;

    @BeforeEach
    public void setUp() {
        role1 = new Role();
        role1.setRoleId(1);
        role1.setRoleName("管理者");
        role2 = new Role();
        role2.setRoleId(2);
        role2.setRoleName("一般");
    }

    @Test
    public void getRoleById_正常に役割を取得できる() {
        doReturn(Optional.of(role1)).when(roleDao).findById(1);

        Role result = roleService.getRoleById(1);

        assertEquals(role1, result);
        verify(roleDao, times(1)).findById(1);
    }

    @Test
    public void getRoleById_存在しない役割IDの場合はnullを返す() {
        doReturn(Optional.empty()).when(roleDao).findById(3);

        Role result = roleService.getRoleById(3);

        assertNull(result);
        verify(roleDao, times(1)).findById(3);
    }
}
