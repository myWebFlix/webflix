# WebFlix

## Database

```bash
docker run -d --name pg-video-metadata -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=video-metadata -p 5432:5432 postgres:13
```

## App
```bash
mvn clean package
docker build -t webflix .
docker run -d -p 8080:8080 webflix
```
