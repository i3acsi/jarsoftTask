package ru.gasevsky.jarsoft.repo;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.gasevsky.jarsoft.dto.BannerDto;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Component
@AllArgsConstructor
@Transactional(propagation = Propagation.REQUIRED)
public class BannerDtoRepo {
    @PersistenceContext
    private final EntityManager entityManager;

    @SuppressWarnings("unchecked")
    public List<BannerDto> findAllByDeletedFalse() {
        return (List<BannerDto>) entityManager.createQuery("select b from BannerDto b where b.deleted=false").getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<BannerDto> findByNameContainingIgnoreCaseAndDeletedFalse(String name) {
        return (List<BannerDto>) entityManager.createQuery("select b from BannerDto b where b.deleted=false and lower(b.name) like :fName")
                .setParameter("fName", "%" + name.toLowerCase() + "%")
                .getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<BannerDto> findBannerDtoByCategoryAndDeletedFalse(int id) {
        return (List<BannerDto>) entityManager.createQuery("select b from BannerDto b where b.category = :fCat and b.deleted = false")
                .setParameter("fCat", id)
                .getResultList();
    }

    public BannerDto save(BannerDto bannerDto) {
        entityManager.persist(bannerDto);
        return bannerDto;
    }

    public void update(BannerDto bannerDto) {
        entityManager.merge(bannerDto);
    }

    public BannerDto findById(int id) {
        return (BannerDto) entityManager.createQuery("select b from BannerDto b where b.id = :fId")
                .setParameter("fId", id)
                .getSingleResult();
    }
}
