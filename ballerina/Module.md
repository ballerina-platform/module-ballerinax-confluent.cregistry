## Overview

[Schema Registry](https://docs.confluent.io/platform/current/schema-registry/) serves as a centralized repository for managing Avro schemas, ensuring data consistency and compatibility in serialization and deserialization processes.

The Ballerina Schema Registry connector integrates with Confluent's Avro Schema Registry, providing users to efficiently manage schemas within Ballerina applications.

## Quickstart

To use the Confluent schema registry connector in your Ballerina project, modify the `.bal` file as follows.

### Step 1: Import the module

Import the `ballerinax/confluent.regclient` module into your Ballerina project.

```ballerina
import ballerinax/confluent.regclient;
```

### Step 2: Instantiate a new connector

```ballerina
    SchemaRegistryClient schemaRegistryClient = check new ({
        baseUrl,
        identityMapCapacity,
        originals,
        headers
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
