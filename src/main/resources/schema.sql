DROP TABLE user;
DROP TABLE session;
DROP TABLE upload;

create table user
(
    id                    bigint       not null AUTO_INCREMENT,
    display_name          varchar(32)  not null,
    username              varchar(16)  not null,
    password              varchar(500) not null,
    bio                   varchar(32),
    email                 varchar(64)  not null,
    email_verified        boolean,
    account_verification  varchar(20)  not null,
    failed_login_attempts int,
    login_disabled        boolean,
    mfa_secret            varchar(255),
    mfa_enabled           boolean,
    is_public             boolean,
    PRIMARY KEY (id)
);

create table session
(
    session_token varchar(500) not null,
    user          bigint       not null,
    needs_mfa     boolean,
    is_new        boolean,
    PRIMARY KEY (session_token)
);

create table upload
(
    id         bigint       not null,
    name       varchar(255) not null,
    uploader   bigint       not null,
    content    blob         not null,
    is_private boolean,
    PRIMARY KEY (id)
);