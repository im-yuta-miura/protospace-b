CREATE TABLE comments(
  comment_id  SERIAL  NOT NULL,
  content     TEXT  NOT NULL,
  user_id  INT  NOT NULL,
  prototype_id  INT  NOT NULL,
  PRIMARY KEY (comment_id),
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
  FOREIGN KEY (prototype_id) REFERENCES prototype(id) ON DELETE CASCADE
);