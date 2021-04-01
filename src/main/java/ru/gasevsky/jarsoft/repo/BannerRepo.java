package ru.gasevsky.jarsoft.repo;

import org.springframework.data.repository.CrudRepository;
import ru.gasevsky.jarsoft.dto.BannerDto;

import java.util.List;

public interface BannerRepo extends CrudRepository<BannerDto, Integer> {
    public List<BannerDto> findAllByDeletedFalse();

    public List<BannerDto> findByNameContainingIgnoreCaseAndDeletedFalse(String name);

    List<BannerDto> findBannerDtoByCategoryAndDeletedFalse(int id );
}
