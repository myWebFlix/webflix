# WebFlix

## Database

```bash
docker run -d --name pg-webflix -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=webflix -p 5432:5432 postgres:13
```

## App

```bash
mvn clean package
docker build -t webflix .
docker run -d -p 8080:8080 webflix
```

Using Docker network:

```bash
docker network create webflix
docker run -d --name pg-webflix -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=webflix -p 5432:5432 --network webflix postgres:13
docker run -d -p 8080:8080 --network webflix -e KUMULUZEE_DATASOURCES0_CONNECTIONURL=jdbc:postgresql://pg-webflix:5432/webflix webflix
```

## etcd

```bash
docker run -p 2379:2379 --name etcd --volume=/tmp/etcd-data:/etcd-data quay.io/coreos/etcd:latest /usr/local/bin/etcd --name my-etcd-1 --data-dir /etcd-data --listen-client-urls http://0.0.0.0:2379 --advertise-client-urls http://0.0.0.0:2379 --listen-peer-urls http://0.0.0.0:2380 --initial-advertise-peer-urls http://0.0.0.0:2380 --initial-cluster my-etcd-1=http://0.0.0.0:2380 --initial-cluster-token my-etcd-token --initial-cluster-state new --auto-compaction-retention 1 -cors="*"
```

Changing values:

```bash
docker exec etcd etcdctl --endpoints http://0.0.0.0:2379 set /environments/dev/services/webflix-service/1.0.0/config/rest-config/maintenance-mode true
```

## Kubernetes

Creating/applying configuration:

```bash
kubectl create -f k8s/webflix-deployment.yaml
kubectl apply -f k8s/webflix-deployment.yaml
```

Creating/deleting a secret:

```bash
kubectl create secret generic MY_SECRET --from-literal=username=MY_USERNAME --from-literal=password=MY_PASSWORD --from-literal=url=jdbc:postgresql://MY_URL:5432/video-metadata
kubectl delete secret MY_SECRET
```

Getting IP/logs and restarting pods:

```bash
kubectl get services
kubectl get pods
kubectl logs MY_POD
kubectl delete pod MY_POD
```

## Logging 

```
marker.name: ENTRY or marker.name: EXIT
```