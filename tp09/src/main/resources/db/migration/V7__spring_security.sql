

create table yaps_user (
    username varchar(20), 
    primary key(username),
    userrole varchar(5), -- "CUST" ou "EMP"
    userpassword varchar(500) not null
) engine=innodb;


alter table customer 
    add column username varchar(20) unique,
    add foreign key (username) references yaps_user(username); 


insert into yaps_user values 
(
    'ada', 'CUST', '{noop}cnam'
);

insert into yaps_user values 
(
        'charles', 'CUST', '{noop}cnam'
);

insert into yaps_user values 
(
        'joe', 'EMP', '{noop}cnam'
);


update customer set username = 'ada' where id = '1';
update customer set username = 'charles' where id = '2';