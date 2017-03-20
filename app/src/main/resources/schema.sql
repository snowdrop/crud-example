DROP TABLE fruit;

CREATE TABLE fruit (
  id SERIAL NOT NULL PRIMARY KEY,
  ftype VARCHAR(256)
);

INSERT INTO fruit (ftype) VALUES ('Apple');
