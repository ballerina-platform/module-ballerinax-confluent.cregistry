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

string subject = "new-subject";
string baseUrl = "http://localhost:8081";
string apiKey = "admin";
string apiSecret = "admin-secret";
int identityMapCapacity = 1000;
string sslBaseUrl = "https://localhost:8082";
string trustStorePath = "./tests/resources/certs/truststore.jks";
string trustStorePassword = "changeit";

Client schemaRegistryClient = check new ({
        baseUrl: sslBaseUrl,
        auth: { apiKey, apiSecret },
        identityMapCapacity,
        secureSocket: {
            enable: true,
            cert: { 
                path: trustStorePath, 
                password: trustStorePassword 
            },
            verifyHostName: false
        }
    });

@test:Config {}
public function testSslWithSchemaRegistry() returns error? {
    Client sslClient = check new ({
        baseUrl: sslBaseUrl,
        auth: { 
            apiKey, 
            apiSecret 
        },
        identityMapCapacity,
        secureSocket: {
            enable: true,
            cert: { path: trustStorePath, password: trustStorePassword },
            verifyHostName: false
        }
    });
    string subject = "test-ssl-topic";
    string schema = string `{"namespace":"example.avro","type":"record","name":"Student","fields":[{"name":"name","type":"string"},{"name":"favorite_color","type":["string","null"]}]}`;
    _ = check sslClient->register(subject, schema);
}

@test:Config {}
public function testAuthConfigs() returns error? {
    Client authClient = check new ({
        baseUrl,
        auth: {
            apiKey, 
            apiSecret
        },
        identityMapCapacity
    });
    
    string subject = "test-auth-topic";
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

    _ = check authClient->register(subject, schema);
}

@test:Config {}
public function testRegister() returns error? {
    string subject = "test-topic";
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

    _ = check schemaRegistryClient->register(subject, schema);
}

@test:Config {}
public function testInvalidSchemaRegister() returns error? {
    string schema = string `
        {
            "type": "record",
            "namespace": "data"
        }`;

    int|Error register = schemaRegistryClient->register(subject, schema);
    test:assertTrue(register is error<ErrorDetails>);
    if register !is int {
        test:assertEquals(register.detail().status, ());
        test:assertEquals(register.detail().errorCode, ());
    }
}

@test:Config {}
public function testGetSchemaById() returns error? {
    string schema = string `{"type":"record","name":"Student","namespace":"example.avro","fields":[{"name":"name","type":"string"},{"name":"favorite_color","type":["string","null"]}]}`;

    int registerResult = check schemaRegistryClient->register(subject, schema);
    string getSchema = check schemaRegistryClient->getSchemaById(registerResult);
    test:assertEquals(getSchema.toJson(), schema.toJson());
}

@test:Config {}
public function testGetInvalidSchemaById() returns error? {
    string schema = string `{"type":"record","name":"Student","namespace":"example.avro","fields":[{"name":"name","type":"string"},{"name":"favorite_color","type":["string","null"]}]}`;
    _ = check schemaRegistryClient->register(subject, schema);
    int invalidId = 999999;
    string|error<ErrorDetails> getSchema = schemaRegistryClient->getSchemaById(invalidId);
    test:assertTrue(getSchema is error<ErrorDetails>);
    if getSchema !is string {
        test:assertTrue(getSchema.detail().status is int);
    }
}

@test:Config {}
public function testInvalidClientInitiation() returns error? {
    ConnectionConfig ConnectionConfig = {
        baseUrl: "",
        identityMapCapacity,
        originals: {}
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

    int|Error registerResult = schemaRegistryClient->register(subject, schema);
    test:assertTrue(registerResult is Error);
}
