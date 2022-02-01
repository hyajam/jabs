# Just Another Blockchain Simulator

![GitHub](https://img.shields.io/github/license/hyajam/jabs)
![Build Status](https://travis-ci.com/hyajam/jabs.svg?branch=objectifiedNetworkAndSimulator)
![Twitter URL](https://img.shields.io/twitter/url?style=social&url=https%3A%2F%2Ftwitter.com%2Fhabibyajam)

![Alt-Text](https://raw.githubusercontent.com/hyajam/jabs/objectifiedNetworkAndSimulator/img/Jabs-logo.png)

JABS - Just Another Blockchain Simulator.

JABS is a blockchain network simulator aimed at researching consensus algorithms for performance and security. it is designed to easily handel simulation of networks as large as normal public blockchain networks (~10000 nodes) in reasonable time.
  
  
## Installation
First, you should have ```git```,  ```openjdk-8-jdk``` and ```maven``` installed.
```shell script
$ sudo apt install git openjdk-8-jdk maven
```
After that, clone the git repository.
```shell script
$ git clone https://github.com/hyajam/jabs.git
```
Use ```maven``` to make a jar file.
```shell script
$ cd jabs
$ mvn clean install
```
You might need to set ```JAVA_HOME``` to java 8.0 home directory:
```shell
$ JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64/; mvn clean install
```
Then to run the compiled jar file.
```shell script
$ java -jar target/jabs-<version>.jar
```

## Usage
Currently, you can use JABS by editing Main.java file and creating new scenarios for your tests.


### Supported Consensus algorithms
Currently, JABS support the following consensus algorithms: 
 1. Nakamoto Consensus
 2. Ghost protocol
 3. PBFT
 4. Casper FFG
 6. DAGsper


## Other Blockchain Simulators ##
 1. Bitcoin Simulator: developed in C++ and based on NS3. Can simulate Bitcoin, Litecoin, Dogecoin and probably other blockchain networks based on Nakamoto Consensus. Although it is probably the most accurate of the blockchain simulators it is fairly slow. Does not simulate transactions. (https://github.com/arthurgervais/Bitcoin-Simulator)
 2. BlockSim: developed in Python. Supports simulating both Bitcoin and Ethereum. Accurate but slow. Simulates transactions and connection handshakes. (https://github.com/carlosfaria94/blocksim)
 3. SimBlock: developed in Java. In current version supports only Nakamoto Consensus. Plans on adding GHOST protocol. Fast. Does not simulate transactions. SimBlock partially inspired this project. (https://github.com/dsg-titech/simblock)
