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

rb:
	cd src/code && sudo ./gradlew build
	docker compose restart --no-deps web1 web2 web3  web-mirror

clean:
	cd src/code && sudo ./gradlew clean