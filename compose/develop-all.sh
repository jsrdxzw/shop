cp ../foodie-api/target/foodie-api-1.0-SNAPSHOT.jar ./api/
docker-compose down
docker rmi  foodie-api
docker-compose up -d