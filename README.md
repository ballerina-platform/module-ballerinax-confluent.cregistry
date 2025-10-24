# Ballerina Confluent Schema Registry Connector

[![Build](https://github.com/ballerina-platform/module-ballerinax-confluent.cregistry/actions/workflows/ci.yml/badge.svg)](https://github.com/ballerina-platform/module-ballerinax-confluent.cregistry/actions/workflows/ci.yml)
[![Trivy](https://github.com/ballerina-platform/module-ballerinax-confluent.cregistry/actions/workflows/trivy-scan.yml/badge.svg)](https://github.com/ballerina-platform/module-ballerinax-confluent.cregistry/actions/workflows/trivy-scan.yml)
[![GraalVM Check](https://github.com/ballerina-platform/module-ballerinax-confluent.cregistry/actions/workflows/build-with-bal-test-graalvm.yml/badge.svg)](https://github.com/ballerina-platform/module-ballerinax-confluent.cregistry/actions/workflows/build-with-bal-test-graalvm.yml)
[![GitHub Last Commit](https://img.shields.io/github/last-commit/ballerina-platform/module-ballerinax-confluent.cregistry.svg)](https://github.com/ballerina-platform/module-ballerinax-confluent.cregistry/commits/main)
[![GitHub Issues](https://img.shields.io/github/issues/ballerina-platform/ballerina-library/module/confluent.cregistry.svg?label=Open%20Issues)](https://github.com/ballerina-platform/ballerina-library/labels/module%2Fconfluent.cregistry)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

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

Config.toml file
``` toml
baseUrl = "<SCHEMA_REGISTRY_URL>"
identityMapCapacity = <IDENTITY_MAP_CAPACITY>

[originals]
"basic.auth.credentials.source"="USER_INFO"
"basic.auth.user.info"="<API_KEY>:<API_SECRET>"
"bootstrap.servers"="<BOOTSTRAP_SERVERS>"

[headers]
"X-Schema-Provider"="<VALUE1>"
"User-Agent"="<VALUE2>"
```

```ballerina
configurable string baseUrl = ?;
configurable int identityMapCapacity = ?;
configurable map<anydata> originals = ?;
configurable map<string> headers = ?;

cregistry:Client schemaRegistryClient = check new ({
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

## Issues and projects

The **Issues** and **Projects** tabs are disabled for this repository as this is part of the Ballerina library. To report bugs, request new features, start new discussions, view project boards, etc., visit the Ballerina library [parent repository](https://github.com/ballerina-platform/ballerina-library).

This repository only contains the source code for the package.

## Building from the source

### Prerequisites

1. Download and install Java SE Development Kit (JDK) version 21. You can download it from either of the following sources:

   - [Oracle JDK](https://www.oracle.com/java/technologies/downloads/)
   - [OpenJDK](https://adoptium.net/)

    > **Note:** After installation, remember to set the `JAVA_HOME` environment variable to the directory where JDK was installed.

2. Download and install [Ballerina Swan Lake](https://ballerina.io/).

3. Download and install [Docker](https://www.docker.com/get-started).

    > **Note**: Ensure that the Docker daemon is running before executing any tests.

4. Generate a Github access token with read package permissions, then set the following `env` variables:

    ```bash
   export packageUser=<Your GitHub Username>
   export packagePAT=<GitHub Personal Access Token>
    ```

### Build options

Execute the commands below to build from the source.

1. To build the package:

   ```bash
   ./gradlew clean build
   ```

2. To run the tests:

   ```bash
   ./gradlew clean test
   ```

3. To build the without the tests:

   ```bash
   ./gradlew clean build -x test
   ```

4. To debug package with a remote debugger:

   ```bash
   ./gradlew clean build -Pdebug=<port>
   ```

5. To debug with Ballerina language:

   ```bash
   ./gradlew clean build -PbalJavaDebug=<port>
   ```

6. Publish the generated artifacts to the local Ballerina central repository:

   ```bash
   ./gradlew clean build -PpublishToLocalCentral=true
   ```

7. Publish the generated artifacts to the Ballerina central repository:

   ```bash
   ./gradlew clean build -PpublishToCentral=true
   ```

## Examples

1. Register a schema in the schema registry and retrieve it by ID:

   ```ballerina
   import ballerinax/confluent.cregistry;

   configurable string baseUrl = ?;
   configurable int identityMapCapacity = ?;
   configurable map<anydata> & readonly originals = ?;
   configurable map<string> & readonly headers = ?;

   public function main() returns error? {
      cregistry:Client schemaRegistryClient = check new (
         baseUrl = baseUrl,
         identityMapCapacity = identityMapCapacity,
         originals = originals,
         headers = headers
      );
      string schema = string `
         {
               "namespace": "example.avro",
               "type": "record",
               "name": "Student",
               "fields": [
                  {"name": "name", "type": "string"},
                  {"name": "favorite_color", "type": ["string", "null"]}
               ]
         }`;
      int schemaID = check schemaRegistryClient->register("student", schema);

      string schemaResp = check schemaRegistryClient->getSchemaById(schemaID);
   }
   ```

2. Producing Avro Messages to Kafka with Schema Registry:

   ``` ballerina
   import ballerinax/kafka;

   type Student record {
      string name;
      string favorite_color;
   };

   public function main() returns error? {
      string valueSchema = string `{
               "namespace": "example.avro",
               "type": "record",
               "name": "Student",
               "fields": [
                  {"name": "name", "type": "string"},
                  {"name": "favorite_color", "type": ["string", "null"]}
               ]
         }`;
      string keySchema = string `{    
               "type": "string",
               "name" : "stringValue", 
               "namespace": "data"
         }`;
      kafka:ProducerConfiguration config = {
         compressionType: kafka:COMPRESSION_SNAPPY,
         auth: {
               username: "<USERNAME>", 
               password: "<PASSWORD>"
         },
         securityProtocol: kafka:PROTOCOL_SASL_SSL,
         keySerializerType: kafka:SER_AVRO,
         valueSerializerType: kafka:SER_AVRO,
         keySchema: keySchema,
         valueSchema: valueSchema,
         schemaRegistryConfig: {
               "baseUrl":  baseUrl,
               "originals": originals,
               "headers": headers
         }
      };
      kafka:Producer producer = check new ("<BOOTSTRAP_SERVERS>", config);

      Student student = {
         name: "John Doe",
         favorite_color: "Red"
      };
      check producer->send({
         topic: "students",
         value: student
      });
   }
   ```

## Contributing to Ballerina

As an open source project, Ballerina welcomes contributions from the community.

For more information, go to the [contribution guidelines](https://github.com/ballerina-platform/ballerina-lang/blob/master/CONTRIBUTING.md).

## Code of conduct

All contributors are encouraged to read the [Ballerina Code of Conduct](https://ballerina.io/code-of-conduct).

## Useful links

- Discuss code changes of the Ballerina project in [ballerina-dev@googlegroups.com](mailto:ballerina-dev@googlegroups.com).
- Chat live with us via our [Discord server](https://discord.gg/ballerinalang).
- Post all technical questions on Stack Overflow with the [#ballerina](https://stackoverflow.com/questions/tagged/ballerina) tag.
