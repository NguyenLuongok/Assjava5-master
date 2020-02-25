package Ass.Repository;

import Ass.Model.Products;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ProductPagingAndSortingRepository extends PagingAndSortingRepository<Products,Long> {
}
