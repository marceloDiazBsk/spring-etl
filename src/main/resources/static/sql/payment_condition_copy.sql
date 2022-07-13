create table payment_condition_copy(
    id SERIAL,
    payment_condition_id bigint,
    name varchar(255),
    status boolean,
    hidden boolean,
    created_at varchar(255),

    PRIMARY KEY (id)
)