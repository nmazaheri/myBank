CREATE TABLE bank_transaction
(
    id          SERIAL PRIMARY KEY,
    account_id  INTEGER,
    parent_id   INTEGER,
    amount      INTEGER,
    description VARCHAR(500),
    category    VARCHAR(50),
    time        TIMESTAMP
);

create index on bank_transaction (account_id, id);
create index on bank_transaction (account_id, parent_id);