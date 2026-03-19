INSERT INTO UserDB.tb_user (id, name, email, password, created, modified, last_login, token, is_active, `role`)
VALUES('24d4160f-4e67-4b0a-8558-8fafe396e77c', 'Cesar', 'cesar@test.com', '$2a$10$8uEo.KijS2GBr6WZ44sZOOj61sVWHRjEJ3O3cJLZuClQ3knkLHJ4u', '2026-03-18 18:00:48', NULL, '2026-03-18 18:00:48', 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huLmRvZUBleGFtcGxlLmNvbSIsInJvbGVzIjpbIlJPTEVfVVNFUiJdLCJpYXQiOjE3NzM4OTI4NDgsImV4cCI6MTc3Mzg5NDY0OH0.Xc2MiVie7Uv5INBMmjO8wrJ9aVb4kez_bi125U2Bzy8', 1, 'ADMIN');



INSERT INTO UserDB.tb_phone (id, user_id, number, city_code, country_code)
VALUES ('a0a149c2-aa55-4c22-ae85-3bb46ad4cf72', '24d4160f-4e67-4b0a-8558-8fafe396e77c', '945749', '1', '57');