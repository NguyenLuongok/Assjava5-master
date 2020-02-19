package Ass.Repository;

import Ass.Model.Users;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
public class UserRepositoryImpl implements UserRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Users> findAll() {
        String query = "SELECT u FROM Users u";
        TypedQuery<Users> usersTypedQuery = entityManager.createQuery(query,Users.class);
        return usersTypedQuery.getResultList();
    }

    @Override
    public Users findById(Long id) {
        return entityManager.find(Users.class,id);
    }


    @Override
    public void save(Users model) {
        if(model != null){
            entityManager.merge(model);
        }else{
            entityManager.persist(model);
        }

    }

    @Override
    public void remove(Long id) {
        entityManager.remove(findById(id));
    }

    @Override
    public void update(Long id, Users model) {

    }

    @Override
    public Users findByUsername(String username) {
        TypedQuery<Users> usersTypedQuery = entityManager.createQuery("select u from Users u where u.taiKhoan =: name", Users.class);
        usersTypedQuery.setParameter("name", username);
        return usersTypedQuery.getSingleResult();
    }
}
