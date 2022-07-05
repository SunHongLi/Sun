package com.hl.sun.bean;

import java.util.Objects;

/**
 * https://cloud.tencent.com/developer/article/1680014
 * Function:
 * Date:2022/7/5
 * Author: sunHL
 */
public class EqualsBean {

    private String id;//用户Id
    private String name;//用户名称

    public EqualsBean(String id, String name) {
        this.id = id;
        this.name = name;
    }

    //重写equals方法表示 id 和 name 相同的对象就判定为相同对象
    @Override
    public boolean equals(Object o) {
        //自反性（自己一定等于自己）
        if (this == o) return true;
        //比较是否为同一类型 或者：if (!(o instanceof Person)) return false;
        if (o == null || getClass() != o.getClass()) return false;
        //比较属性值
        EqualsBean user = (EqualsBean) o;
        return Objects.equals(id, user.id) &&
                Objects.equals(name, user.name);
    }

    //重写hashCode详见Objects.hash()方法

    /**
     * 为什么java中在重写equals()方法后必须对hashCode()方法进行重写？
     * * 为了维护hashCode()方法的equals协定，该协定指出：如果根据 equals()方法，两个对象是相等的，
     * * 那么对这两个对象中的每个对象调用 hashCode方法都必须生成相同的整数结果；
     * * 而两个hashCode()返回的结果相等，两个对象的equals()方法不一定相等。
     *
     * @return
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
