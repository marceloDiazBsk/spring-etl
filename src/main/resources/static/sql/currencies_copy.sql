create table currency_copy(
    id SERIAL,
    currency_id bigint,
    name varchar(255),
    symbol varchar(255),
    status boolean,
    hidden boolean,
    created_at varchar(255),
    PRIMARY KEY (id)
)