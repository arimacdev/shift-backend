FROM i386/openjdk:8-jdk-alpine

RUN apk add --no-cache tzdata && \
	cp /usr/share/zoneinfo/Asia/Colombo /etc/localtime && \
	echo "Asia/Colombo" > /etc/timezone && apk del tzdata

ADD target/*.jar pmtool-backend.jar
EXPOSE 8080

ENTRYPOINT ["java","-jar","pmtool-backend.jar"]
