.PHONY: up
.PHONY: down
.PHONY: nr
.PHONY: rb
.PHONY: clean

up:
	docker-compose up -d

down:
	docker-compose down

nr:
	docker compose exec -d nginx nginx -s reload

build:
	cd src/code && sudo ./gradlew build

rb: build
	docker compose restart --no-deps web1 web2 web3  web-mirror

rb1: build
	docker compose restart --no-deps web

curl:
	docker compose restart --no-deps curl

clean:
	cd src/code && sudo ./gradlew clean