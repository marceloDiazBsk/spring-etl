create table bill_detail_copy(
    id SERIAL,
    bill_id bigint,
    detail_id bigint,
    quantity int,
    description varchar(255),
    unit_price decimal(24,4),
    vat varchar(255),
    total_vat decimal(24,4),
    total_amount decimal(24,4),
    status boolean,
    hidden boolean,
    vat5 decimal(24,4),
    vat10 decimal(24,4),
    exempt decimal(24,4),
    created_at varchar(255),

    PRIMARY KEY (id)
)