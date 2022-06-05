FROM ubuntu:20.04

RUN apt-get update \
    && DEBIAN_FRONTEND=noninteractive apt-get install -y --no-install-recommends git openjdk-17-jdk \
    && DEBIAN_FRONTEND=noninteractive apt-get install -y --no-install-recommends maven \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

RUN git clone https://github.com/hyajam/jabs.git
WORKDIR /jabs

RUN export JAVA_HOME="$(dirname $(dirname $(dirname $(readlink -f $(which java)))))" && mvn clean install

CMD java -jar target/jabs-0.1.1.jar