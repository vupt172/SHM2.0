package com.vupt.SHM.services;

import com.vupt.SHM.dto.CategoryDto;
import com.vupt.SHM.entity.Category;
import com.vupt.SHM.exceptions.AppException;
import com.vupt.SHM.exceptions.SQLException;
import com.vupt.SHM.mapstruct.mapper.MapstructMapper;
import com.vupt.SHM.repositories.CategoryRepository;
import com.vupt.SHM.specifications.CategorySpecification;
import com.vupt.SHM.utils.DisplayTextUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;


@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepo;
    @Autowired
    private MapstructMapper mapstructMapper;

    public List<Category> findAll() {
        return this.categoryRepo.findAll();
    }

    public List<CategoryDto> findAllDto() {
        return this.mapstructMapper.categoríesToCategoryDtos(this.categoryRepo.findAll());
    }


    public List<CategoryDto> findAllDtoIgnoreSuspended() {
        List<CategoryDto> categoryDtoIgnoreSuspendedList = (List<CategoryDto>) findAllDto().stream().filter(categoryDto -> !categoryDto.isSuspended()).collect(Collectors.toList());
        return categoryDtoIgnoreSuspendedList;
    }

    public List<CategoryDto> search(String name) {
        return this.mapstructMapper.categoríesToCategoryDtos(this.categoryRepo.findAll(CategorySpecification.filterSearch(name)));
    }

    public Optional<Category> findCategoryByMatchName(String categoryName) {
        return this.categoryRepo.findAll().stream()
                .filter(e -> categoryName.toLowerCase().contains(e.getName().toLowerCase()))
                .findFirst();
    }

    public Category findById(long id) {
        return (Category) this.categoryRepo.findById(Long.valueOf(id))
                .orElseThrow(() -> new AppException(DisplayTextUtils.getNotFoundMessage("Danh mục", "id", Long.valueOf(id))));
    }

    public CategoryDto getDTO(long id) {
        Category c = findById(id);
        return this.mapstructMapper.categoryToCategoryDto(c);
    }

    public void checkIfExistsByCode(String code) {
        if (this.categoryRepo.existsByCode(code)) {
            throw new AppException("Danh mục đã tồn tại với code là " + code);
        }
    }

    public void save(CategoryDto categoryDTO) {
        if (categoryDTO.getId() == 0L) {
            checkIfExistsByCode(categoryDTO.getCode());
            Category category = this.mapstructMapper.categoryDtoToCategory(categoryDTO);
            this.categoryRepo.save(category);
        } else {
            Category curCategory = this.categoryRepo.findById(Long.valueOf(categoryDTO.getId())).get();
            if (!curCategory.getCode().equals(categoryDTO.getCode())) {
                checkIfExistsByCode(categoryDTO.getCode());
            }
            this.mapstructMapper.categoryDtoToSelectedCategory(categoryDTO, curCategory);
            this.categoryRepo.save(curCategory);
        }
    }

    public void delete(long id) {
        try {
            Category category = findById(id);
            this.categoryRepo.delete(category);
        } catch (DataIntegrityViolationException e) {
            throw new SQLException("Không thể xóa do có dữ liệu liên kết", e.getCause());
        }
    }
}
