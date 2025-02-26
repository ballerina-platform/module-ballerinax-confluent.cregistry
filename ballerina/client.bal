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
public isolated client class Client {

    # Gets invoked to initialize the `connector`.
    #
    # + config - The configurations to be used when initializing the `connector`
    # + return - An `cregistry:Error` if connector initialization failed
    public isolated function init(*ConnectionConfig config) returns error? {
        self.generateSchemaRegistryClient(config);
    }

    private isolated function generateSchemaRegistryClient(ConnectionConfig cregistry) = @java:Method {
        'class: "io.ballerina.lib.confluent.registry.CustomSchemaRegistryClient"
    } external;

    # Registers a schema with the schema registry.
    #
    # + subject - The subject under which the schema should be registered
    # + schema - The Avro schema to be registered
    # + return - The ID of the registered schema, or an `cregistry:Error` if registration fails
    remote isolated function register(string subject, string schema) returns int|Error = @java:Method {
        'class: "io.ballerina.lib.confluent.registry.CustomSchemaRegistryClient"
    } external;

    # Retrieves a schema from the schema registry by its ID.
    # 
    # + id - The ID of the schema to retrieve
    # + return - The retrieved schema or an `cregistry:Error` if the schema does not exist
    remote isolated function getSchemaById(int id) returns string|Error = @java:Method {
        'class: "io.ballerina.lib.confluent.registry.CustomSchemaRegistryClient"
    } external;
}
