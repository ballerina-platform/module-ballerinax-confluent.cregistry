// Copyright (c) 2024, WSO2 LLC. (http://www.wso2.com) All Rights Reserved.
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

import ballerina/http;

type Schema readonly & record {|
    int id;
    string schema;
|};

service /registry on new http:Listener(9090) {
    resource function post subjects/[string subject]/versions() returns http:Response|error {
        http:Response res = new;
        _ = check res.setContentType("application/vnd.schemaregistry.v1+json");
        res.setPayload({"id": 1002});
        return res;
    }

    resource function get schemas/ids/[int id]() returns http:Response|error {
        http:Response res = new;
        res.statusCode = 404;
        _ = check res.setContentType("application/vnd.schemaregistry.v1+json");
        string message = string `Schema ${id} not found`;
        res.setPayload({"error_code":40403,"message": message});
        return res;
    }
}
