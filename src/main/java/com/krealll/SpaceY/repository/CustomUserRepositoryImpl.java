package com.krealll.SpaceY.repository;

import com.krealll.SpaceY.model.User;
import com.krealll.SpaceY.model.type.UserStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class CustomUserRepositoryImpl implements CustomUserRepository{

    private final EntityManager entityManager;

    @Autowired
    public CustomUserRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<User> findAll(Integer limit){
        List<User> userList;
        TypedQuery<User> query = entityManager.createQuery("SELECT u " +
                "FROM User AS u"
                ,User.class);
        query.setMaxResults(limit);
        userList = query.getResultList();
        return userList;
    }

    public List<User> findAll(Integer limit, Integer offset){
        List<User> userList;
        TypedQuery<User> query = entityManager.createQuery("SELECT u " +
                "FROM User AS u"
                ,User.class);
        query.setMaxResults(limit);
        query.setFirstResult(offset);
        userList = query.getResultList();
        return userList;
    }

    public List<User> findAllByStatus(UserStatus userStatus, Integer limit){
        List<User> userList;
        TypedQuery<User> query = entityManager.createQuery
                ("SELECT u" +
                        " FROM User AS u " +
                        "WHERE u.status is '" + userStatus.name()+"'"
                        ,User.class);
        query.setMaxResults(limit);
        userList = query.getResultList();
        return userList;
    }

    public List<User> findAllByStatus(UserStatus userStatus, Integer limit, Integer offset ){
        List<User> userList;
        TypedQuery<User> query = entityManager.createQuery
                ("SELECT u " +
                "FROM User AS u " +
                "WHERE u.status is '" + userStatus.name() +"'"
                ,User.class);
        query.setMaxResults(limit);
        query.setFirstResult(offset);
        userList = query.getResultList();
        return userList;
    }

}
