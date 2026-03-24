ALTER TABLE users 
  -- 既存の文字列カラムを削除
  DROP COLUMN affiliation,
  DROP COLUMN position,
  -- 外部キー用のカラムを追加
  ADD COLUMN affiliation_id INTEGER,
  ADD COLUMN position_id INTEGER,
  -- 外部キー制約の追加
  ADD CONSTRAINT fk_user_affiliation FOREIGN KEY (affiliation_id) REFERENCES affiliations(id),
  ADD CONSTRAINT fk_user_position FOREIGN KEY (position_id) REFERENCES positions(id);