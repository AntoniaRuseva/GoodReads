CREATE SCHEMA IF NOT EXISTS goodreads;

USE goodreads ;

CREATE TABLE IF NOT EXISTS goodreads.users (
    id INT NOT NULL AUTO_INCREMENT,
    about_me TEXT NULL DEFAULT NULL,
    e_mail VARCHAR(255) NOT NULL,
    first_name VARCHAR(255) NULL DEFAULT NULL,
    gender VARCHAR(255) NULL DEFAULT NULL,
    last_name VARCHAR(255) NULL DEFAULT NULL,
    link_to_site VARCHAR(255) NULL DEFAULT NULL,
    password VARCHAR(255) NOT NULL,
    profile_photo VARCHAR(255) NULL DEFAULT NULL,
    user_name VARCHAR(255) NOT NULL,
    PRIMARY KEY (id));


CREATE TABLE IF NOT EXISTS goodreads.authors (
    id INT NOT NULL AUTO_INCREMENT,
    biography TEXT NULL DEFAULT NULL,
    сborn_in VARCHAR(255) NULL DEFAULT NULL,
    born_on DATE NULL DEFAULT NULL,
    name VARCHAR(255) NOT NULL,
    photo VARCHAR(255) NULL DEFAULT NULL,
    user_id INT NULL DEFAULT NULL,
    PRIMARY KEY (id),
    CONSTRAINT FK6g6ireq6qd4nxohq9ldidxfin
    FOREIGN KEY (user_id)
    REFERENCES goodreads.users (id));


CREATE TABLE IF NOT EXISTS goodreads.books (
    id INT NOT NULL AUTO_INCREMENT,
    cover_photo VARCHAR(255) NULL DEFAULT NULL,
    description TEXT NULL DEFAULT NULL,
    format TEXT NOT NULL,
    isbn VARCHAR(255) NULL DEFAULT NULL,
    language VARCHAR(255) NOT NULL,
    pages INT NULL DEFAULT NULL,
    rate_counter INT NULL DEFAULT NULL,
    rating DOUBLE NULL DEFAULT NULL,
    released_date DATE NULL DEFAULT NULL,
    title VARCHAR(255) NOT NULL,
    author_id INT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT FKfjixh2vym2cvfj3ufxj91jem7
    FOREIGN KEY (author_id)
    REFERENCES goodreads.authors (id));


CREATE TABLE IF NOT EXISTS goodreads.categories (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    PRIMARY KEY (id));


CREATE TABLE IF NOT EXISTS goodreads.books_categories (
    book_id INT NOT NULL,
    category_id INT NOT NULL,
    PRIMARY KEY (book_id, category_id),
    CONSTRAINT FK4klp9o273ej1ywgmie14rvdx3
    FOREIGN KEY (category_id)
    REFERENCES goodreads.categories (id),
    CONSTRAINT FKmsuoucvyyccli3f6u59co41cq
    FOREIGN KEY (book_id)
    REFERENCES goodreads.books (id));


CREATE TABLE IF NOT EXISTS goodreads.shelves (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    user_id INT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT FK26j0prhaxbvf2c3mf9f2p5dck
    FOREIGN KEY (user_id)
    REFERENCES goodreads.users (id));

CREATE TABLE IF NOT EXISTS goodreads.books_shelves (
    id INT NOT NULL AUTO_INCREMENT,
    date_added DATE NOT NULL,
    book_id INT NOT NULL,
    shelf_id INT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT FK4w1y6uda57ga9r5w94rj12v4s
    FOREIGN KEY (shelf_id)
    REFERENCES goodreads.shelves (id),
    CONSTRAINT FKbqnrntp00yi6jcsjct2oc2am7
    FOREIGN KEY (book_id)
    REFERENCES goodreads.books (id));


CREATE TABLE IF NOT EXISTS goodreads.challenges (
    id INT NOT NULL AUTO_INCREMENT,
    date_added DATE NOT NULL,
    number INT NOT NULL,
    user_fk INT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT FKo8ue1y8ut48yr9dy6ol1c172g
    FOREIGN KEY (user_fk)
    REFERENCES goodreads.users (id));


CREATE TABLE IF NOT EXISTS goodreads.reviews (
    id INT NOT NULL AUTO_INCREMENT,
    content TEXT NOT NULL,
    date DATE NOT NULL,
    book_id INT NOT NULL,
    writer_id INT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT FK6a9k6xvev80se5rreqvuqr7f9
    FOREIGN KEY (book_id)
    REFERENCES goodreads.books (id),
    CONSTRAINT FKf0l51dg2l9fd7r3q1s71hwdod
    FOREIGN KEY (writer_id)
    REFERENCES goodreads.users (id));


CREATE TABLE IF NOT EXISTS goodreads.comments (
    id INT NOT NULL AUTO_INCREMENT,
    content TEXT NOT NULL,
    parent_id INT NULL DEFAULT NULL,
    review_id INT NOT NULL,
    writer_id INT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT FKdpo60i7auk5cudv7kkny8jrqb
    FOREIGN KEY (review_id)
    REFERENCES goodreads.reviews (id),
    CONSTRAINT FKlri30okf66phtcgbe5pok7cc0
    FOREIGN KEY (parent_id)
    REFERENCES goodreads.comments (id),
    CONSTRAINT FKtdvhgjfwg764l2sltaixna6ku
    FOREIGN KEY (writer_id)
    REFERENCES goodreads.users (id));


CREATE TABLE IF NOT EXISTS goodreads.friend_request (
    id INT NOT NULL AUTO_INCREMENT,
    accepted INT NOT NULL,
    rejected INT NOT NULL,
    receiver_id INT NULL DEFAULT NULL,
    requester_id INT NULL DEFAULT NULL,
    PRIMARY KEY (id),
    CONSTRAINT FK2j9x9icn4n27jgwx9daltsi9a
    FOREIGN KEY (receiver_id)
    REFERENCES goodreads.users (id),
    CONSTRAINT FKfn7k7arutruq4t6878e87nufh
    FOREIGN KEY (requester_id)
    REFERENCES goodreads.users (id));


CREATE TABLE IF NOT EXISTS goodreads.user_likes_reviews (
    user_id INT NOT NULL,
    reviews_id INT NOT NULL,
    PRIMARY KEY (user_id, reviews_id),
    CONSTRAINT FK8i4nssxmsdqh0f8kalm1qfxj5
    FOREIGN KEY (user_id)
    REFERENCES goodreads.reviews (id),
    CONSTRAINT FKcfu26tdkw5fsafn376tnvwhup
    FOREIGN KEY (reviews_id)
    REFERENCES goodreads.users (id));



CREATE TABLE IF NOT EXISTS goodreads.user_rate_book (
    user_id INT NOT NULL,
    book_id INT NOT NULL,
    PRIMARY KEY (user_id, book_id),
    CONSTRAINT FK2amyi3d1w0c6m9oa7ydngj9fj
    FOREIGN KEY (user_id)
    REFERENCES goodreads.users (id),
    CONSTRAINT FKsxbv9sow5pi0sm5plse4yngvk
    FOREIGN KEY (book_id)
    REFERENCES goodreads.books (id));


CREATE TABLE IF NOT EXISTS goodreads.users_followers (
    user_id INT NOT NULL,
    followers_id INT NOT NULL,
    PRIMARY KEY (user_id, followers_id),
    CONSTRAINT FK84bqk8303ipwj1aqwsrpkr86u
    FOREIGN KEY (user_id)
    REFERENCES goodreads.users (id),
    CONSTRAINT FKstn0wiml6td6is3rw2751jy7v
    FOREIGN KEY (followers_id)
    REFERENCES goodreads.users (id));


CREATE TABLE IF NOT EXISTS goodreads.users_friends (
    user_id INT NOT NULL,
    friend_id INT NOT NULL,
    PRIMARY KEY (user_id, friend_id),
    CONSTRAINT FKetin2ga6w0oln69xfef2wwjqw
    FOREIGN KEY (friend_id)
    REFERENCES goodreads.users (id),
    CONSTRAINT FKry5pun2eg852sbl2l50p236bo
    FOREIGN KEY (user_id)
    REFERENCES goodreads.users (id));