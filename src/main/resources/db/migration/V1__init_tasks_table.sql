drop table if exists tasks;
create  table tasks(
id bigint primary key AUTO_INCREMENT,
description varchar (100) not null ,
done bit
);