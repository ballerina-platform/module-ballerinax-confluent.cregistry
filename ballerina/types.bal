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

# Provides a set of configurations to control the behaviours when communicating with a schema registry.
#
# + baseUrl - The base URL of the schema registry endpoint
# + identityMapCapacity - Capacity of the schema ID map for a particular subject
# + originals - Connection configurations required to integrate with the schema registry
# + headers - Custom headers to be included in the requests
public type ConnectionConfig record {|
    string baseUrl;
    int identityMapCapacity = 1000;
    map<anydata> originals?;
    map<string> headers?;
|};
