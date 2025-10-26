## Examples

The Ballerina Confluent Schema Registry connector provides practical examples illustrating usage in various scenarios. Explore these [examples](https://github.com/ballerina-platform/module-ballerinax-confluent.cregistry/tree/main/examples).

1. [User registration](https://github.com/ballerina-platform/module-ballerinax-confluent.cregistry/tree/main/examples/user-registration)
    This example shows how to use Confluent Schema Registry APIs to register schemas and retrieve the corresponding ID of schemas.

## Prerequisites

Create a `Config.toml` file with the base URL, schema capacity, subject, connection configurations and header values. Here's an example of how your `Config.toml` file should look:

    ```toml
    baseUrl = <BASE_URL>
    identityMapCapacity = <SCHEMA_MAP_CAPACITY>
    subject = <SCHEMA_REGISTRY_TOPIC>

    [auth]
    apiKey = <API-KEY>
    apiSecret = <API-SECRET>
    ```

## Running an Example

Execute the following commands to build an example from the source:

* To build an example:

    ```bash
    bal build
    ```

* To run an example:

    ```bash
    bal run
    ```

## Building the Examples with the Local Module

**Warning**: Due to the absence of support for reading local repositories for single Ballerina files, the Bala of the module is manually written to the central repository as a workaround. Consequently, the bash script may modify your local Ballerina repositories.

Execute the following commands to build all the examples against the changes you have made to the module locally:

* To build all the examples:

    ```bash
    ./build.sh build
    ```

* To run all the examples:

    ```bash
    ./build.sh run
    ```
