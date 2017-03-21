DROP TABLE fruit;

CREATE TABLE fruit (
  id SERIAL NOT NULL PRIMARY KEY,
  fname VARCHAR(256)
);

INSERT INTO fruit (fname) VALUES ('Apple');
