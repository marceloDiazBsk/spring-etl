create table providers_copy(
    id SERIAL,
    provider_id bigint,
    name varchar(255),
    fantasy_name varchar(255),
    fiscal_number varchar(255),
    address varchar(255),
    department_id bigint,
    city_id bigint,
    area text,
    phone varchar(255),
    status boolean,
    hidden boolean,
    created_at varchar(255),

    PRIMARY KEY (id)
)