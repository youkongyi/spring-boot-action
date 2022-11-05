package com.learn.tacocloud.data;

import com.learn.tacocloud.Taco;
import org.springframework.data.repository.CrudRepository;

public interface TacoRepository extends CrudRepository<Taco, Long> {

//    Taco save(Taco design);
}
