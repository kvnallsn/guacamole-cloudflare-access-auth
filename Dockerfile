FROM guacamole/guacamole:1.5.2
COPY docker/start.sh /opt/guacamole/bin/start.sh
COPY target/*.jar /opt/guacamole/cloudflare/
