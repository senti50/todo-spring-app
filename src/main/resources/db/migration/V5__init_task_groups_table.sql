create  table tasks_groups(
id bigint primary key AUTO_INCREMENT,
description varchar (100) not null ,
done bit
);

alter table tasks add column  task_group_id bigint null;
alter table tasks add foreign key (task_group_id) references tasks_groups(id);