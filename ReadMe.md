9.29 11:00第一次提交
实现了Room数据库和RecycleView列表

待修正：
1.实现PrimaryKey的autoGenerate。现在用Random暂时顶替着
2.由于AppDatabase的单例（静态内部类）使用了MainActivity的单类，只能在MainActivity里进行AppDatabase操作
3.现在在MainActivity中根据不同的OPERATION_MESSAGE来判断执行哪种操作。本来应该在其他Activity里进行操作的。而且也许不该用Switch Case结构，而应该直接进入函数

Todo : 实现数据结构和CardView
