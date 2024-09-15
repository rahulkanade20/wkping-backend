#!/bin/bash

if [[ -f .env ]]; then
	source .env
fi

docker login registry.gitlab.com -u oauth2 -p "$ACCESS_TOKEN"

docker stop wkping-frontend-app 2>/dev/null || true

docker rm wkping-frontend-app 2>/dev/null || true

docker compose down

docker pull registry.gitlab.com/monitoring-app1/wkping-backend:latest && docker-compose up -d

docker pull registry.gitlab.com/monitoring-app1/wkping-frontend:latest && docker run -d --name wkping-frontend-app -p 80:3000 registry.gitlab.com/monitoring-app1/wkping-frontend:latest