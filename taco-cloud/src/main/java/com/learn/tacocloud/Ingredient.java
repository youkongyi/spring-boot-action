package com.learn.tacocloud;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

@Data
@RequiredArgsConstructor // 生成带有必需参数(final)的构造函数
@NoArgsConstructor(access=AccessLevel.PUBLIC, force=true)// 设置一个私有的无参构造函数,并且初始化final字段
@Entity
public class Ingredient {
    @Id
    private final String id;

    private final String name;

    @Enumerated(EnumType.STRING)
    private final Type type;

    public static enum Type {
        WRAP, PROTEIN, VEGGIES, CHEESE , SAUCE
    }
}
