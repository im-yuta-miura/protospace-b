CREATE TABLE prototypes (
  id          SERIAL          NOT NULL,
  title       VARCHAR(128)    NOT NULL,
  catchphrase VARCHAR(128)    NOT NULL,
  concept     VARCHAR(512)    NOT NULL,
  image       VARCHAR(512)    NOT NULL,
  user_id     INT             NOT NULL,

  PRIMARY KEY (id),
  FOREIGN KEY (user_id)  REFERENCES users(id)
);