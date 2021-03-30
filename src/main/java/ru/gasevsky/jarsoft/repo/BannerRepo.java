package ru.gasevsky.jarsoft.repo;

import org.springframework.data.repository.CrudRepository;
import ru.gasevsky.jarsoft.dto.BannerDto;
import ru.gasevsky.jarsoft.model.Banner;

import java.util.List;

public interface BannerRepo extends CrudRepository<BannerDto, Integer> {
    public List<BannerDto> findAllByDeletedFalse();
}
