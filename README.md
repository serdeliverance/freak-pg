# freak-pg

## Requirements

* JDK8
* SBT
* Docker and Docker-compose

## Instructions

1. Start docker-compose

```
docker-compose up
```

2. Run the app

```
sbt run
```

It starts the app on http://localhost:9000

## API

The API exposes the following operations:

* Create transaction

```
curl -X POST -H "Content-Type: application/json" \
    -d '{"amount": 100, "card_last_4_digits": "4532", "date_time": "2021-01-09 16:28:25.700", "installments": 2, "card_type": "VISA", "user_id": 3}' \
    http://localhost:9000/transactions/process
```

* Get all transactions

```
curl -i -H "Accept: application/json" -H "Content-Type: application/json" -X GET http://localhost:9000/transactions
```

* Get by userId

```
curl -i -H "Accept: application/json" -H "Content-Type: application/json" -X GET http://localhost:9000/transactions/:userId
```

* Update transaction 