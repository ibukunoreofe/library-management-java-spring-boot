
SET JVM HOME to the path installed
```shell
$ export JAVA_HOME="~\.jdks\openjdk-22.0.1"
```


### Running

```shell
./mvnw spring-boot:run
```

### Generate JWT Token
https://dev.to/tkirwa/generate-a-random-jwt-secret-key-39j4

```shell
$ node -e "console.log(require('crypto').randomBytes(32).toString('hex'))"
```
