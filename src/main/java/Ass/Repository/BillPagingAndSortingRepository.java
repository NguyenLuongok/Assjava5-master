package Ass.Repository;

import Ass.Model.Bill;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface BillPagingAndSortingRepository extends PagingAndSortingRepository<Bill,Long> {
}
