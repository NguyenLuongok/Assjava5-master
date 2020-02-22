package Ass.Repository;

import Ass.Model.Bill;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
public class BillRepositoryImpl implements BillRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Bill> findAll() {
        String query = "SELECT p FROM Bill p";
        TypedQuery<Bill> billTypedQuery =  entityManager.createQuery(query,Bill.class);
        return billTypedQuery.getResultList();
    }

    @Override
    public Bill findById(Long id) {
        return entityManager.find(Bill.class,id);
    }


    @Override
    public void save(Bill model) {
        if (model.getId() != null) {
            //update
            entityManager.merge(model);
        }else {
            //add new
            entityManager.persist(model);
        }
    }

    @Override
    public void remove(Long id) {
        entityManager.remove(entityManager.find(Bill.class,id));
    }

    @Override
    public void update(Long id, Bill model) {

    }

    @Override
    public List<Bill> saveAll(List<Bill> bill) {
        for (Bill bills : bill){
            entityManager.persist(bills);
        }
        return null;
    }
}
