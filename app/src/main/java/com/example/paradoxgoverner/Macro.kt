package com.example.paradoxgoverner

const val RECORD_UID = "RECORD_UID"
const val OPERATION_MESSAGE = "OPERATION_MESSAGE"
const val OPERATION_DESCRIPTION = "OPERATION_DESCRIPTION"
const val EXTRA_MESSAGE = "EXTRA_MESSAGE"

const val VOID_ITEM = "其他"

val DEFAULT_TYPE_LIST = listOf<String>("收入","支出","转账","借贷")
val DEFAULT_MEMBER_LIST = listOf<String>("我",VOID_ITEM)
val DEFAULT_CATEGORY_LIST = listOf("餐饮","出行","购物",VOID_ITEM)
val DEFAULT_SUBCATEGORY_LIST = listOf(
    listOf("早餐","午餐","晚餐",VOID_ITEM),
    listOf("打车","地铁","公交车",VOID_ITEM),
    listOf("衣服","化妆品","电子产品",VOID_ITEM),
    listOf(VOID_ITEM)
)
val DEFAULT_MERCHANT_LIST = listOf<String>("Steam","COCO","京东",VOID_ITEM)
val DEFAULT_ITEM_LIST = listOf<String>(VOID_ITEM)
const val ALL_ACCOUNT = "全部账户"
val DEFAULT_ACCOUNT_LIST = listOf<String>("账户1",VOID_ITEM)

const val MEMBER_INDEX = 0
const val CATEGORY_INDEX = 1
const val SUBCATEGORY_INDEX = 2
const val TYPE_INDEX = 3
const val MERCHANT_INDEX = 4
const val ITEM_INDEX = 5
const val ACCOUNT_INDEX = 6

val CUSTOMIZED_LIST = listOf("账户","分类","商家","项目","成员")

const val THEME_PURPLE = "紫色"
const val THEME_RED = "红色"
const val THEME_YELLOW = "黄色"
const val THEME_ORANGE = "橙色"
const val THEME_BLUE = "蓝色"
const val THEME_GREEN = "绿色"
const val THEME_PINK = "粉色"
val THEME_LIST = listOf<String>(THEME_PURPLE, THEME_RED, THEME_PINK,THEME_ORANGE, THEME_YELLOW,
    THEME_BLUE, THEME_GREEN)

const val RESET_PATTERN = "reset pattern"

const val VOID_TEMPLATE = "无模板"