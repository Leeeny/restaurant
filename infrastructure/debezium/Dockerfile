FROM debezium/connect:2.7.3.Final

USER root

RUN microdnf install -y unzip curl \
    && curl -L -o /tmp/avro-converter.zip \
       "https://hub-downloads.confluent.io/api/plugins/confluentinc/kafka-connect-avro-converter/versions/7.6.1/confluentinc-kafka-connect-avro-converter-7.6.1.zip" \
    && unzip /tmp/avro-converter.zip -d /tmp/avro-converter \
    && mkdir -p /kafka/connect/kafka-connect-avro-converter \
    && cp /tmp/avro-converter/confluentinc-kafka-connect-avro-converter-7.6.1/lib/*.jar /kafka/connect/kafka-connect-avro-converter/ \
    && rm -rf /tmp/avro-converter /tmp/avro-converter.zip

USER 1001