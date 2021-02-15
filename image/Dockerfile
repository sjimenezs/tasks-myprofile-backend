FROM ubuntu:focal
RUN apt-get update && apt-get install -y locales && rm -rf /var/lib/apt/lists/* \
    && localedef -i es_CO -c -f UTF-8 -A /usr/share/locale/locale.alias es_CO.UTF-8
ENV LANG es_CO.UTF-8
ENV LANGUAGE es_CO
RUN mkdir -p /app/server/
COPY server /app/server/bin
COPY schema /app/server/schema
COPY java /app/java
COPY start /app/server/bin/start.sh
COPY log.xml /app/server/bin/log.xml
RUN ["chmod", "+x", "/app/server/bin/start.sh"]
CMD [ "/app/server/bin/start.sh"]
EXPOSE 8080