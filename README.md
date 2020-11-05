# WebFlix

## Database

```
docker run -d --name pg-video-metadata -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=video-metadata -p 5432:5432 postgres:13
```
