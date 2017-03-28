DROP TABLE IF EXISTS known_fruits;

CREATE TABLE known_fruits (
  id SERIAL NOT NULL PRIMARY KEY,
  fname VARCHAR(256)
);

INSERT INTO known_fruits(fname) VALUES ('Cherry');
INSERT INTO known_fruits(fname) VALUES ('Apple');
INSERT INTO known_fruits(fname) VALUES ('Banana');
