# quartz-springboot-culster
Proof of concept for clustered Quartz using Spring Boot and MySQL

Based on [ka4ok85/quartz-springboot-mysql](https://github.com/ka4ok85/quartz-springboot-mysql)

## How to run

1. Create a MySQL instance (E.g. using docker).
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
Or
```
java -jar quartz-springboot-cluster-1.0.0-SNAPSHOT.jar --spring.profiles.active=ins1
java -jar quartz-springboot-cluster-1.0.0-SNAPSHOT.jar --spring.profiles.active=ins2
```

5. Check the output of each instance, the jobs will be dispatch to each instance randomly.

- In console you should see many "Hello" message(s) with job name.
- When either of the instance down, all jobs will be switch to another one.
- When new instance join the cluster, jobs will be automatically re-distributed.

6. The demo also provide interface to add time-lapse job and to remove existing job.

- Visit `<ip>:<port>/api/start/{jobName}/{groupName}/{quantity}/{interval}` to dynamically add job.

E.g.
```
curl localhost:8001/api/start/job_1/group_1/10/5
```

- Visit `<ip>:<port>/api/start/{jobName}/{groupName}/{quantity}/{interval}` to remove existing job.

E.g.
```
curl localhost:8002/api/stop/job_1/group_1
```

> **Tip:** Be careful with the jobName and triggerName.
> If you define a new job using existing group-and-job-name, or define a new trigger using existing group-and-trigger-name, it would not take effect.