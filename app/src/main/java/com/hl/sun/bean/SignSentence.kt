package com.hl.sun.bean

/**
 * Function:句子得分情况
 * Date:2023/3/3
 * Author: sunHL
 */
class SignSentence {
    var content: String? = null

    var judgeStatus: Int? = null

    //是否有得分
    var hasScore: Boolean = false
        get() {
            return judgeStatus == 1 && (score ?: 0F) > 0
        }

    var children: List<SignSentence?>? = null

    //得分
    var score: Float? = null

}