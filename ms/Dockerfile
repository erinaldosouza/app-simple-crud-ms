FROM aerospike/aerospike-server

# Copia o arquivo de configuração customizado
COPY aerospike.conf /etc/aerospike/aerospike.conf

# Expõe a porta do Aerospike
EXPOSE 3000

# Comando para iniciar o Aerospike
CMD ["/usr/bin/asd", "--config-file", "/etc/aerospike/aerospike.conf"]