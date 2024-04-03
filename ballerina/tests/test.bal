// Copyright (c) 2024, WSO2 LLC. (http://www.wso2.com)
//
// WSO2 LLC. licenses this file to you under the Apache License,
// Version 2.0 (the "License"); you may not use this file except
// in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.

import ballerina/test;

configurable string subject = ?;
configurable string baseUrl = ?;
configurable int identityMapCapacity = ?;
configurable map<anydata> originals = ?;
configurable map<string> headers = ?;

@test:Config {}
public isolated function testRegister() returns error? {
    ConnectionConfig ConnectionConfig = {
        baseUrl,
        identityMapCapacity,
        originals,
        headers
    };
    Client schemaRegistryClient = check new (ConnectionConfig);

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

    _ = check schemaRegistryClient.register(subject, schema);
}

@test:Config {}
public isolated function testInvalidSchemaRegister() returns error? {
    ConnectionConfig ConnectionConfig = {
        baseUrl,
        identityMapCapacity,
        originals,
        headers
    };
    Client schemaRegistryClient = check new (ConnectionConfig);

    string schema = string `
        {
            "type": "record",
            "namespace": "data"
        }`;

    int|Error register = schemaRegistryClient.register(subject, schema);
    test:assertTrue(register is error<ErrorDetails>);
    if register !is int {
        test:assertEquals(register.detail().status, ());
        test:assertEquals(register.detail().errorCode, ());
    } 
}

@test:Config {}
public isolated function testGetSchemaById() returns error? {
    ConnectionConfig ConnectionConfig = {
        baseUrl,
        identityMapCapacity,
        originals,
        headers
    };
    Client schemaRegistryClient = check new (ConnectionConfig);

    string schema = string `{"type":"record","name":"Student","namespace":"example.avro","fields":[{"name":"name","type":"string"},{"name":"favorite_color","type":["string","null"]}]}`;

    int registerResult = check schemaRegistryClient.register(subject, schema);
    string getSchema = check schemaRegistryClient.getById(registerResult);
    test:assertEquals(getSchema.toJson(), schema.toJson());
}

@test:Config {}
public isolated function testGetInvalidSchemaById() returns error? {
    ConnectionConfig ConnectionConfig = {
        baseUrl,
        identityMapCapacity,
        originals,
        headers
    };
    Client schemaRegistryClient = check new (ConnectionConfig);

    string schema = string `{"type":"record","name":"Student","namespace":"example.avro","fields":[{"name":"name","type":"string"},{"name":"favorite_color","type":["string","null"]}]}`;
    
    int registerResult = check schemaRegistryClient.register(subject, schema);
    string|error<ErrorDetails> getSchema = schemaRegistryClient.getById(registerResult * registerResult);
    test:assertTrue(getSchema is error<ErrorDetails>);
    if getSchema !is string {
        test:assertEquals(getSchema.detail().status, 404);
        test:assertEquals(getSchema.detail().errorCode, 40403);
    }
}

@test:Config {}
public isolated function testGetId() returns error? {
    ConnectionConfig ConnectionConfig = {
        baseUrl,
        identityMapCapacity,
        originals,
        headers
    };
    Client schemaRegistryClient = check new (ConnectionConfig);

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

    int registerId = check schemaRegistryClient.register(subject, schema);
    int getId = check schemaRegistryClient.getId(subject, schema);
    test:assertEquals(registerId, getId);
}

@test:Config {}
public isolated function testInvalidClientInitiation() returns error? {
    ConnectionConfig ConnectionConfig = {
        baseUrl: "",
        identityMapCapacity,
        originals,
        headers
    };
    Client schemaRegistryClient = check new (ConnectionConfig);

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

    int|Error registerResult = schemaRegistryClient.register(subject, schema);
    test:assertTrue(registerResult is Error);
}
