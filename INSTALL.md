# Install gittalent on centos

# install and configure nginx to serve the angular2 front 
yum install nginx

vim /etc/nginx/nginx.conf

````
[root@ip-10-0-137-46 ~]# cat /etc/nginx/nginx.conf
# For more information on configuration, see:
#   * Official English Documentation: http://nginx.org/en/docs/
#   * Official Russian Documentation: http://nginx.org/ru/docs/

user nginx;
worker_processes auto;
error_log /var/log/nginx/error.log;
pid /run/nginx.pid;

# Load dynamic modules. See /usr/share/nginx/README.dynamic.
include /usr/share/nginx/modules/*.conf;

events {
    worker_connections 1024;
}

http {
    log_format  main  '$remote_addr - $remote_user [$time_local] "$request" '
                      '$status $body_bytes_sent "$http_referer" '
                      '"$http_user_agent" "$http_x_forwarded_for"';

    access_log  /var/log/nginx/access.log  main;

    sendfile            on;
    tcp_nopush          on;
    tcp_nodelay         on;
    keepalive_timeout   65;
    types_hash_max_size 2048;

    include             /etc/nginx/mime.types;
    default_type        application/octet-stream;

    include /etc/nginx/conf.d/*.conf;

    server {
        listen       80 default_server;
        listen       [::]:80 default_server;
        server_name  _;
        root         /usr/share/nginx/html;



    ##
    # Gzip Settings
    ##
    gzip on;
    gzip_http_version 1.1;
    gzip_disable      "MSIE [1-6]\.";
    gzip_min_length   256;
    gzip_vary         on;
    gzip_proxied      expired no-cache no-store private auth;
    gzip_types        application/hal+json text/plain text/css application/json application/javascript application/x-javascript text/xml application/xml application/xml+rss text/javascript;
    gzip_comp_level 9;

        location / {
              try_files $uri$args $uri$args/ $uri/ /index.html =404; # /adm/index.html and not /index.htm
        }

   	 #Static File Caching. All static files with the following extension will be cached for 1 day
    	location ~* .(jpg|jpeg|png|gif|ico|css|js|svg)$ {     
		expires 7d;
		add_header Cache-Control public;
    	}
   }

}
````



# Create gittalent user

`adduser gittalent`

# Create systemd service for gittalent spring boot app

vim /ets/systemd/system/gittalent.service 

````
[Unit]
Description=gittalent
After=syslog.target

[Service]
User=gittalent
ExecStart=/var/opt/gittalent/gittalent.jar
SuccessExitStatus=143

[Install]
WantedBy=multi-user.target
````

# Enable service

`systemctl gittalent status`

# create app directory and give permission to user 

````
mkdir /var/opt/gittalent
chown gittalent /var/opt/gittalent
chgrp gittalent /var/opt/gittalent
````

# Setup backend configuration

````
su - gittalent

vim /var/opt/gittalent/application.properties

spring.couchbase.bootstrap-hosts=ec2-52-4-12-105.compute-1.amazonaws.com,ec2-52-1-86-211.compute-1.amazonaws.com,ec2-54-82-132-50.compute-1.amazonaws.com
server.port=8080
gittalent.cors.allowedOrigin=http://ec2-52-20-157-174.compute-1.amazonaws.com
spring.data.couchbase.consistency=eventually_consistent
server.compression.enabled=true
server.compression.mime-types=application/json,application/hal+json,application/xml,text/html,text/xml,text/plain

vim /home/gittalent/.github

login=githubLogin
password=githubPassword
````





