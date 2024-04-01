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

import ballerina/jballerina.java;

# Consists of APIs to integrate with Avro Schema Registry.
public isolated class SchemaRegistryClient {

    private handle schemaRegistryClient;

    public isolated function init(SchemaRegistryClientConfig regclient) returns error? {
        self.schemaRegistryClient = generateSchemaRegistryClient(regclient);
    }

    public isolated function register(string subject, string schema) returns int|error {
        lock {
            return register(self.schemaRegistryClient, subject, schema);
        }
    }

    public isolated function getById(int id) returns string|error {
        lock {
            return getById(self.schemaRegistryClient, id);
        }
    }

    public isolated function idToBytes(int id) returns byte[]|error {
        lock {
            return idToBytes(self.schemaRegistryClient, id);
        }
    }
}

isolated function generateSchemaRegistryClient(SchemaRegistryClientConfig regclient) returns handle = @java:Constructor {
    'class: "io.ballerina.lib.confluent.CustomSchemaRegistryClient"
} external;

isolated function register(handle schemaRegistryClient, string subject, string schema) returns int|error = @java:Method {
    'class: "io.ballerina.lib.confluent.CustomSchemaRegistryClient"
} external;

isolated function getById(handle schemaRegistryClient, int id) returns string|error = @java:Method {
    'class: "io.ballerina.lib.confluent.CustomSchemaRegistryClient"
} external;

isolated function idToBytes(handle schemaRegistryClient, int id) returns byte[]|error = @java:Method {
    'class: "io.ballerina.lib.confluent.CustomSchemaRegistryClient"
} external;
