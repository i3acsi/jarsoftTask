package ru.gasevsky.jarsoft.service;

import lombok.AllArgsConstructor;
import org.hibernate.engine.jdbc.spi.SqlExceptionHelper;
import org.hibernate.event.spi.SaveOrUpdateEvent;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;
import ru.gasevsky.jarsoft.dto.BannerDto;
import ru.gasevsky.jarsoft.model.Category;
import ru.gasevsky.jarsoft.repo.BannerRepo;
import ru.gasevsky.jarsoft.repo.CategoryRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@AllArgsConstructor
@Service
public class ApplicationService {
    private final BannerRepo bannerRepo;
    private final CategoryRepo categoryRepo;

    public List<BannerDto> findAllActiveBanners() {
        return StreamSupport.stream(
                this.bannerRepo.findAllByDeletedFalse().spliterator(), false
        ).collect(Collectors.toList());
    }

    public List<BannerDto> findBannersByNameContains(String partOfName) {
        return bannerRepo.findByNameContainingIgnoreCaseAndDeletedFalse(partOfName);
    }

    public ResponseEntity<BannerDto> create(BannerDto bannerDto) {
        bannerDto.setId(null);
        bannerDto.setDeleted(false);
        return new ResponseEntity<BannerDto>(
                this.bannerRepo.save(bannerDto),
                HttpStatus.CREATED
        );
    }

    public ResponseEntity<Void> update(BannerDto bannerDto) {
        bannerDto.setDeleted(false);
        this.bannerRepo.save(bannerDto);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<Void> deleteBanner(int id) {
        Optional<BannerDto> bannerOpt = this.bannerRepo.findById(id);
        if (bannerOpt.isPresent()) {
            BannerDto bannerDto = bannerOpt.get();
            bannerDto.setDeleted(true);
            this.bannerRepo.save(bannerDto);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public List<Category> findAllActiveCategories() {
        return StreamSupport.stream(
                this.categoryRepo.findAllByDeletedFalse().spliterator(), false
        ).collect(Collectors.toList());
    }

    public List<Category> findCategoriesByNameContains(String partOfName) {
        return categoryRepo.findByNameContainingIgnoreCaseAndDeletedFalse(partOfName);
    }

    public ResponseEntity<Category> create(Category category) {
        category.setId(null);
        category.setDeleted(false);
            return new ResponseEntity<Category>(
                    this.categoryRepo.save(category),
                    HttpStatus.CREATED
            );
    }

    public ResponseEntity<Void> update(Category category) {
        category.setDeleted(false);
        this.categoryRepo.save(category);
        return ResponseEntity.ok().build();
    }

    public List<Integer> deleteCategory(int id) {
        //Категорию нельзя удалить если с ней связаны не удалённые баннеры. При удалении категории, используемой в каких-либо баннерах, должно показываться сообщение со списком идентификаторов баннеров, в которых используется категория.
        List<BannerDto> banners = bannerRepo.findBannerDtoByCategoryAndDeletedFalse(id);
        if (banners.size()>0){
            return banners.stream().map(BannerDto::getId).collect(Collectors.toList());
        } else {
            Optional<Category> optCategory = categoryRepo.findById(id);
            if (optCategory.isPresent()){
                Category category = optCategory.get();
                category.setDeleted(true);
                categoryRepo.save(category);
            }
            return new ArrayList<>();
        }
    }

}
