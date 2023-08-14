# myBank App
A simple app for managing your transactions online

## Transactions
Each transaction has a few fields which define it
- **accountId** - the account you want to update
- **time** - the time you want the transaction be take effect
- **amount** - an integer number which represents the (income/spend) such as 1000 which represents $10.00
- **description** - a description for the transaction
- **category** - a category for the transaction

## API endpoints
- **POST** /api/add to submit new transactions or update previous ones, returns id which is required for the updating
- **GET** /api/balance to see your balance
- **GET** /api/show to view the list of transactions

Every API request requires your accountId and optionally the time you want to reference

## Example Requests
### New Transaction

```
POST http://localhost:8080/api/add
Content-Type: application/json

{
"accountId": 1,
"amount": "-1000",
"category": "groceries",
"description": "dinner"
"time": "2023-08-01T14:14:23Z"
}
```

### Update Transaction
```
POST http://localhost:8080/api/add
Content-Type: application/json

{
  "accountId": 1,
  "amount": "-2500",
  "id": 1,
  "category": "bills",
  "description": "electricity"
  "time": "2023-08-01T14:14:23Z"
}
```
### Show Balance
at the current time:
```
GET http://localhost:8080/api/balance?accountId=1
```
at a specific time:
```
GET http://localhost:8080/api/balance?accountId=1&time=2023-08-01T14:14:23Z
```
### Show Transactions
at the current time:
```
GET http://localhost:8080/api/show?accountId=1
```
with sorting:
```
GET http://localhost:8080/api/show?accountId=1&sort=description,asc&sort=category,desc
```
with filtering using search:
```
GET http://localhost:8080/api/show?accountId=1&search=amount<0
GET http://localhost:8080/api/show?accountId=1&search=time>2023-08-02
GET http://localhost:8080/api/show?accountId=1&search=description==electricity;category==bills
```
For more information on filtering you can refer [here](https://github.com/jirutka/rsql-parser#grammar-and-semantic)
