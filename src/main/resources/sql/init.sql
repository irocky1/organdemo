create table organ_demo.dept
(
	id int auto_increment comment '主键'
		primary key,
	dept_name varchar(50) not null comment '部门名称',
	org_id int default '111' not null comment '组织id',
	lft int default '0' null comment '左值',
	rgt int default '0' null comment '右值',
	flag int default '0' null
)
comment '组织部门表'
;

create index dept_scope
	on dept (lft, rgt)
;

create index dept_rgt_index
	on dept (rgt)
;

create table organ_demo.user
(
	id int auto_increment comment '主键'
		primary key,
	org_id int not null comment '组织id',
	flag tinyint default '0' null comment '删除标记 1是 0否',
	user_name varchar(20) null comment '用户名称',
	feed_id int null comment '名片id'
)
;

create view organ_demo.dept_view as 
SELECT
    `node`.`id`                       AS `id`,
    `node`.`dept_name`                AS `dept_name`,
    `node`.`lft`                      AS `lft`,
    `node`.`rgt`                      AS `rgt`,
    `node`.`flag`                     AS `flag`,
    `node`.`org_id`                   AS `org_id`,
    (count(`parent`.`dept_name`) - 1) AS `deep`
  FROM (`organ_demo`.`dept` `node`
    JOIN `organ_demo`.`dept` `parent`)
  WHERE (`node`.`lft` BETWEEN `parent`.`lft` AND `parent`.`rgt`)
  GROUP BY `node`.`dept_name`
  ORDER BY `node`.`lft`;

