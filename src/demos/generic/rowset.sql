connect hr/hr

DROP TABLE reservation;

CREATE TABLE reservation 
(
  seatno NUMBER PRIMARY KEY,
  tdate  DATE,
  name   VARCHAR2(25),
  class  VARCHAR2(10)
);

INSERT INTO reservation VALUES (101, to_date ('04-dec-2000', 'dd-mon-yyyy'),
  'Ashok', 'Business');
INSERT INTO reservation VALUES (102, to_date ('04-dec-2000', 'dd-mon-yyyy'),
  'Allen', 'Economy');
INSERT INTO reservation VALUES (103, to_date ('04-dec-2000', 'dd-mon-yyyy'),
  'Smith', 'Economy');
INSERT INTO reservation VALUES (104, to_date ('04-dec-2000', 'dd-mon-yyyy'),
  'Sunil', 'Business');
INSERT INTO reservation VALUES (105, to_date ('04-dec-2000', 'dd-mon-yyyy'),
  'Umesh', 'Business');
INSERT INTO reservation VALUES (106, to_date ('04-dec-2000', 'dd-mon-yyyy'),
  'Scott', 'Business');

commit;
exit
