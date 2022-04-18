FROM ubuntu:20.04

RUN apt-get update && DEBIAN_FRONTEND=noninteractive apt-get install -y --no-install-recommends git openjdk-8-jdk
RUN apt-get install -y maven

RUN git clone https://github.com/hyajam/jabs.git
WORKDIR /jabs

RUN export JAVA_HOME="$(dirname $(dirname $(dirname $(readlink -f $(which java)))))" && mvn clean install
RUN apt-get clean && rm -rf /var/lib/apt/lists/*

CMD java -jar target/jabs-0.1.1.jar