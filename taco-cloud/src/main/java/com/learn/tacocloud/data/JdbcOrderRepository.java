package com.learn.tacocloud.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learn.tacocloud.Order;
import com.learn.tacocloud.Taco;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@Repository
public class JdbcOrderRepository
//        implements OrderRepository
{

    private final SimpleJdbcInsert orderInserter;

    private final SimpleJdbcInsert orderTacoInserter;

    private final ObjectMapper objectMapper;

    /**
     * 第一个实例被分配给 orderInserter 实例变量，它被配置为使用 Taco_Order 表，并假定 id 属性将由数据库提供或生成
     * 分配给 orderTacoInserter 的第二个实例被配置为使用 Taco_Order_Tacos 表，但是没有声明如何在该表中生成任何 id
     * 构造函数还创建 ObjectMapper 实例，并将其分配给实例变量。
     */
    @Autowired
    public JdbcOrderRepository(JdbcTemplate jdbc, ObjectMapper objectMapper) {
        this.orderInserter = new SimpleJdbcInsert(jdbc)
                .withTableName("Taco_Order")
                .usingGeneratedKeyColumns("id");
        this.orderTacoInserter = new SimpleJdbcInsert(jdbc)
                .withTableName("Taco_Order_Tacos");
        this.objectMapper = objectMapper;
    }

//    @Override
    public Order save(Order order) {
        order.setPlacedAt(new Date());
        long orderId = this.saveOrderDetails(order);
        order.setId(orderId);

        List<Taco> tacos = order.getTacos();
        for (Taco taco : tacos) {
            this.saveTacoToOrder(taco, orderId);
        }

        return order;
    }

    /**
     * 通过将 Order 中的值复制到 Map 的条目中，很容易创建这样的 Map。
     * 但是 Order 有几个属性，这些属性和它们要进入的列有相同的名字。
     * 因此，在 saveOrderDetails() 中，我决定使用 Jackson 的 ObjectMapper 及其 convertValue() 方法
     * 将 Order 转换为 Map。这是必要的，否则 ObjectMapper 会将 Date 属性转换为 long，这与 Taco_Order
     * 表中的 placedAt 字段不兼容
     */
    private long saveOrderDetails(Order order) {
        Map<String, Object> values = objectMapper.convertValue(order, Map.class);
        values.put("placedAt", order.getPlacedAt());
        values.put("deliveryName", order.getName());
        values.put("deliveryStreet", order.getStreet());
        values.put("deliveryCity", order.getCity());
        values.put("deliveryState", order.getState());
        values.put("deliveryZip", order.getZip());
        long orderId = orderInserter.executeAndReturnKey(values).longValue();
        return orderId;
    }

    private void saveTacoToOrder(Taco taco, long orderId) {
        Map<String, Object> values = new HashMap<>();
        values.put("tacoOrder", orderId);
        values.put("taco", taco.getId());

        orderTacoInserter.execute(values);
    }
}
