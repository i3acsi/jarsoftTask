package ru.gasevsky.jarsoft.service;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.gasevsky.jarsoft.dto.BannerDto;
import ru.gasevsky.jarsoft.model.Category;
import ru.gasevsky.jarsoft.repo.BannerDtoRepo;
import ru.gasevsky.jarsoft.repo.CategoryRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@AllArgsConstructor
@Service
public class ApplicationService {
    private final BannerDtoRepo bannerDtoRepo;
    private final CategoryRepo categoryRepo;

    public List<BannerDto> findAllActiveBanners() {
        return bannerDtoRepo.findAllByDeletedFalse();
    }

    public List<BannerDto> findBannersByNameContains(String partOfName) {
        return bannerDtoRepo.findByNameContainingIgnoreCaseAndDeletedFalse(partOfName);
    }

    public ResponseEntity<BannerDto> create(BannerDto bannerDto) {
        bannerDto.setId(null);
        bannerDto.setDeleted(false);
        return new ResponseEntity<BannerDto>(
                this.bannerDtoRepo.save(bannerDto),
                HttpStatus.CREATED
        );
    }

    public ResponseEntity<Void> update(BannerDto bannerDto) {
        bannerDto.setDeleted(false);
        this.bannerDtoRepo.update(bannerDto);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<Void> deleteBanner(int id) {
        BannerDto bannerDto = this.bannerDtoRepo.findById(id);
        if (bannerDto!=null) {
            bannerDto.setDeleted(true);
            this.bannerDtoRepo.save(bannerDto);
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
        List<BannerDto> banners = bannerDtoRepo.findBannerDtoByCategoryAndDeletedFalse(id);
        if (banners.size() > 0) {
            return banners.stream().map(BannerDto::getId).collect(Collectors.toList());
        } else {
            Optional<Category> optCategory = categoryRepo.findById(id);
            if (optCategory.isPresent()) {
                Category category = optCategory.get();
                category.setDeleted(true);
                categoryRepo.save(category);
            }
            return new ArrayList<>();
        }
    }

}
