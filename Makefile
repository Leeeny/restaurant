# List of services, each has its own gradle wrapper
SERVICES := menu-service orders-service reviews-service menu-aggregate-service

COMPOSE_FILE ?= docker-compose.yml

# --- Cross-platform (Windows cmd vs bash) ---
ifeq ($(OS),Windows_NT)
    SHELL := cmd.exe
    .SHELLFLAGS := /C
    GRADLEW := gradlew.bat
    CD_AND := &&
else
    SHELL := /bin/bash
    GRADLEW := ./gradlew
    CD_AND := &&
endif

.PHONY: help build test clean docker-build compose-up compose-down compose-restart compose-logs \
        $(addprefix build-,$(SERVICES)) \
        $(addprefix test-,$(SERVICES)) \
        $(addprefix clean-,$(SERVICES)) \
        $(addprefix docker-build-,$(SERVICES))

help: ## Show list of commands
	@echo build                 - build all services
	@echo build-SERVICE         - build one service (e.g. build-menu-service)
	@echo test                  - run tests in all services
	@echo test-SERVICE          - run tests in one service
	@echo docker-build          - bootBuildImage for all services
	@echo docker-build-SERVICE  - bootBuildImage for one service
	@echo compose-up            - build images and start docker compose
	@echo compose-down          - stop and remove containers
	@echo compose-restart       - restart the stack
	@echo compose-logs          - follow logs of all containers
	@echo clean                 - gradlew clean in all services
	@echo clean-SERVICE         - gradlew clean in one service

## ---------- Build ----------

build: $(addprefix build-,$(SERVICES)) ## Build all services

$(addprefix build-,$(SERVICES)): build-%:
	@echo ">> building $*"
	cd $* $(CD_AND) $(GRADLEW) build -x test

## ---------- Tests ----------

test: $(addprefix test-,$(SERVICES)) ## Run tests in all services

$(addprefix test-,$(SERVICES)): test-%:
	@echo ">> testing $*"
	cd $* $(CD_AND) $(GRADLEW) test

## ---------- Docker images (bootBuildImage) ----------

docker-build: $(addprefix docker-build-,$(SERVICES)) ## Build docker images for all services via bootBuildImage

$(addprefix docker-build-,$(SERVICES)): docker-build-%:
	@echo ">> bootBuildImage for $*"
	cd $* $(CD_AND) $(GRADLEW) bootBuildImage

## ---------- Docker Compose ----------

compose-up: docker-build ## Build images and start the whole stack
	docker compose -f $(COMPOSE_FILE) up -d

compose-down: ## Stop and remove containers
	docker compose -f $(COMPOSE_FILE) down

compose-restart: compose-down compose-up ## Restart the stack

compose-logs: ## Logs of all containers (follow)
	docker compose -f $(COMPOSE_FILE) logs -f

## ---------- Clean ----------

clean: $(addprefix clean-,$(SERVICES)) ## Clean build dirs of all services

$(addprefix clean-,$(SERVICES)): clean-%:
	@echo ">> cleaning $*"
	cd $* $(CD_AND) $(GRADLEW) clean