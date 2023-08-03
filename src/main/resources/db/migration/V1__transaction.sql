CREATE TABLE transaction_entity
(
    id          SERIAL PRIMARY KEY,
    account_id  INTEGER,
    parent_id   INTEGER,
    amount      INTEGER,
    description VARCHAR(500),
    category    VARCHAR(50),
    time        TIMESTAMP,
    created     TIMESTAMP
);

create index on transaction_entity (account_id, parent_id);
create index on transaction_entity (account_id, id);
-- create sequence if not exists transaction_entity_seq;
