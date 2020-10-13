create  table projects(
id bigint primary key AUTO_INCREMENT,
description varchar (100) not null
);
create  table project_steps(
id bigint primary key AUTO_INCREMENT,
description varchar (100) not null ,
days_to_deadline int not null ,
project_id bigint not null,
foreign key (project_id) references projects(id)
);
alter table tasks_groups
add column project_id bigint null;
alter table tasks_groups
add foreign key (project_id) references projects(id);