package ru.gasevsky.jarsoft.repo;

import org.springframework.data.repository.CrudRepository;
import ru.gasevsky.jarsoft.model.Banner;

import java.util.List;

public interface BannerRepo extends CrudRepository<Banner, Integer> {
//@Query("select distinct b from Banner b left join fetch b.category where b.category.reqName = :reqName")
    List<Banner> findByCategory_ReqNameOrderByPriceDesc(String reqName);
}
