DROP TABLE people IF EXISTS;

create table people
(
	person_id bigint auto_increment
		primary key,
	first_name varchar(20) null,
	last_name varchar(20) null
);

