## Package overview

[Confluent Schema Registry](https://docs.confluent.io/platform/current/schema-registry/) serves as a centralized repository for managing Avro schemas, ensuring data consistency and compatibility in serialization and deserialization processes.

The Ballerina Confluent Schema Registry connector integrates with Confluent's Avro Schema Registry, providing users to efficiently manage schemas within Ballerina applications.

## Quickstart

To use the Confluent schema registry connector in your Ballerina project, modify the `.bal` file as follows.

### Step 1: Import the module

Import the `ballerinax/confluent.cregistry` module into your Ballerina project.

```ballerina
import ballerinax/confluent.cregistry;
```

### Step 2: Instantiate a new connector

```ballerina
configurable string schemaRegistryUrl = ?;
configurable string apiKey = ?;
configurable string apiSecret = ?;
configurable string truststorePath = ?;
configurable string truststorePassword = ?;

cregistry:Client schemaRegistryClient = check new (
    baseUrl = schemaRegistryUrl,
    originals = {
      "basic.auth.credentials.source": "USER_INFO",
      "basic.auth.user.info": string `${apiKey}:${apiSecret}`
      // Truststore configurations are optional when the schema registry's HTTP(S) endpoint is secured with a publicly trusted certificate.
      "schema.registry.ssl.truststore.location": truststorePath,
      "schema.registry.ssl.truststore.password": truststorePassword,
    }
);
```

### Step 3: Invoke the connector operation

You can now utilize the operations available within the connector.

```ballerina
public function main() returns error? {
    string schema = string `
        {
            "type": "int",
            "name" : "value",
            "namespace": "data"
        }`;

    int registerResult = check schemaRegistryClient.register("subject-name", schema);
}
```

### Step 4: Run the Ballerina application

Use the following command to compile and run the Ballerina program.

```
bal run
```
