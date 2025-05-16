-- Insert languages only if not already present
INSERT INTO languages (name, code, flag)
SELECT 'English', 'en-US', '🇺🇸' WHERE NOT EXISTS (SELECT 1 FROM languages WHERE code = 'en-US');
INSERT INTO languages (name, code, flag)
SELECT 'French', 'fr-FR', '🇫🇷' WHERE NOT EXISTS (SELECT 1 FROM languages WHERE code = 'fr-FR');
INSERT INTO languages (name, code, flag)
SELECT 'German', 'de-DE', '🇩🇪' WHERE NOT EXISTS (SELECT 1 FROM languages WHERE code = 'de-DE');
INSERT INTO languages (name, code, flag)
SELECT 'Hindi', 'hi-IN', '🇮🇳' WHERE NOT EXISTS (SELECT 1 FROM languages WHERE code = 'hi-IN');
INSERT INTO languages (name, code, flag)
SELECT 'Italian', 'it-IT', '🇮🇹' WHERE NOT EXISTS (SELECT 1 FROM languages WHERE code = 'it-IT');
INSERT INTO languages (name, code, flag)
SELECT 'Portuguese', 'pt-PT', '🇵🇹' WHERE NOT EXISTS (SELECT 1 FROM languages WHERE code = 'pt-PT');
INSERT INTO languages (name, code, flag)
SELECT 'Spanish', 'es-ES', '🇪🇸' WHERE NOT EXISTS (SELECT 1 FROM languages WHERE code = 'es-ES');
INSERT INTO languages (name, code, flag)
SELECT 'Thai', 'th-TH', '🇹🇭' WHERE NOT EXISTS (SELECT 1 FROM languages WHERE code = 'th-TH');

-- The following inserts are commented out to avoid duplicate or unwanted data
-- password is 'user'
-- INSERT INTO users (email, password, spoken_language_id, studied_language_id) VALUES ('user@example.com', '$2b$10$pFFRHG4TbWv5e1vLeexrteXS.mkpi.znp.6hMMAz/Z4e5i1bLIbt2', 2, 1);
-- password is 'admin'
-- INSERT INTO users (email, password, spoken_language_id, studied_language_id) VALUES ('admin@example.com', '$2b$10$jXLT/mrlt64KJRnDREEbeup.6j6cO2Q7Oz622CpIelfYu9QpLWghG', 5, 3);

-- English Words for user 1 (user@example.com)
-- INSERT INTO words (word, user_id, language_id, failed_usages, successful_usages) VALUES ('coffee', 1, 1, 4, 2);
-- INSERT INTO words (word, user_id, language_id, failed_usages, successful_usages) VALUES ('book', 1, 1, 3, 5);
-- INSERT INTO words (word, user_id, language_id, failed_usages, successful_usages) VALUES ('apple', 1, 1, 2, 6);
-- INSERT INTO words (word, user_id, language_id, failed_usages, successful_usages) VALUES ('table', 1, 1, 1, 4);
-- INSERT INTO words (word, user_id, language_id, failed_usages, successful_usages) VALUES ('chair', 1, 1, 5, 3);
-- INSERT INTO words (word, user_id, language_id, failed_usages, successful_usages) VALUES ('window', 1, 1, 0, 7);
-- INSERT INTO words (word, user_id, language_id, failed_usages, successful_usages) VALUES ('door', 1, 1, 4, 1);
-- INSERT INTO words (word, user_id, language_id, failed_usages, successful_usages) VALUES ('pen', 1, 1, 2, 3);
-- INSERT INTO words (word, user_id, language_id, failed_usages, successful_usages) VALUES ('notebook', 1, 1, 3, 2);
-- INSERT INTO words (word, user_id, language_id, failed_usages, successful_usages) VALUES ('lamp', 1, 1, 1, 5);

-- Italian Words for user 1 (user@example.com)
-- INSERT INTO words (word, user_id, language_id, failed_usages, successful_usages) VALUES ('caffè', 1, 5, 2, 4);
-- INSERT INTO words (word, user_id, language_id, failed_usages, successful_usages) VALUES ('libro', 1, 5, 3, 1);
-- INSERT INTO words (word, user_id, language_id, failed_usages, successful_usages) VALUES ('mela', 1, 5, 1, 6);
-- INSERT INTO words (word, user_id, language_id, failed_usages, successful_usages) VALUES ('bicchiere', 1, 5, 2, 3);
-- INSERT INTO words (word, user_id, language_id, failed_usages, successful_usages) VALUES ('piatto', 1, 5, 1, 4);
-- INSERT INTO words (word, user_id, language_id, failed_usages, successful_usages) VALUES ('forchetta', 1, 5, 3, 2);
-- INSERT INTO words (word, user_id, language_id, failed_usages, successful_usages) VALUES ('coltello', 1, 5, 4, 1);
-- INSERT INTO words (word, user_id, language_id, failed_usages, successful_usages) VALUES ('cucchiaio', 1, 5, 0, 5);
-- INSERT INTO words (word, user_id, language_id, failed_usages, successful_usages) VALUES ('tovaglia', 1, 5, 2, 4);
-- INSERT INTO words (word, user_id, language_id, failed_usages, successful_usages) VALUES ('bicicletta', 1, 5, 1, 6);

-- German Words for user 2 (admin@example.com)
-- INSERT INTO words (word, user_id, language_id, failed_usages, successful_usages) VALUES ('haus', 2, 3, 2, 5);
-- INSERT INTO words (word, user_id, language_id, failed_usages, successful_usages) VALUES ('baum', 2, 3, 1, 4);
-- INSERT INTO words (word, user_id, language_id, failed_usages, successful_usages) VALUES ('wasser', 2, 3, 3, 6);
-- INSERT INTO words (word, user_id, language_id, failed_usages, successful_usages) VALUES ('hund', 2, 3, 0, 7);
-- INSERT INTO words (word, user_id, language_id, failed_usages, successful_usages) VALUES ('katze', 2, 3, 4, 3);
-- INSERT INTO words (word, user_id, language_id, failed_usages, successful_usages) VALUES ('auto', 2, 3, 2, 4);
-- INSERT INTO words (word, user_id, language_id, failed_usages, successful_usages) VALUES ('schule', 2, 3, 1, 5);
-- INSERT INTO words (word, user_id, language_id, failed_usages, successful_usages) VALUES ('buch', 2, 3, 3, 2);
-- INSERT INTO words (word, user_id, language_id, failed_usages, successful_usages) VALUES ('stuhl', 2, 3, 2, 3);
-- INSERT INTO words (word, user_id, language_id, failed_usages, successful_usages) VALUES ('fenster', 2, 3, 1, 6);
