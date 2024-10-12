#!/bin/bash

if [[ -f .env ]]; then
	source .env
fi

docker login registry.gitlab.com -u oauth2 -p "$ACCESS_TOKEN"

docker compose down

docker pull registry.gitlab.com/monitoring-app1/wkping-backend:latest && docker-compose up -d