CREATE TABLE authors
(
    id        INT AUTO_INCREMENT NOT NULL,
    biography TEXT NULL,
    born_in   VARCHAR(255) NULL,
    born_on   date NULL,
    name      VARCHAR(255) NOT NULL,
    photo     VARCHAR(255) NULL,
    user_id   INT NULL,
    CONSTRAINT PK_AUTHORS PRIMARY KEY (id)
);

CREATE TABLE books
(
    id            INT AUTO_INCREMENT NOT NULL,
    cover_photo   VARCHAR(255) NULL,
    `description` TEXT NULL,
    format        TEXT         NOT NULL,
    isbn          VARCHAR(255) NULL,
    language      VARCHAR(255) NOT NULL,
    pages         INT NULL,
    rate_counter  INT NULL,
    rating        DOUBLE NULL,
    released_date date NULL,
    title         VARCHAR(255) NOT NULL,
    author_id     INT          NOT NULL,
    CONSTRAINT PK_BOOKS PRIMARY KEY (id)
);

CREATE TABLE books_categories
(
    book_id     INT NOT NULL,
    category_id INT NOT NULL,
    CONSTRAINT PK_BOOKS_CATEGORIES PRIMARY KEY (book_id, category_id)
);

CREATE TABLE books_shelves
(
    id         INT AUTO_INCREMENT NOT NULL,
    date_added date NOT NULL,
    book_id    INT  NOT NULL,
    shelf_id   INT  NOT NULL,
    CONSTRAINT PK_BOOKS_SHELVES PRIMARY KEY (id)
);

CREATE TABLE categories
(
    id   INT AUTO_INCREMENT NOT NULL,
    name VARCHAR(255) NOT NULL,
    CONSTRAINT PK_CATEGORIES PRIMARY KEY (id)
);

CREATE TABLE challenges
(
    id         INT AUTO_INCREMENT NOT NULL,
    date_added date NOT NULL,
    number     INT  NOT NULL,
    user       INT  NOT NULL,
    CONSTRAINT PK_CHALLENGES PRIMARY KEY (id)
);

CREATE TABLE comments
(
    id        INT AUTO_INCREMENT NOT NULL,
    content   TEXT NOT NULL,
    parent_id INT NULL,
    review_id INT  NOT NULL,
    writer_id INT  NOT NULL,
    CONSTRAINT PK_COMMENTS PRIMARY KEY (id)
);

CREATE TABLE friend_request
(
    id           INT AUTO_INCREMENT NOT NULL,
    accepted     BIT(1) NOT NULL,
    rejected     BIT(1) NOT NULL,
    receiver_id  INT NULL,
    requester_id INT NULL,
    CONSTRAINT PK_FRIEND_REQUEST PRIMARY KEY (id)
);

CREATE TABLE reviews
(
    id        INT AUTO_INCREMENT NOT NULL,
    content   TEXT NOT NULL,
    date      date NOT NULL,
    book_id   INT  NOT NULL,
    writer_id INT  NOT NULL,
    CONSTRAINT PK_REVIEWS PRIMARY KEY (id)
);

CREATE TABLE shelves
(
    id      INT AUTO_INCREMENT NOT NULL,
    name    VARCHAR(255) NOT NULL,
    user_id INT          NOT NULL,
    CONSTRAINT PK_SHELVES PRIMARY KEY (id)
);

CREATE TABLE user_likes_reviews
(
    user_id    INT NOT NULL,
    reviews_id INT NOT NULL,
    CONSTRAINT PK_USER_LIKES_REVIEWS PRIMARY KEY (user_id, reviews_id)
);

CREATE TABLE user_rate_book
(
    user_id INT NOT NULL,
    book_id INT NOT NULL,
    CONSTRAINT PK_USER_RATE_BOOK PRIMARY KEY (user_id, book_id)
);

CREATE TABLE users
(
    id            INT AUTO_INCREMENT NOT NULL,
    about_me      TEXT NULL,
    e_mail        VARCHAR(255) NOT NULL,
    first_name    VARCHAR(255) NULL,
    gender        VARCHAR(255) NULL,
    last_name     VARCHAR(255) NULL,
    link_to_site  VARCHAR(255) NULL,
    password      VARCHAR(255) NOT NULL,
    profile_photo VARCHAR(255) NULL,
    user_name     VARCHAR(255) NOT NULL,
    CONSTRAINT PK_USERS PRIMARY KEY (id)
);

CREATE TABLE users_followers
(
    user_id      INT NOT NULL,
    followers_id INT NOT NULL,
    CONSTRAINT PK_USERS_FOLLOWERS PRIMARY KEY (user_id, followers_id)
);

CREATE TABLE users_friends
(
    user_id   INT NOT NULL,
    friend_id INT NOT NULL,
    CONSTRAINT PK_USERS_FRIENDS PRIMARY KEY (user_id, friend_id)
);

CREATE INDEX FK26j0prhaxbvf2c3mf9f2p5dck ON shelves (user_id);

CREATE INDEX FK2j9x9icn4n27jgwx9daltsi9a ON friend_request (receiver_id);

CREATE INDEX FK4klp9o273ej1ywgmie14rvdx3 ON books_categories (category_id);

CREATE INDEX FK4w1y6uda57ga9r5w94rj12v4s ON books_shelves (shelf_id);

CREATE INDEX FK6a9k6xvev80se5rreqvuqr7f9 ON reviews (book_id);

CREATE INDEX FK6g6ireq6qd4nxohq9ldidxfin ON authors (user_id);

CREATE INDEX FKbqnrntp00yi6jcsjct2oc2am7 ON books_shelves (book_id);

CREATE INDEX FKcfu26tdkw5fsafn376tnvwhup ON user_likes_reviews (reviews_id);

CREATE INDEX FKdpo60i7auk5cudv7kkny8jrqb ON comments (review_id);

CREATE INDEX FKetin2ga6w0oln69xfef2wwjqw ON users_friends (friend_id);

CREATE INDEX FKf0l51dg2l9fd7r3q1s71hwdod ON reviews (writer_id);

CREATE INDEX FKfjixh2vym2cvfj3ufxj91jem7 ON books (author_id);

CREATE INDEX FKfn7k7arutruq4t6878e87nufh ON friend_request (requester_id);

CREATE INDEX FKlri30okf66phtcgbe5pok7cc0 ON comments (parent_id);

CREATE INDEX FKo8ue1y8ut48yr9dy6ol1c172g ON challenges (user);

CREATE INDEX FKstn0wiml6td6is3rw2751jy7v ON users_followers (followers_id);

CREATE INDEX FKsxbv9sow5pi0sm5plse4yngvk ON user_rate_book (book_id);

CREATE INDEX FKtdvhgjfwg764l2sltaixna6ku ON comments (writer_id);

ALTER TABLE shelves
    ADD CONSTRAINT FK26j0prhaxbvf2c3mf9f2p5dck FOREIGN KEY (user_id) REFERENCES users (id) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE user_rate_book
    ADD CONSTRAINT FK2amyi3d1w0c6m9oa7ydngj9fj FOREIGN KEY (user_id) REFERENCES users (id) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE friend_request
    ADD CONSTRAINT FK2j9x9icn4n27jgwx9daltsi9a FOREIGN KEY (receiver_id) REFERENCES users (id) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE books_categories
    ADD CONSTRAINT FK4klp9o273ej1ywgmie14rvdx3 FOREIGN KEY (category_id) REFERENCES categories (id) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE books_shelves
    ADD CONSTRAINT FK4w1y6uda57ga9r5w94rj12v4s FOREIGN KEY (shelf_id) REFERENCES shelves (id) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE reviews
    ADD CONSTRAINT FK6a9k6xvev80se5rreqvuqr7f9 FOREIGN KEY (book_id) REFERENCES books (id) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE authors
    ADD CONSTRAINT FK6g6ireq6qd4nxohq9ldidxfin FOREIGN KEY (user_id) REFERENCES users (id) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE users_followers
    ADD CONSTRAINT FK84bqk8303ipwj1aqwsrpkr86u FOREIGN KEY (user_id) REFERENCES users (id) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE user_likes_reviews
    ADD CONSTRAINT FK8i4nssxmsdqh0f8kalm1qfxj5 FOREIGN KEY (user_id) REFERENCES reviews (id) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE books_shelves
    ADD CONSTRAINT FKbqnrntp00yi6jcsjct2oc2am7 FOREIGN KEY (book_id) REFERENCES books (id) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE user_likes_reviews
    ADD CONSTRAINT FKcfu26tdkw5fsafn376tnvwhup FOREIGN KEY (reviews_id) REFERENCES users (id) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE comments
    ADD CONSTRAINT FKdpo60i7auk5cudv7kkny8jrqb FOREIGN KEY (review_id) REFERENCES reviews (id) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE users_friends
    ADD CONSTRAINT FKetin2ga6w0oln69xfef2wwjqw FOREIGN KEY (friend_id) REFERENCES users (id) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE reviews
    ADD CONSTRAINT FKf0l51dg2l9fd7r3q1s71hwdod FOREIGN KEY (writer_id) REFERENCES users (id) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE books
    ADD CONSTRAINT FKfjixh2vym2cvfj3ufxj91jem7 FOREIGN KEY (author_id) REFERENCES authors (id) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE friend_request
    ADD CONSTRAINT FKfn7k7arutruq4t6878e87nufh FOREIGN KEY (requester_id) REFERENCES users (id) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE comments
    ADD CONSTRAINT FKlri30okf66phtcgbe5pok7cc0 FOREIGN KEY (parent_id) REFERENCES comments (id) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE books_categories
    ADD CONSTRAINT FKmsuoucvyyccli3f6u59co41cq FOREIGN KEY (book_id) REFERENCES books (id) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE challenges
    ADD CONSTRAINT FKo8ue1y8ut48yr9dy6ol1c172g FOREIGN KEY (user) REFERENCES users (id) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE users_friends
    ADD CONSTRAINT FKry5pun2eg852sbl2l50p236bo FOREIGN KEY (user_id) REFERENCES users (id) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE users_followers
    ADD CONSTRAINT FKstn0wiml6td6is3rw2751jy7v FOREIGN KEY (followers_id) REFERENCES users (id) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE user_rate_book
    ADD CONSTRAINT FKsxbv9sow5pi0sm5plse4yngvk FOREIGN KEY (book_id) REFERENCES books (id) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE comments
    ADD CONSTRAINT FKtdvhgjfwg764l2sltaixna6ku FOREIGN KEY (writer_id) REFERENCES users (id) ON UPDATE RESTRICT ON DELETE RESTRICT;