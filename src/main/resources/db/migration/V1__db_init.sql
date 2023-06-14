CREATE SEQUENCE IF NOT EXISTS sequence_customer START WITH 1 INCREMENT BY 5;

CREATE SEQUENCE IF NOT EXISTS sequence_address START WITH 1 INCREMENT BY 5;

CREATE SEQUENCE IF NOT EXISTS sequence_role START WITH 1 INCREMENT BY 5;

CREATE SEQUENCE IF NOT EXISTS public.sequence_user START WITH 1 INCREMENT BY 5;


CREATE TABLE IF NOT EXISTS customer
(
    id              BIGINT           NOT NULL,
    first_name      VARCHAR(500),
    last_name       VARCHAR(500),
    age             INT              NOT NULL,
    created_at      timestamp        DEFAULT current_timestamp,
    CONSTRAINT pk_customer PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS address
(
    id              BIGINT          NOT NULL,
    street_name     VARCHAR(300),
    number          INT             NOT NULL,
    city            VARCHAR(300),
    country         VARCHAR(300),
    customer_id     BIGINT          NOT NULL,
    CONSTRAINT pk_address PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS role
(
    id   BIGINT      NOT NULL,
    type VARCHAR(20) NOT NULL,
    CONSTRAINT pk_role PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public."user"
(
    id         BIGINT       NOT NULL,
    first_name VARCHAR(50)  NOT NULL,
    last_name  VARCHAR(50)  NOT NULL,
    username   VARCHAR(20)  NOT NULL,
    password   VARCHAR(100) NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public.user_role
(
    role_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    CONSTRAINT pk_user_role PRIMARY KEY (role_id, user_id)
);

ALTER TABLE role
    ADD CONSTRAINT uc_role_type UNIQUE (type);

ALTER TABLE public."user"
    ADD CONSTRAINT uc_user_username UNIQUE (username);

ALTER TABLE address
    ADD CONSTRAINT FK_TO_CUSTOMER_FROM_ADDRESS FOREIGN KEY (customer_id) REFERENCES customer (id);

ALTER TABLE public.user_role
    ADD CONSTRAINT FK_TO_ROLE_FROM_USER_ROLE FOREIGN KEY (role_id) REFERENCES role (id);

ALTER TABLE public.user_role
    ADD CONSTRAINT FK_TO_USER_FROM_USER_ROLE FOREIGN KEY (user_id) REFERENCES "user" (id);
