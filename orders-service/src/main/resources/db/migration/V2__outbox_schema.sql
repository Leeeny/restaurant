CREATE TABLE IF NOT EXISTS orders.orders_outbox
(
    order_id   BIGINT PRIMARY KEY,
    city       TEXT      NOT NULL,
    street     TEXT      NOT NULL,
    house      INTEGER   NOT NULL,
    apartment  INTEGER   NOT NULL,
    created_by TEXT      NOT NULL,
    created_at TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS orders.heartbeat_table
(
    id         INTEGER primary key,
    updated_at TIMESTAMP NOT NULL
);

CREATE PUBLICATION orders_outbox_publication FOR TABLE orders.orders_outbox, orders.heartbeat_table;