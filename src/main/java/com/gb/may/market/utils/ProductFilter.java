package com.gb.may.market.utils;

import com.gb.may.market.entities.Category;
import com.gb.may.market.entities.Product;
import com.gb.may.market.repositories.specifications.ProductSpecifications;
import lombok.Getter;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Map;

@Getter
public class ProductFilter {
    private Specification<Product> spec;
    private StringBuilder filterDefinition;

    public ProductFilter(Map<String, String> map, List<Category> categories) {
        this.spec = Specification.where(null);
        this.filterDefinition = new StringBuilder();
        if (map.containsKey("min_price") && !map.get("min_price").isEmpty()) {
            int minPrice = Integer.parseInt(map.get("min_price"));
            spec = spec.and(ProductSpecifications.priceGreaterOrEqualsThan(minPrice));
            filterDefinition.append("&min_price=").append(minPrice);
        }
        if (map.containsKey("max_price") && !map.get("max_price").isEmpty()) {
            int maxPrice = Integer.parseInt(map.get("max_price"));
            spec = spec.and(ProductSpecifications.priceLesserOrEqualsThan(maxPrice));
            filterDefinition.append("&max_price=").append(maxPrice);
        }
        if (map.containsKey("title") && !map.get("title").isEmpty()) {
            String title = map.get("title");
            spec = spec.and(ProductSpecifications.titleLike(title));
            filterDefinition.append("&title=").append(title);
        }
        if (categories != null && !categories.isEmpty()){
            Specification specCategories = null;
            for (Category c : categories){
                if (specCategories == null){
                    specCategories = ProductSpecifications.categoryIs(c);
                } else {
                    specCategories = specCategories.or(ProductSpecifications.categoryIs(c));
                }
            }
            spec = spec.and(specCategories);
        }
    }
}
