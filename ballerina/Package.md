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
configurable string baseUrl = ?;
configurable int identityMapCapacity = ?;
configurable cregistry:AuthConfig auth = ?;
configurable cregistry:SecureSocket secureSocket = ?;

cregistry:Client schemaRegistryClient = check new ({
    baseUrl,
    identityMapCapacity,
    auth,
    secureSocket
});
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

```bash
bal run
```

## Examples

The Ballerina Confluent Schema Registry connector provides practical examples illustrating usage in various scenarios. Explore these [examples](https://github.com/ballerina-platform/module-ballerinax-confluent.cregistry/tree/main/examples).

1. [User registration](https://github.com/ballerina-platform/module-ballerinax-confluent.cregistry/tree/main/examples/user-registration)
    This example shows how to use Confluent Schema Registry APIs to register schemas and retrieve the corresponding ID of schemas.
