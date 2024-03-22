INSERT INTO role (name)
values ('ROLE_USER'),
('ROLE_ADMIN');

INSERT INTO profile (login, password, firstname, lastname, email, balance, dob)
values ('user', '$2a$12$5FFM8eTRdp1AFEyvimVWG.iNv5kIYvARy2bzb/bB4S9E4YTWENdsa',
        'Victor', 'Lukyanchik', 'victor@mail.ru', 22.10, '2002-04-02');

INSERT INTO profile_role (profile_id, role_id)
values (1,1);
