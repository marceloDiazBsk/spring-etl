create table source_copy(
    id SERIAL,
    source_id bigint,
    name varchar(255),
    status boolean,
    hidden boolean,
    created_at varchar(255),

    PRIMARY KEY (id)
)