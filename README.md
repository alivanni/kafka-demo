# Metrics collector with Kafka Producer-Consumer

## Prerequisites

- JRE 1.8
- Maven

## Metrics

There are 2 simple metrics: CPU usage and Free Memory. Metrics scraping is implemented as periodic tasks running every 10 seconds.

## Usage

Build service by navigating to project root folder (one containing pom.xml file) and executing:

```
$ mvn clean install
```
This will generate "fat jar" with all the dependencies included. We use single codebase / project for both Producer and Consumer.
There are 2 main classes, so proper one can be supplied as run command argument.


To run Producer use:

```
$ java -cp target/KafkaProducerConsumer-1.0-SNAPSHOT-jar-with-dependencies.jar com.demokafka.producer.Producer
```

To run Consumer use:

```
$ java -cp target/KafkaProducerConsumer-1.0-SNAPSHOT-jar-with-dependencies.jar com.demokafka.consumer.Consumer
```

Now, when you have all the components up and running, whole the flow should start working. Metrics should be scraped periodically, metrics events sent to Kafka by producer, read on other side by consumer, and finally put to Postgre DB.
For convenience, events are also duplicated as INFO log entries.


### Testing

Execute tests using `test` command:

```
$ mvn test
```