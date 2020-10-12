package com.example.paradoxgoverner

const val RECORD_UID = "RECORD_UID"
const val OPERATION_MESSAGE = "OPERATION_MESSAGE"
const val OPERATION_DESCRIPTION = "OPERATION_DESCRIPTION"
const val EXTRA_MESSAGE = "EXTRA_MESSAGE"
val DEFAULT_TYPE_LIST = listOf<String>("收入","支出","转账","借贷")
val DEFAULT_MEMBER_LIST = listOf<String>("我")
val DEFAULT_CATEGORY_LIST = listOf("餐饮","出行","购物")
val DEFAULT_SUBCATEGORY_LIST = listOf(
    listOf("早餐","午餐","晚餐"),
    listOf("打车","地铁","公交车"),
    listOf("衣服","化妆品","电子产品")
)
