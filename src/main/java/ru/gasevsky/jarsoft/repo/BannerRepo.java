package ru.gasevsky.jarsoft.repo;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.gasevsky.jarsoft.model.Banner;

import java.util.List;

public interface BannerRepo extends CrudRepository<Banner, Integer> {
    @Query("select distinct b from Banner b join fetch b.category where b.category.reqName = :reqName and b.deleted=false order by b.price desc")
    List<Banner> findActiveBannersForReqName(String reqName);
}
