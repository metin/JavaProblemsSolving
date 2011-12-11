
-- Schema to RUN RefClient.java

drop table student_table;
drop type STUDENT;
create type STUDENT as object (name VARCHAR (30), age NUMBER);
/
create table student_table of STUDENT;
insert into student_table values ('John', 19);
commit;
exit;



