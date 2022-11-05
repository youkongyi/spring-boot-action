package com.learn.tacocloud.data;

import com.learn.tacocloud.Order;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order, Long> {

//    Order save(Order order);
}
