package com.learn.tacocloud.data;

import com.learn.tacocloud.Ingredient;
import com.learn.tacocloud.Taco;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.sql.Types;
import java.util.Arrays;
import java.util.Date;

//@Repository
public class JdbcTacoRepository
//        implements TacoRepository
{

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

//    @Override
    public Taco save(Taco taco) {
        long tacoId = this.saveTacoInfo(taco);
        taco.setId(tacoId);
        for (Ingredient ingredient : taco.getIngredients()) {
            this.saveIngredientToTaco(ingredient, tacoId);
        }
        return taco;
    }

    /**
     * 保存Taco信息并返回taco主键
     */
    private long saveTacoInfo(Taco taco) {
        taco.setCreatedAt(new Date());
        PreparedStatementCreatorFactory pf = new PreparedStatementCreatorFactory(
                "insert into Taco (name, createdAt) values (?, ?)",
                Types.VARCHAR, Types.TIMESTAMP
        );
        // 必须设置返回主键,不然GeneratedKeyHolder无法获取主键
        pf.setReturnGeneratedKeys(true);
        PreparedStatementCreator psc = pf.newPreparedStatementCreator(
                Arrays.asList(
                        taco.getName(),
                        new Timestamp(taco.getCreatedAt().getTime())));
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(psc, keyHolder);
        return keyHolder.getKey().longValue();
    }

    /**
     * 保存Ingredient信息
     */
    private void saveIngredientToTaco(Ingredient ingredient, long tacoId) {
        jdbcTemplate.update(
                "insert into Taco_Ingredients (taco, ingredient) values (?, ?)",
                tacoId, ingredient.getId());
    }
}
