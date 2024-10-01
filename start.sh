#!/bin/bash

if [[ -f .env ]]; then
	source .env
fi

docker login registry.gitlab.com -u oauth2 -p "$ACCESS_TOKEN"

docker stop wkping-frontend-app 2>/dev/null || true

docker rm wkping-frontend-app 2>/dev/null || true

docker compose down

docker pull registry.gitlab.com/monitoring-app1/wkping-backend:latest && IP_ADDRESS=127.0.0.1 docker-compose up -d

docker pull registry.gitlab.com/monitoring-app1/wkping-frontend:latest && docker run -d --name wkping-frontend-app -e IP_ADDRESS=127.0.0.1 -p 3000:3000 registry.gitlab.com/monitoring-app1/wkping-frontend:latest