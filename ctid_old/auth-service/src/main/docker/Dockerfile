FROM 172.16.1.180:5000/java8dev:0.1


VOLUME /tmp

RUN mkdir /app
RUN mkdir /app/logs
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

ADD auth-service.jar /app/auth-service.jar
ADD runboot.sh /app/

RUN bash -c 'touch /app/auth-service.jar'

WORKDIR /app
RUN chmod a+x runboot.sh

CMD  /app/runboot.sh