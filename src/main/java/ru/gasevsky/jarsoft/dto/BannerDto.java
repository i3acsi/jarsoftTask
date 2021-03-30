package ru.gasevsky.jarsoft.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "banner")
@NoArgsConstructor
public class BannerDto {
    @Id
    private Integer id;
    private String name;
    private Float price;
    @Column(name = "category_id")
    private Integer category;
    private String content;
    private Boolean deleted;
}
