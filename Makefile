# Usuario ID y Grupo ID del host para evitar problemas de permisos con Docker
UID := $(shell id -u)
GID := $(shell id -g)

iniciar:
	@docker compose up -d --force-recreate
detener:
	@docker compose down
reiniciar-spring-boot:
	@docker restart spring-boot
logs:
	@docker logs -f spring-boot
consola:
	@docker exec -it spring-boot /bin/sh
cambiar-permisos:
	@sudo chown -R $(UID):$(GID) ./app/target/
