# Specification: Ballerina Confluent Schema Registry Library

_Authors_: @Nuvindu \
_Reviewers_: @ThisaruGuruge \
_Created_: 2024/04/04 \
_Updated_: 2024/04/04 \
_Edition_: Swan Lake

## Introduction

The Ballerina Confluent Schema Registry module facilitates the integration with Confluent's Schema Registry. It offers capabilities to retrieve, register schemas from the registry.

The Confluent Schema Registry library specification has evolved and may continue to evolve in the future. The released versions of the specification can be found under the relevant GitHub tag.

For feedback or suggestions about the library, please initiate a discussion via a [GitHub issue](https://github.com/ballerina-platform/ballerina-library/issues) or in the [Discord server](https://discord.gg/ballerinalang). The specification and implementation can be updated based on the discussion's outcome. Community feedback is highly appreciated. Accepted proposals that affect the specification are stored under `/docs/proposals`. Proposals under discussion can be found with the label `type/proposal` in GitHub.

The conforming implementation of the specification is released and included in the distribution. Any deviation from the specification is considered a bug.

## Contents

1. [Overview](#1-overview)
2. [Initialize the schema registry client](#2-initialize-the-schema-registry-client)
    * 2.1 [The `init` method](#21-the-init-method)
3. [Fetch a schema](#3-fetch-a-schema)
    * 3.1 [The `getSchemaById` API](#31-the-getschemabyid-api)
4. [Schema registration](#4-schema-registration)
    * 4.1 [The `register` API](#41-the-register-api)
5. [The `cregistry:Error` Type](#6-the-cregistryerror-type)

## 1. Overview

This specification provides a detailed explanation of the functionalities offered by the Ballerina Confluent Schema Registry module. The module provides the following capabilities.

1. Register a schema
2. Fetch a schema
3. Fetch a schema ID

## 2. Initialize the schema registry client

The Schema Registry client needs to be initialized before performing the functionalities.

### 2.1 The `init` method

The `init` method initializes the Schema Registry client. It takes a `config` parameter, which contains the necessary configuration to connect to the Schema Registry. The method returns an error if the initialization fails.

```ballerina
configurable string baseUrl = ?;
configurable int identityMapCapacity = ?;
configurable map<anydata> originals = ?;
configurable map<string> headers = ?;

cregistry:Client registry = check new ({
    baseUrl,
    identityMapCapacity,
    originals,
    headers
});
```

## 3. Fetch a schema

The Confluent Schema Registry module allows fetching a schema from the registry.

### 3.1 The `getSchemaById` API

The `getSchemaById` method retrieves a schema from the registry using its ID.

```ballerina
string schema = check registry->getSchemaById(10001);
```

## 4. Schema registration

This section details the process of registering a schema to the registry.

### 4.1 The `register` API

The `register` method registers a new schema to the registry.

```ballerina
int schemaId = check registry->register("subject", "schema");
```

## 5. The `cregistry:Error` Type

The `cregistry:Error` type represents all the errors related to the Confluent Schema Registry module. This is a subtype of the Ballerina `error<ErrorDetails>` type.

```ballerina
public type ErrorDetails record {
    int status?;
    int errorCode?;
};
```

The `ErrorDetails` type is used to handle errors that may occur while interacting with the Confluent Schema Registry. The `status` field represents the HTTP status code of the error, and the `errorCode` field represents the specific error code returned by the Schema Registry.
