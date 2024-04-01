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

configurable string baseUrl = ?;
configurable int identityMapCapacity = ?;
configurable map<json> originals = ?;
configurable map<string> headers = ?;

@test:Config{
    groups: ["test"]
}
public isolated function testRegister() returns error? {
    SchemaRegistryClientConfig schemaRegistryClientConfig = {
        baseUrl,
        identityMapCapacity,
        originals,
        headers
    };

    SchemaRegistryClient schemaRegistryClient = check new (schemaRegistryClientConfig);

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

    int|error registerResult = schemaRegistryClient.register("avro-topic", schema);
    test:assertTrue(registerResult !is error);
}

@test:Config{}
public isolated function testGetSchema() returns error? {
    SchemaRegistryClientConfig schemaRegistryClientConfig = {
        baseUrl,
        identityMapCapacity,
        originals,
        headers
    };
    SchemaRegistryClient schemaRegistryClient = check new (schemaRegistryClientConfig);
    
    string|error getSchema = schemaRegistryClient.getById(100003);
    test:assertTrue(getSchema !is error);
}

@test:Config{}
public isolated function testIdToBytes() returns error? {

    SchemaRegistryClientConfig schemaRegistryClientConfig = {
        baseUrl,
        identityMapCapacity,
        originals,
        headers
    };
    SchemaRegistryClient schemaRegistryClient = check new (schemaRegistryClientConfig);
    byte[]|error bytes = schemaRegistryClient.idToBytes(100003);
    test:assertTrue(bytes is byte[]);
}
