-- table for RowId.java

connect hr/hr

DROP TABLE books;

CREATE TABLE books 
(
  name   VARCHAR2(25),
  author VARCHAR2(50),
  isbn   NUMBER,
  category VARCHAR2(25),
  edition  NUMBER, 
  premier_day  DATE,
  sold_amount  NUMBER,
  instock      NUMBER,
  purchase_plan NUMBER
);

INSERT INTO books VALUES ('book1', 'namea', 43868, 'science', 1,
                                to_date ('04-dec-2000', 'dd-mon-yyyy'),
                                589, 200, 0);
INSERT INTO books VALUES ('book2', 'namea', 37369, 'business', 1,
                                to_date ('04-oct-2001', 'dd-mon-yyyy'),
                                967, 102, 0);
INSERT INTO books VALUES ('book3', 'nameb', 37486, 'business', 1,
                                to_date ('30-oct-2002', 'dd-mon-yyyy'),
                                450, 289, 0);
INSERT INTO books VALUES ('book4', 'namec', 37369, 'business', 1,
                                to_date ('04-oct-2001', 'dd-mon-yyyy'),
                                967, 102, 0);
INSERT INTO books VALUES ('book5', 'named', 25936, 'business', 1,
                                to_date ('04-jan-2003', 'dd-mon-yyyy'),
                                1004, 230, 0);
INSERT INTO books VALUES ('book6', 'namee', 37375, 'economy', 1,
                                to_date ('04-oct-2002', 'dd-mon-yyyy'),
                                560, 58, 0);
INSERT INTO books VALUES ('book7', 'namec', 37525, 'economy', 1,
                                to_date ('04-feb-2001', 'dd-mon-yyyy'),
                                200, 598, 0);
INSERT INTO books VALUES ('book8', 'namef', 37340, 'economy', 1,
                                to_date ('25-mar-2003', 'dd-mon-yyyy'),
                                778, 234, 0);
INSERT INTO books VALUES ('book9', 'nameg', 37568, 'economy', 1,
                                to_date ('04-sep-2001', 'dd-mon-yyyy'),
                                457, 358, 0);
INSERT INTO books VALUES ('book10', 'namec', 37753, 'economy', 1,
                                to_date ('04-oct-2002', 'dd-mon-yyyy'),
                                385, 1354, 0);
INSERT INTO books VALUES ('book11', 'nameh', 37642, 'economy', 1,
                                to_date ('01-apr-2003', 'dd-mon-yyyy'),
                                687, 234, 0);
INSERT INTO books VALUES ('book12', 'namei', 34956, 'health', 1,
                                to_date ('04-oct-2002', 'dd-mon-yyyy'),
                                1560, 358, 0);
INSERT INTO books VALUES ('book13', 'namej', 342355, 'health', 2,
                                to_date ('04-may-2003', 'dd-mon-yyyy'),
                                1356, 458, 0);
INSERT INTO books VALUES ('book14', 'namek', 346723, 'health', 1,
                                to_date ('04-jun-2003', 'dd-mon-yyyy'),
                                2004, 138, 0);

INSERT INTO books VALUES ('book15', 'namee', 735356, 'romance', 1,
                                to_date ('24-nov-2002', 'dd-mon-yyyy'),
                                1609, 108, 0);
INSERT INTO books VALUES ('book16', 'namel', 736465, 'romance', 1,
                                to_date ('24-aug-2003', 'dd-mon-yyyy'),
                                856, 408, 0);
INSERT INTO books VALUES ('book17', 'namem', 737564, 'romance', 1,
                                to_date ('23-jul-2003', 'dd-mon-yyyy'),
                                782, 368, 0);
INSERT INTO books VALUES ('book18', 'nameo', 737453, 'romance', 1,
                                to_date ('26-jun-2003', 'dd-mon-yyyy'),
                                1009, 108, 0);
INSERT INTO books VALUES ('book19', 'namek', 935427, 'cook', 1,
                                to_date ('24-nov-2002', 'dd-mon-yyyy'),
                                8463, 302, 0);
INSERT INTO books VALUES ('book20', 'namea', 936471, 'cook', 1,
                                to_date ('24-jan-2003', 'dd-mon-yyyy'),
                                609, 832, 0);
INSERT INTO books VALUES ('book21', 'namep', 632432, 'linguistics', 2,
                                to_date ('24-nov-2003', 'dd-mon-yyyy'),
                                1003, 391, 0);
INSERT INTO books VALUES ('book22', 'nameq', 636832, 'linguistics', 1,
                                to_date ('14-feb-2003', 'dd-mon-yyyy'),
                                609, 897, 0);

INSERT INTO books VALUES ('book23', 'namee', 746534, 'suspense', 1,
                                to_date ('24-nov-2002', 'dd-mon-yyyy'),
                                609, 846, 0);
INSERT INTO books VALUES ('book24', 'namex', 741935, 'suspense', 1,
                                to_date ('24-apr-2003', 'dd-mon-yyyy'),
                                531, 637, 0);
INSERT INTO books VALUES ('book25', 'namey', 744865, 'suspense', 1,
                                to_date ('25-may-2003', 'dd-mon-yyyy'),
                                325, 389, 0);
commit;
exit
