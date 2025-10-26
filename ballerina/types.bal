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

import ballerina/crypto;
import ballerina/http;

# Provides a set of configurations to control the behaviours when communicating with a schema registry.
#
# + baseUrl - The base URL of the schema registry endpoint
# + identityMapCapacity - Capacity of the schema ID map for a particular subject
# + auth - Authentication configuration for the schema registry
# + secureSocket - SSL/TLS configuration for secure connections
# + originals - Connection configurations required to integrate with the schema registry
# + headers - Custom headers to be included in the requests
public type ConnectionConfig record {|
    string baseUrl;
    int identityMapCapacity = 1000;
    AuthConfig auth?;
    SecureSocket secureSocket?;
    map<string> headers?;
    map<anydata> originals?;
|};

# SSL/TLS configuration for secure connections
#
# + enable - Enable/disable SSL (default: true)
# + cert - Configuration associated with the trust store or path to certificate file
# + key - Configurations associated with the key store
# + protocol - SSL/TLS protocol configuration
# + ciphers - List of allowed cipher suites
# + verifyHostName - Enable/disable hostname verification (default: true)
public type SecureSocket record {|
    boolean enable = true;
    crypto:TrustStore|string cert?;
    crypto:KeyStore key?;
    record {|
        http:Protocol name;
        string[] versions = [];
    |} protocol?;
    string[] ciphers?;
    boolean verifyHostName = true;
|};

# Authentication configurations for schema registry
public type AuthConfig CredentialsConfig;

# API credentials based authentication
#
# + apiKey - The API key for authentication
# + apiSecret - The API secret for authentication
public type CredentialsConfig record {|
    string apiKey;
    string apiSecret;
|};
