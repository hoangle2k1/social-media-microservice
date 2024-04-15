-- users
INSERT INTO users (id, about, activation_code, active, background_color, birthday, color_scheme, country, country_code, email, full_name, gender, language, like_count, location, media_tweet_count, muted_direct_messages, notifications_count, mentions_count, password, password_reset_code, phone, private_profile, profile_customized, profile_started, registration_date, role, tweet_count, username, website, pinned_tweet_id, unread_messages_count, avatar, wallpaper) VALUES (1, null, null, true, 'DEFAULT', null, 'BLUE', null, null, 'user2024@gmail.com', 'Random', null, null, 1, null, 0, true, 0, 0, '$2a$08$TZekzxmq.KO2f.juYNUa4eU/ePYPx3r2MWONVjPDKOBJB4qUlhkxW', null, null, false, true, true, '2021-11-15 14:05:08.000000', 'USER', 0, 'Random', null, 1, 0, 'https://perfumeweb2.s3.eu-central-1.amazonaws.com/ae83099c-885b-499a-bb6f-5e34e1b69e7d_4ec7201fd370bd9870cdb326f0511f38.jpg', 'https://perfumeweb2.s3.eu-central-1.amazonaws.com/dfc8a223-45fc-43da-8b7c-f76e2c2507cd_82ecbca14eb4999212c07257f41c70e7.jpg');
INSERT INTO users (id, about, activation_code, active, background_color, birthday, color_scheme, country, country_code, email, full_name, gender, language, like_count, location, media_tweet_count, muted_direct_messages, notifications_count, mentions_count, password, password_reset_code, phone, private_profile, profile_customized, profile_started, registration_date, role, tweet_count, username, website, pinned_tweet_id, unread_messages_count, avatar, wallpaper) VALUES (2, 'Hello twitter!', null, true, 'DIM', null, 'BLUE', 'UA', 'UA', 'user2016@gmail.com', 'MrCat', 'Cat', 'Ukrainian - українська', 30, 'New York', 22, false, 0, 0, '$2a$08$TZekzxmq.KO2f.juYNUa4eU/ePYPx3r2MWONVjPDKOBJB4qUlhkxW', null, 666966623, true, false, true, '2021-08-01 23:34:32.000000', 'USER', 4, 'Cat', 'https://www.google.com', null, 0, 'https://perfumeweb2.s3.eu-central-1.amazonaws.com/348b7dbe-3ac5-477f-8483-edc24f53091b_814370.jpg', 'https://perfumeweb2.s3.eu-central-1.amazonaws.com/d0e5b95f-acc0-47ef-b499-477f7e5a1a06_PrMnWa2z.jpg');
INSERT INTO users (id, about, activation_code, active, background_color, birthday, color_scheme, country, country_code, email, full_name, gender, language, like_count, location, media_tweet_count, muted_direct_messages, notifications_count, mentions_count, password, password_reset_code, phone, private_profile, profile_customized, profile_started, registration_date, role, tweet_count, username, website, pinned_tweet_id, unread_messages_count, avatar, wallpaper) VALUES (3, 'Hello twitter!', null, true, 'DEFAULT', null, 'BLUE', null, null, 'user2017@gmail.com', 'Kitty', null, null, 0, 'New York', 0, true, 2, 0, '$2a$08$TZekzxmq.KO2f.juYNUa4eU/ePYPx3r2MWONVjPDKOBJB4qUlhkxW', null, null, false, true, true, '2021-08-01 23:34:32.000000', 'USER', 0, 'Kitty', 'https://www.google.com', null, 0, 'https://perfumeweb2.s3.eu-central-1.amazonaws.com/a7e03e7c-c05f-4e30-ba8c-2271fd0b4b43_779301.jpg', null);
INSERT INTO users (id, about, activation_code, active, background_color, birthday, color_scheme, country, country_code, email, full_name, gender, language, like_count, location, media_tweet_count, muted_direct_messages, notifications_count, mentions_count, password, password_reset_code, phone, private_profile, profile_customized, profile_started, registration_date, role, tweet_count, username, website, pinned_tweet_id, unread_messages_count, avatar, wallpaper) VALUES (4, 'Hello twitter!', null, true, 'DEFAULT', null, 'BLUE', null, null, 'user2019@gmail.com', 'JavaCat', null, null, 0, 'Java', 0, false, 1, 0, '$2a$08$TZekzxmq.KO2f.juYNUa4eU/ePYPx3r2MWONVjPDKOBJB4qUlhkxW', null, null, false, true, true, '2021-08-01 23:34:32.000000', 'USER', 0, 'JavaCat', 'https://www.java.com', null, 0, 'https://perfumeweb2.s3.eu-central-1.amazonaws.com/b999d944-c9ec-4a9c-b356-db937211df5c_Ec1OBK3XsAEjVZR.png', null);
INSERT INTO users (id, about, activation_code, active, background_color, birthday, color_scheme, country, country_code, email, full_name, gender, language, like_count, location, media_tweet_count, muted_direct_messages, notifications_count, mentions_count, password, password_reset_code, phone, private_profile, profile_customized, profile_started, registration_date, role, tweet_count, username, website, pinned_tweet_id, unread_messages_count, avatar, wallpaper) VALUES (5, 'Hello twitter!', null, true, 'DEFAULT', null, 'BLUE', null, null, 'user2018@gmail.com', 'hoangle', null, null, 0, 'London', 0, false, 2, 0, '$2a$08$TZekzxmq.KO2f.juYNUa4eU/ePYPx3r2MWONVjPDKOBJB4qUlhkxW', null, null, false, true, true, '2021-08-01 23:34:32.000000', 'USER', 0, 'Кот Бегемот', 'https://www.google.com', null, 0, 'https://perfumeweb2.s3.eu-central-1.amazonaws.com/68a7b0d5-2b0c-493e-85ff-098725c52ecc_Cl5DjoUWYAAslnd.jfif', null);
-- user_subscriptions
INSERT INTO user_subscriptions (subscriber_id, user_id) VALUES (1, 2);
INSERT INTO user_subscriptions (subscriber_id, user_id) VALUES (1, 3);
INSERT INTO user_subscriptions (subscriber_id, user_id) VALUES (1, 4);
INSERT INTO user_subscriptions (subscriber_id, user_id) VALUES (1, 5);
INSERT INTO user_subscriptions (subscriber_id, user_id) VALUES (2, 1);
INSERT INTO user_subscriptions (subscriber_id, user_id) VALUES (5, 1);
