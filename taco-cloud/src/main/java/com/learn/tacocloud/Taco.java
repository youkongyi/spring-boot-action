package com.learn.tacocloud;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.PrePersist;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Entity
public class Taco implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // 数据库自动生成 id 值
    private Long id;

    private Date createdAt;

    @NotNull
    @Size(min=5, message="Name must be at least 5 characters long")
    private String name;

    @Size(min=1, message="You must choose at least 1 ingredient")
    @ManyToMany(targetEntity=Ingredient.class) // 声明多对多表关系
    private List<Ingredient> ingredients;

    @PrePersist //持久化对象前执行填入实体类创建时间、更新时间
    void createdAt() {
        this.createdAt = new Date();
    }
}