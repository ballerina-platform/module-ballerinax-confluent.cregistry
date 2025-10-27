/*
 * Copyright (c) 2024, WSO2 LLC. (http://www.wso2.com)
 *
 * WSO2 LLC. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package io.ballerina.lib.confluent.registry;

import io.ballerina.runtime.api.utils.StringUtils;
import io.ballerina.runtime.api.values.BArray;
import io.ballerina.runtime.api.values.BMap;
import io.ballerina.runtime.api.values.BObject;
import io.ballerina.runtime.api.values.BString;
import io.confluent.kafka.schemaregistry.ParsedSchema;
import io.confluent.kafka.schemaregistry.client.CachedSchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.rest.exceptions.RestClientException;
import org.apache.avro.Schema;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static io.ballerina.lib.confluent.registry.Utils.CLIENT_INVOCATION_ERROR;

public final class CustomSchemaRegistryClient {

    public static final String AVRO = "AVRO";
    public static final BString AUTH_CONFIG = StringUtils.fromString("auth");
    public static final String CREDENTIALS_CONFIG = "CredentialsConfig";
    public static final BString API_KEY = StringUtils.fromString("apiKey");
    public static final BString API_SECRET = StringUtils.fromString("apiSecret");
    public static final String BASIC_AUTH_CREDENTIALS_SOURCE = "basic.auth.credentials.source";
    public static final String SCHEMA_REGISTRY_BASIC_AUTH_USER_INFO = "basic.auth.user.info";
    public static final BString BASE_URL = StringUtils.fromString("baseUrl");
    public static final BString IDENTITY_MAP_CAPACITY = StringUtils.fromString("identityMapCapacity");
    public static final BString ORIGINALS = StringUtils.fromString("originals");
    public static final BString HEADERS = StringUtils.fromString("headers");
    public static final BString SECURE_SOCKET = StringUtils.fromString("secureSocket");
    public static final String NATIVE_CLIENT = "client";
    public static final String USER_INFO = "USER_INFO";

    public static final String SCHEMA_REGISTRY_SSL_TRUSTSTORE_LOCATION = "schema.registry.ssl.truststore.location";
    public static final String SCHEMA_REGISTRY_SSL_TRUSTSTORE_PASSWORD = "schema.registry.ssl.truststore.password";
    public static final String SCHEMA_REGISTRY_SSL_TRUSTSTORE_TYPE = "schema.registry.ssl.truststore.type";
    public static final String SCHEMA_REGISTRY_SSL_TRUSTSTORE_CERTIFICATES =
            "schema.registry.ssl.truststore.certificates";
    public static final String SCHEMA_REGISTRY_SSL_KEYSTORE_LOCATION = "schema.registry.ssl.keystore.location";
    public static final String SCHEMA_REGISTRY_SSL_KEYSTORE_PASSWORD = "schema.registry.ssl.keystore.password";
    public static final String SCHEMA_REGISTRY_SSL_KEY_PASSWORD = "schema.registry.ssl.key.password";
    public static final String SCHEMA_REGISTRY_SSL_ENABLED_PROTOCOLS = "schema.registry.ssl.enabled.protocols";
    public static final String SCHEMA_REGISTRY_SSL_CIPHER_SUITES = "schema.registry.ssl.cipher.suites";
    public static final String SCHEMA_REGISTRY_SSL_ENDPOINT_IDENTIFICATION_ALGORITHM =
            "schema.registry.ssl.endpoint.identification.algorithm";

    public static final BString ENABLE = StringUtils.fromString("enable");
    public static final BString CERT = StringUtils.fromString("cert");
    public static final BString KEY = StringUtils.fromString("key");
    public static final BString PROTOCOL = StringUtils.fromString("protocol");
    public static final BString CIPHERS = StringUtils.fromString("ciphers");
    public static final BString VERIFY_HOST_NAME = StringUtils.fromString("verifyHostName");
    public static final BString PATH = StringUtils.fromString("path");
    public static final BString PASSWORD = StringUtils.fromString("password");
    public static final String SSL_TRUSTSTORE_TYPE = "ssl.truststore.type";
    public static final String SSL_TRUSTSTORE_CERTIFICATES = "ssl.truststore.certificates";
    public static final String SSL_TRUSTSTORE_LOCATION = "ssl.truststore.location";
    public static final String SSL_TRUSTSTORE_PASSWORD = "ssl.truststore.password";
    public static final String PEM = "PEM";
    public static final String SSL_ENABLED_PROTOCOLS = "ssl.enabled.protocols";
    public static final String SSL_CIPHER_SUITES = "ssl.cipher.suites";
    public static final String SSL_ENDPOINT_IDENTIFICATION_ALGORITHM = "ssl.endpoint.identification.algorithm";
    public static final String SSL_KEYSTORE_LOCATION = "ssl.keystore.location";
    public static final String SSL_KEYSTORE_PASSWORD = "ssl.keystore.password";
    public static final String SSL_KEY_PASSWORD = "ssl.key.password";
    public static final BString VERSIONS = StringUtils.fromString("versions");

    private CustomSchemaRegistryClient() {}

    public static void generateSchemaRegistryClient(BObject registryClient, BMap<BString, Object> config) {
        BString baseUrl = (BString) config.get(BASE_URL);
        long identityMapCapacity = config.getIntValue(IDENTITY_MAP_CAPACITY);
        BMap<BString, Object> originals = (BMap<BString, Object>) config.getMapValue(ORIGINALS);
        BMap<BString, Object> httpHeaders = (BMap<BString, Object>) config.getMapValue(HEADERS);
        BMap<BString, Object> authConfig = (BMap<BString, Object>) config.getMapValue(AUTH_CONFIG);
        BMap<BString, Object> secureSocketConfig = (BMap<BString, Object>) config.getMapValue(SECURE_SOCKET);
        Map<String, String> configurations = new HashMap<>();
        if (authConfig != null && (authConfig.getType().getName().contains(CREDENTIALS_CONFIG) ||
                authConfig.getType().getCachedReferredType() != null &&
                        authConfig.getType().getCachedReferredType().getName().contains(CREDENTIALS_CONFIG))) {
            BString apiKey = (BString) authConfig.get(API_KEY);
            BString apiSecret = (BString) authConfig.get(API_SECRET);
            configurations.put(BASIC_AUTH_CREDENTIALS_SOURCE, USER_INFO);
            configurations.put(SCHEMA_REGISTRY_BASIC_AUTH_USER_INFO, apiKey.getValue() + ":" + apiSecret.getValue());
        }
        if (secureSocketConfig != null) {
            handleSecureSocketConfiguration(secureSocketConfig, configurations);
        }
        if (originals != null) {
            for (BString key: originals.getKeys()) {
                configurations.put(key.getValue().replaceAll("^\"|\"$", ""),
                                   originals.get(key).toString());
            }
        }
        Map<String, String> headers = new HashMap<>();
        if (httpHeaders != null) {
            for (BString header : httpHeaders.getKeys()) {
                headers.put(header.getValue(), httpHeaders.get(header).toString());
            }
        }
        SchemaRegistryClient registry = new CachedSchemaRegistryClient(baseUrl.getValue(), (int) identityMapCapacity,
                                                                       configurations, headers);
        registryClient.addNativeData(NATIVE_CLIENT, registry);
    }

    private static void handleSecureSocketConfiguration(BMap<BString, Object> secureSocketConfig, 
                                                        Map<String, String> configurations) {
        boolean enable = secureSocketConfig.getBooleanValue(ENABLE);
        if (!enable) {
            return;
        }
        Object certConfig = secureSocketConfig.get(CERT);
        if (certConfig != null) {
            if (certConfig instanceof BString) {
                String certPath = ((BString) certConfig).getValue();
                configurations.put(SCHEMA_REGISTRY_SSL_TRUSTSTORE_TYPE, PEM);
                configurations.put(SCHEMA_REGISTRY_SSL_TRUSTSTORE_CERTIFICATES, certPath);
                configurations.put(SSL_TRUSTSTORE_TYPE, PEM);
                configurations.put(SSL_TRUSTSTORE_CERTIFICATES, certPath);
            } else if (certConfig instanceof BMap) {
                BMap<BString, Object> trustStoreConfig = (BMap<BString, Object>) certConfig;
                Object path = trustStoreConfig.get(PATH);
                Object password = trustStoreConfig.get(PASSWORD);
                if (path != null) {
                    configurations.put(SCHEMA_REGISTRY_SSL_TRUSTSTORE_LOCATION, path.toString());
                    configurations.put(SSL_TRUSTSTORE_LOCATION, path.toString());
                }
                if (password != null) {
                    configurations.put(SCHEMA_REGISTRY_SSL_TRUSTSTORE_PASSWORD, password.toString());
                    configurations.put(SSL_TRUSTSTORE_PASSWORD, password.toString());
                }
            }
        }
        Object keyConfig = secureSocketConfig.get(KEY);
        if (keyConfig instanceof BMap) {
            BMap<BString, Object> keyStoreConfig = (BMap<BString, Object>) keyConfig;
            Object path = keyStoreConfig.get(PATH);
            Object password = keyStoreConfig.get(PASSWORD);
            
            if (path != null) {
                configurations.put(SCHEMA_REGISTRY_SSL_KEYSTORE_LOCATION, path.toString());
                configurations.put(SSL_KEYSTORE_LOCATION, path.toString());
            }
            if (password != null) {
                configurations.put(SCHEMA_REGISTRY_SSL_KEYSTORE_PASSWORD, password.toString());
                configurations.put(SCHEMA_REGISTRY_SSL_KEY_PASSWORD, password.toString());
                configurations.put(SSL_KEYSTORE_PASSWORD, password.toString());
                configurations.put(SSL_KEY_PASSWORD, password.toString());
            }
        }
        Object protocolConfig = secureSocketConfig.get(PROTOCOL);
        if (protocolConfig instanceof BMap) {
            BMap<BString, Object> protocol = (BMap<BString, Object>) protocolConfig;
            Object versions = protocol.get(VERSIONS);
            if (versions instanceof BArray versionArray) {
                StringBuilder protocols = new StringBuilder();
                for (int i = 0; i < versionArray.size(); i++) {
                    if (i > 0) {
                        protocols.append(",");
                    }
                    protocols.append(versionArray.getBString(i).getValue());
                }
                configurations.put(SCHEMA_REGISTRY_SSL_ENABLED_PROTOCOLS, protocols.toString());
                configurations.put(SSL_ENABLED_PROTOCOLS, protocols.toString());
            }
        }
        Object ciphersConfig = secureSocketConfig.get(CIPHERS);
        if (ciphersConfig instanceof BArray cipherArray) {
            StringBuilder ciphers = new StringBuilder();
            for (int i = 0; i < cipherArray.size(); i++) {
                if (i > 0) {
                    ciphers.append(",");
                }
                ciphers.append(cipherArray.getBString(i).getValue());
            }
            configurations.put(SCHEMA_REGISTRY_SSL_CIPHER_SUITES, ciphers.toString());
            configurations.put(SSL_CIPHER_SUITES, ciphers.toString());
        }
        boolean verifyHostName = secureSocketConfig.getBooleanValue(VERIFY_HOST_NAME);
        if (!verifyHostName) {
            configurations.put(SCHEMA_REGISTRY_SSL_ENDPOINT_IDENTIFICATION_ALGORITHM, "");
            configurations.put(SSL_ENDPOINT_IDENTIFICATION_ALGORITHM, "");
        }
    }

    public static Object register(BObject registryClient, BString subject, BString schema) {
        SchemaRegistryClient schemaRegistryClient = (SchemaRegistryClient) registryClient.getNativeData(NATIVE_CLIENT);
        try {
            Schema.Parser parser = new Schema.Parser();
            Schema avroSchema = parser.parse(schema.getValue());
            ParsedSchema parsedSchema = getParsedSchema(schemaRegistryClient, avroSchema);
            return schemaRegistryClient.register(subject.getValue(), parsedSchema);
        } catch (Exception e) {
            return Utils.createError(CLIENT_INVOCATION_ERROR, e);
        }
    }

    public static Object getSchemaById(BObject registryClient, int i) {
        SchemaRegistryClient schemaRegistryClient = (SchemaRegistryClient) registryClient.getNativeData(NATIVE_CLIENT);
        try {
            return StringUtils.fromString(schemaRegistryClient.getSchemaById(i).canonicalString());
        } catch (IOException | RestClientException e) {
            return Utils.createError(CLIENT_INVOCATION_ERROR, e);
        }
    }

    private static ParsedSchema getParsedSchema(SchemaRegistryClient schemaRegistryClient,
                                                          Schema avroSchema) {
        Optional<ParsedSchema> parsedSchema = schemaRegistryClient.parseSchema(AVRO,
                avroSchema.toString(), null);
        if (parsedSchema.isEmpty()) {
            throw Utils.createError(CLIENT_INVOCATION_ERROR, new Throwable("Error occurred while parsing the schema"));
        }
        return parsedSchema.get();
    }
}
