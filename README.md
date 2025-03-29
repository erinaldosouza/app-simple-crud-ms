# app-simple-crud-ms

podman build -t aerospike-custom .
podman run -d -p 3000:3000 localhost/aerospike-custom

podman run -tid --name aerospike -e "NAMESPACE=device-match" -p 3000-3002:3000-3002 aerospike/aerospike-server
