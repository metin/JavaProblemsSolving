-- create users and tables for ProxySession01,02,03

connect system/manager

drop role role1;
drop role role2;
drop role role3;

drop user client cascade;
drop user proxy  cascade;

drop table account;
create table account (purchase number);
insert into account values(6);
insert into account values(9);

create user proxy identified by proxy;
create user client identified by client;

grant create session, connect, resource to proxy;
grant create session, connect, resource to client;

create role role1;
create role role2;
create role role3;

grant select on account to role1;
grant insert on account to role2;
grant delete on account to role3;
grant role1, role2, role3 to client;

commit;

connect client/client;

drop table client_account;
create table client_account(balance number);
insert into client_account values(600);
insert into client_account values(900);

commit;
exit
