package com.hl.sun.bean

/**
 * Function:测试groupBy API
 * Date:2022/6/6
 * Author: sunHL
 */
class GroupByBean {
    var id: String? = null
    var title: String? = null

    constructor(id: String?, title: String?) {
        this.id = id
        this.title = title
    }

    override fun toString(): String {
        return "GroupByBean(title=$title)"
    }
}