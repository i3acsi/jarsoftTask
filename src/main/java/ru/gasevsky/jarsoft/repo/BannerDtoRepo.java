package ru.gasevsky.jarsoft.repo;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.gasevsky.jarsoft.dto.BannerDto;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Component
@AllArgsConstructor
public class BannerDtoRepo {
    @PersistenceContext
    private final EntityManager entityManager;

    @Transactional
    public List<BannerDto> findAllByDeletedFalse() {
        return (List<BannerDto>) entityManager.createQuery("select b from BannerDto b where b.deleted=false").getResultList();
    }

    @Transactional
    public List<BannerDto> findByNameContainingIgnoreCaseAndDeletedFalse(String name) {
        return (List<BannerDto>) entityManager.createQuery("select b from BannerDto b where b.deleted=false and lower(b.name) like :fName")
                .setParameter("fName", "%" + name.toLowerCase() + "%")
                .getResultList();
    }

    @Transactional
    public List<BannerDto> findBannerDtoByCategoryAndDeletedFalse(int id) {
        return (List<BannerDto>) entityManager.createQuery("select b from BannerDto b where b.category = :fCat and b.deleted = false")
                .setParameter("fCat", id)
                .getResultList();
    }

    @Transactional
    public BannerDto save(BannerDto bannerDto) {
        entityManager.persist(bannerDto);
        return bannerDto;
    }

    @Transactional
    public void update(BannerDto bannerDto) {
        entityManager.merge(bannerDto);
    }

    @Transactional
    public BannerDto findById(int id) {
        return (BannerDto) entityManager.createQuery("select b from BannerDto b where b.id = :fId")
                .setParameter("fId", id)
                .getSingleResult();
    }
}
