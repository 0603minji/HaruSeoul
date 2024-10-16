insert into member
(id, nickname, name, user_id, user_pwd, reg_date, email, birth)
values (1, 'minjiiiii', 'kimminji', 'minji0603', '1234',
        '2024-09-04 16:42:12.357000', 'qs1018@naver.com','1994-06-03');

insert into program
    (id, reg_date, update_date, title, detail,
     category1, category2, language, start_time, end_time,
     status, price, group_size_max, group_size_min, reg_member_id, rating)
values (15,  '2024-09-04 16:42:12.357000',  '2024-09-04 17:16:12.247000', 'let''s drop by the hyundai department store and han-river', 'let''s drop by the hyundai department store and han-river', 'activity', null, 'english', '11:00', '14:00', 'unpublished', 15000, 4, 2, 1, 2.5);
