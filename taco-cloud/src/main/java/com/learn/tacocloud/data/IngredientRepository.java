package com.learn.tacocloud.data;

import com.learn.tacocloud.Ingredient;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IngredientRepository extends CrudRepository<Ingredient, String> {

//    Iterable<Ingredient> findAll();
//
//    Ingredient findOne(String id);
//
//    Ingredient save(Ingredient ingredient);
}
