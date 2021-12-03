package com.wwyl.service.security;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wwyl.dao.security.OperatorDao;
import com.wwyl.dao.security.PermissionDao;
import com.wwyl.dao.security.RoleDao;
import com.wwyl.entity.security.Operator;
import com.wwyl.entity.security.Permission;
import com.wwyl.entity.security.Role;
import com.wwyl.util.MD5Encryption;

/**
 * @author fyunli
 */
@Service
@Transactional(readOnly = true)
public class SecurityService {

    @Resource
    private PermissionDao permissionDao;
    @Resource
    private RoleDao roleDao;
    @Resource
    private OperatorDao operatorDao;

    // --- 权限 --- //
    // @Cacheable(value = "com.wwyl.entity.security.Permission")
    public List<Permission> findRootPermissions() {
        List<Permission> permissions = permissionDao.findAll();
        List<Permission> rootPermissions = new ArrayList<Permission>();
        for (Permission permission : permissions) {
            if (permission.getParent() == null) {
                // 过滤因left join造成的重复
                if (!rootPermissions.contains(permission)) {
                    rootPermissions.add(permission);
                }
            }
        }
        return rootPermissions;
    }

    // --- 角色 --- //
    //@Cacheable(value = "com.wwyl.entity.security.Role")
    public List<Role> findAllRoles() {
        return roleDao.findAll();
    }

    //@Cacheable(value = "com.wwyl.entity.security.Role")
    public Role findRoleById(Long id) {
        return roleDao.findOne(id);
    }

    @Transactional(readOnly = false)
    //@CacheEvict(value = "com.wwyl.entity.security.Role", allEntries = true)
    public void saveRole(Role role) {
        roleDao.save(role);
    }

    @Transactional(readOnly = false)
    //@CacheEvict(value = "com.wwyl.entity.security.Role", allEntries = true)
    public void deleteRole(Long id) {
        roleDao.delete(id);
    }

    //@Cacheable(value = "com.wwyl.entity.security.Role")
    public Role findRoleByCode(final String code) {
        return roleDao.findOne(new Specification<Role>() {
            @Override
            public Predicate toPredicate(Root<Role> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get("code").as(String.class), code);
            }
        });
    }

    // --- 操作员 --- //
    //@Cacheable(value = "com.wwyl.entity.security.Operator")
    public List<Operator> findAllOperators() {
        return operatorDao.findAll();
    }

    //@Cacheable(value = "com.wwyl.entity.security.Operator")
    public List<Operator> findOperatorByUsernameLike(String username) {
        System.out.println("%" + username + "%");
        return operatorDao.findByUsernameLike("%" + username + "%");
    }

    //@Cacheable(value = "com.wwyl.entity.security.Operator")
    public Operator findOperatorById(Long id) {
        return operatorDao.findOne(id);
    }

    //@Cacheable(value = "com.wwyl.entity.security.Operator")
    public Operator findOperatorByUsername(final String username) {
        Operator operator = operatorDao.findOne(new Specification<Operator>() {
            @Override
            public Predicate toPredicate(Root<Operator> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get("username").as(String.class), username);
            }
        });
        try {
            new ObjectMapper().writeValueAsString(operator);
        } catch (JsonProcessingException e) {

        }// 强制加载
        return operator;
    }

    //@Cacheable(value = "com.wwyl.entity.security.Operator")
    public boolean isUsernameExists(String username) {
        return this.findOperatorByUsername(username) != null;
    }


    @Transactional(readOnly = false)
    //@CacheEvict(value = "com.wwyl.entity.security.Operator", allEntries = true)
    public void saveOperator(Operator operator) {
        operatorDao.save(operator);
    }

    @Transactional(readOnly = false)
    //@CacheEvict(value = "com.wwyl.entity.security.Operator", allEntries = true)
    public void deleteOperator(Long id) {
        operatorDao.delete(id);
    }

    /**
     * 查找授予某个角色的用户集合
     */
    //@Cacheable(value = "com.wwyl.entity.security.Operator")
    public List<Operator> findOperatorsByRole(String roleCode) {
        return operatorDao.findByRole(roleCode);
    }

    /**
     * 查找授予某个权限的用户集合
     */
    //@Cacheable(value = "com.wwyl.entity.security.Operator")
    public List<Operator> findOperatorsByPermission(String permCode) {
        return operatorDao.findByPermission(permCode);
    }

    @Transactional(readOnly = false)
    //@Cacheable(value = "com.wwyl.entity.security.Operator", key = "#username + #password + '.login'")
    public Operator login(String username, String password, String ipAddr) {
        Operator operator = this.findOperatorByUsername(username);
        if (operator == null) {
            throw new SecurityException("该用户不可用！");
        }

        if (!operator.validatePassword(password)) {
            // TODO: TJ 处理密码锁定
            throw new SecurityException("用户密码不正确！");
        }

        // TODO: TJ 验证操作员锁定状态
        Date curTime = new Date();
        operator.setErrorCount(0);
        operator.setLastLoginTime(curTime);
        operator.setLastLoginIpAddr(ipAddr);
        operatorDao.save(operator);

        // 强制加载
        for (Role role : operator.getRoles()) {
            Hibernate.initialize(role);
            for (Permission permission : role.getPermissions()) {
                Hibernate.initialize(permission);
            }
        }
        for (Permission permission : operator.getPermissions()) {
            Hibernate.initialize(permission);
        }

        return operator;
    }

    // --- 客户端登陆验证 --- //
    @Resource
    private CacheManager cacheManager;

    private static final String FROG_SECRET_KEY_PREFIX = "frog.secretKey.";
    private static final String SECRET_KEY_OPERATOR_PREFIX = "secretKey.operator.";
    private static final String AUTH_TOKEN_SECRET_KEY_PREFIX = "auth_token.secretKey.";

    public void cacheFrog(String frog, String secretKey, Operator operator) {
        Cache instantCache = cacheManager.getCache("instantCache");
        instantCache.put(FROG_SECRET_KEY_PREFIX + frog, secretKey);
        instantCache.put(SECRET_KEY_OPERATOR_PREFIX + secretKey, operator);
    }

    public boolean validateFrog(String frog, String signature) {
        if (StringUtils.isBlank(frog) || StringUtils.isBlank(signature)) {
            return false;
        }

        Cache instantCache = cacheManager.getCache("instantCache");
        if (instantCache.get(FROG_SECRET_KEY_PREFIX + frog, String.class) == null) {
            return false;
        }

        String secretKey = this.getSecretKeyByFrog(frog);
        String hash = MD5Encryption.encode(secretKey + "frog" + frog);
        return StringUtils.equals(signature, hash);
    }

    public String getSecretKeyByFrog(String frog) {
        Cache instantCache = cacheManager.getCache("instantCache");
        return instantCache.get(FROG_SECRET_KEY_PREFIX + frog, String.class);
    }

    public Operator cacheAuthToken(String frog, String authToken) {
        Cache instantCache = cacheManager.getCache("instantCache");
        String secretKey = instantCache.get(FROG_SECRET_KEY_PREFIX + frog, String.class);
        Operator operator = instantCache.get(SECRET_KEY_OPERATOR_PREFIX + secretKey, Operator.class);

        Cache dailyCache = cacheManager.getCache("dailyCache");
        dailyCache.put(AUTH_TOKEN_SECRET_KEY_PREFIX + authToken, secretKey);
        dailyCache.put(SECRET_KEY_OPERATOR_PREFIX + secretKey, operator);
        return operator;
    }

    public boolean validateAuthToken(String authToken) {
        Cache dailyCache = cacheManager.getCache("dailyCache");
        return dailyCache.get(AUTH_TOKEN_SECRET_KEY_PREFIX + authToken, String.class) != null;
    }

    public String getSecketKeyByAuthToken(String authToken) {
        Cache dailyCache = cacheManager.getCache("dailyCache");
        return dailyCache.get(AUTH_TOKEN_SECRET_KEY_PREFIX + authToken, String.class);
    }

    public Operator lengthenAuthToken(String authToken) {
        Cache dailyCache = cacheManager.getCache("dailyCache");
        String secretKey = dailyCache.get(AUTH_TOKEN_SECRET_KEY_PREFIX + authToken, String.class);
        Operator operator = dailyCache.get(SECRET_KEY_OPERATOR_PREFIX + secretKey, Operator.class);
        dailyCache.put(AUTH_TOKEN_SECRET_KEY_PREFIX + authToken, secretKey);
        dailyCache.put(SECRET_KEY_OPERATOR_PREFIX + secretKey, operator);
        return operator;
    }

}
