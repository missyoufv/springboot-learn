mysql 索引类型：B-Tree 索引、哈希索引、全文索引等

mysql 搜索引擎：innodb,myisam,memory

innodb存储引擎索引失效场景：索引列参与余下场景
              1.使用or，索引失效，除非所有字段都加索引（有待考证）
              2.not in （有待考证），not exist
              3.like '%a'
              4.隐式类型转换
              5.函数操作
              6.多列索引不遵循最左匹配原则
              7.大于小于（如果是多列索引情况，查询不是覆盖索引，索引失效，如果是单列索引，索引生效）
              8.order by 索引字段，不放在where中，索引失效 ，& select * from sys_user order by (非主键索引) asc
                    为什么只有order by 字段出现在where条件中时,才会利用该字段的索引而避免排序
                           一条SQL实际上可以分为三步。

                           1.得到数据

                           2.处理数据

                           3.返回处理后的数据

                           比如上面的这条语句select sid from student where sid < 50000  order by sid desc

                           第一步：根据where条件和统计信息生成执行计划，得到数据。

                           第二步：将得到的数据排序。

                           当执行处理数据（order by）时，数据库会先查看第一步的执行计划，看order by 的字段是否在执行计划中利用了索引。如果是，则可以利用索引顺序而直接取得已经排好序的数据。如果不是，则排序操作。

                           第三步：返回排序后的数据。


                           当执行处理数据（order by）时，数据库会先查看第一步的执行计划，看order by 的字段是否在执行计划中利用了索引。如果是，则可以利用索引顺序而直接取得已经排好序的数据。如果不是，则排序操作

聚簇索引并不是单独的一种索引类型，而是一种数据存储方式。具体细节依赖其实现方式，但是innodb的聚簇索引实际上是在同一结构中保存了B-tree 索引 和数据行。
mysql的聚簇索引是指innodb引擎的特性，mysiam并没有，如果需要该索引，只要将索引指定为主键（primary key）就可以了。

对于InnoDB表，在非主键列的其他列上建的索引就是二级索引（因为聚集索引只有一个）。二级索引可以有0个，1个或者多个。二级索引和聚集索引的区别是什么呢？
二级索引的节点页和聚集索引一样，只存被索引列的值，而二级索引的叶子页除了索引列值，还存这一列对应的主键值。

innodb索引分类：
    聚簇索引(clustered index)：
        1)  有主键时，根据主键创建聚簇索引
        2)  没有主键时，会用一个唯一且不为空的索引列做为主键，成为此表的聚簇索引
        3) 如果以上两个都不满足那innodb自己创建一个虚拟的聚集索引

    辅助索引(secondary index)又叫二级索引
       非聚簇索引都是辅助索引，像复合索引、前缀索引、唯一索引

B-Tree索引有如下几种实现在innodb中：
UNIQUE(唯一索引) ALTER TABLE `table_name` ADD UNIQUE ( `column`)
添加INDEX(普通索引) ALTER TABLE `table_name` ADD INDEX index_name ( `column` )
多列索引  ALTER TABLE `table_name` ADD INDEX index_name ( `column1`, `column2`, `column3` )

补充：mysql会默认给主键创建一个唯一索引，但是主键跟唯一索引也是有区别的

    1.主键为一种约束，唯一索引为一种索引，本质上就不同。

    2.主键在表中只能有一个，唯一索引可以有多个。

    3.主键创建后一定包含唯一性索引，而唯一索引不一定就是主键。

    4.主键不能为null,唯一索引可以为null.

    5.主键可以被其它表引用，唯一索引不能。

    6.主键和索引都是键，主键是逻辑键，索引为物理键，即主键不实际存在。


覆盖索引：如果索引包含所有满足查询需要的数据的索引成为覆盖索引(Covering Index)，也就是平时所说的不需要回表操作

多列索引如下（last_name,first_name）
explain select * from people where  last_name in ('Akroyd'); 索引生效
explain select * from people where  last_name not in ('Akroyd');索引失效

explain select * from people where  first_name = 'Debbie' or last_name = 'Akroyd' or dob = '1958-12-07' 索引失效

explain select * from people force INDEX(last_name) where   last_name = 'Akroyd' 索引生效

explain select * from people where  last_name like '%Akr%' 索引失效
-- 覆盖索引
explain select last_name from people where  last_name like 'Akr%' 索引生效

explain select last_name from people 索引生效

explain select * from people where  last_name > 'Akroyd'  索引失效
explain select last_name from people where  last_name > 'Akroyd'  索引生效 覆盖索引

explain select * from people where  last_name is null;
explain select * from people where  last_name is not null;


https://blog.csdn.net/qq_27529917/article/details/87954427
1. 筛选条件放置在where和on上的不同？

在Mysql多表连接查询的执行细节
    where里可能有关于每张表的筛选条件，不同表的条件生效时期不同。对于驱动表，在执行一开始就会通过where上关于词表的条件筛选一条或者一批记录，然后通过on条件关联下一张表，
    将得到的结果集再用where上第二张表的条件做过滤，然后重复此过程直到所有表关联完毕。也就是对于驱动表，因为只有where生效，对于其他被驱动表，先被on关联，也就是on先生效，
    然后再用where过滤关联的结果集。同时对于外表连接，比如left join和right join，把条件放在on上，如果被关联表没有匹配上，那么外表还是能放入结果集的；
    而如果将条件放在where上，因为where是对关联后的结果做过滤，此时之前匹配的记录也会被筛选掉。

    例子如下

    create table a(a1 int primary key, a2 int ,index(a2));
    create table c(c1 int primary key, c2 int ,index(c2), c3 int);
    create table b(b1 int primary key, b2 int);

    insert into a values(1,1),(2,2),(3,3),(4,4),(5,5),(6,6),(7,7),(8,8),(9,9),(10,10);
    insert into b values(1,1),(2,2),(3,3),(4,4),(5,5),(6,6),(7,7),(8,8),(9,9),(10,10);
    insert into c values(1,1,1),(2,4,4),(3,6,6),(4,5,5),(5,3,3),(6,3,3),(7,2,2),(8,8,8),(9,5,5),(10,3,3);


    select a.*,b.* from a left join b on a.a1=b.b1 where a.a2>3 and b.b2<5;  筛选条件放在最后，因此对于关联那种空的数据没有

    select a.*,b.* from a left join b on a.a1=b.b1 and b.b2<5 where a.a2>3 ;  关联放到on后面，因此对于没有关联到结果，也是可以的有null记录与驱动表连接的


    2. 外连接时外表是否一定为驱动表？

    大部分情況下外表都是驱动表，这是因为外表在关联内表时，如果没有匹配的记录，那么会生成一条所有字段都为null的内表记录来和外表行关联，就像上图一样。
    用外表做驱动表，这样在Nested-Loop join时才能很容易的判断是内表是否有匹配的记录，判断是否用null来关联。但有些个别情况下，外表也可能是被驱动表，比如如下sql：


    . 是否应该使用join连接查询？

    对于多表间查询，可以使用join关联，也可以拆成多张表的单独查询。对于join关联查询，如果使用不当，很容易造成对被驱动表的多次扫描(Block Nested-Loop join)，进而导致IO压力增大，同时多次的扫面被驱动表，会导致被驱动表的数据页被置入mysql buffer-pool的young区域，一次join就替换掉之前真正的热点数据页。

    正常一次查询仅全表扫描一次数据的话，数据页会被放入buffer pool的old区域的前端，但是如果一个数据页在第一次使用时，如果不在buffer pool，那么会加载仅buffer pool，放在old区域的前端，但是如果这个数据页在buffer pool，且距离上次使用时间超过1S，也就是join查询时间拆过1S，那么就会把数据页放到buffer pool的young区域，也就是热点区域，这样会导致原先真正的热点数据被替换。这样即使join查询结束了，对mysql的性能有很直接的负面影响，也就是buffer pool内存命中率突然暴跌，查询时间突然变长，很多都需要读取磁盘，需要很长时间才能恢复。

    所以，对于大表的关联查询，如果没有使用上索引，也就是on的字段没有索引，通过explain能看到Extra里有Using join buffer(Block Nested Loop)，那么就不要使用join了。当然能使用上使用的join查询还是比单表查询来的快的，同时还能很方便的做结果筛选。


mysql 主查询和子查询的执行顺序

    下面是有关子查询的一些说明：
    子查询又称内部查询，而包含子查询的语句称之外部查询（又称主查询）。
    所有的子查询可以分为两类，即相关子查询和非相关子查询。
    非相关子查询是独立于外部查询的子查询，子查询总共执行一次，执行完毕后将值传递给外部查询。
    相关子查询的执行依赖于外部查询的数据，外部查询执行一行，子查询就执行一次。
















