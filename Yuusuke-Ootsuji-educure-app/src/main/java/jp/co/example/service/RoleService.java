package jp.co.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.example.dao.RoleDao;
import jp.co.example.entity.Role;

@Service
public class RoleService {

    @Autowired
    private RoleDao roleDao;

    public Role getRoleById(int roleId) {
        return roleDao.findById(roleId).orElse(null);
    }
}
