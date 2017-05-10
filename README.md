# quartz-springboot-culster
Proof of concept for clustered Quartz using Spring Boot and MySQL

Based on ka4ok85/quartz-springboot-mysql

## How to run

1. Create a MySQL instance.
```
docker run -d --name mysql --net=host -e MYSQL_ROOT_PASSWORD=root mysql
```
2. Create "quartz" database and tables in MySQL.
```
docker exec -it mysql mysql -uroot -proot -e "$(curl https://raw.githubusercontent.com/linfan/quartz-springboot-cluster/master/db/quartz_tables_mysql.sql)"
```

3. Compile the code.
```
mvn clean package
```

4. Run two instance of demo app.
```
mvn spring-boot:run -Dspring.profiles.active=ins1
mvn spring-boot:run -Dspring.profiles.active=ins2
```
或
```
java -jar quartz-springboot-cluster-1.0.0-SNAPSHOT.jar --spring.profiles.active=ins1
java -jar quartz-springboot-cluster-1.0.0-SNAPSHOT.jar --spring.profiles.active=ins1
```

5. Open localhost:8080/api/start/{jobName}/{groupName}/{quantity}/{interval} in Browser (e.g. localhost:8080/api/start/job_1/group_1/10/5)

6. Confirm Service is called by Quartz. In console you should see "Hello World!" message(s)
