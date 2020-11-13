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

Using Docker network:

```
docker network create webflix
docker run -d --name pg-video-metadata -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=video-metadata -p 5432:5432 --network webflix postgres:13
docker run -d -p 8080:8080 --network webflix -e KUMULUZEE_DATASOURCES0_CONNECTIONURL=jdbc:postgresql://pg-video-metadata:5432/video-metadata webflix
```

## etcd

```
docker run -p 2379:2379 --name etcd --volume=/tmp/etcd-data:/etcd-data quay.io/coreos/etcd:latest /usr/local/bin/etcd --name my-etcd-1 --data-dir /etcd-data --listen-client-urls http://0.0.0.0:2379 --advertise-client-urls http://0.0.0.0:2379 --listen-peer-urls http://0.0.0.0:2380 --initial-advertise-peer-urls http://0.0.0.0:2380 --initial-cluster my-etcd-1=http://0.0.0.0:2380 --initial-cluster-token my-etcd-token --initial-cluster-state new --auto-compaction-retention 1 -cors="*"
```

Changing values:

```
docker exec etcd etcdctl --endpoints http://0.0.0.0:2379 set /environments/dev/services/webflix-service/1.0.0/config/rest-config/maintenance-mode true
```
