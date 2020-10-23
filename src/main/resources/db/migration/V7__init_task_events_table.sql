drop table if exists tasks_events;
create  table tasks_events(
id bigint primary key AUTO_INCREMENT,
task_id bigint,
occurrence datetime,
name varchar (30)
);