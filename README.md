#Run docker
docker start

#Run docker-compose file (cd on project folder)
docker-compose up / docker-compose up -d / docker-compose up -d --force-recreate

#If micros are down because dependency on cofig server or eureka server
docker-compose start

#Kill containers
docker-compose down

#View images
docker images ls

#View containers
docker ps -a

#View logs for a container
docker logs <container-id> / docker logs <container-name>

