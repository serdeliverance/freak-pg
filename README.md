# freak-pg

`Freak PG` stands for `Freak Payment Gateway`, and as its name suggests, it is a POC that simulates a payment gateway. The idea behind this project is to server as an example of a full API implemented using `Play Framework` and as a starting point for new `Scala` programmers who want to learn how to build real world apps using these tools.

## High level components and architecture

* `Freak PG` our API built on `Play Framework`
* `Credit Card API` an API that authorize/reject transactions based on some rules. It is an app written in Node and is started automatically using the [docker-compose](docker-compose.yml) provided (we don't have to deal with this app directly).
* `Kafka` for sending notifications such as `transaction created` and `transaction updated`.
* `Database` for storing our transactions once them have been processed by the `Credit Card API`

The following diagram shows the system architecture:

![Alt text](fpg-app-diagram.png?raw=true "Title") 

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
    -d '{"amount": 100, "card_last_digits": "4532", "date_time": "2021-01-09 16:28:25.700", "installments": 2, "card_type": "VISA", "user_id": 3}' \
    http://localhost:9000/transactions
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

## Docker Image

To generate the Docker image, run:

```
sbt docker
```