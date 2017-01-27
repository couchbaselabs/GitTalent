export revId=$(git rev-parse HEAD)
echo $revId

docker run --rm -it -v $(pwd)/gittalent-frontend:/project -u $(id -u):$(id -g) metal3d/ng build --environment=test
docker build -t gittalent/frontend:$revId gittalent-frontend

docker run -e revId=$revId -it --rm --name gittalent-couchbase --privileged -v /var/run/docker.sock:/var/run/docker.sock -v $(pwd)/gittalent-backend/:/usr/src/gittalent-couchbase -w /usr/src/gittalent-couchbase maven:3.3.9-jdk-8 mvn clean package docker:build -DskipTests

cd gittalent-integration-test;mvn clean install;
