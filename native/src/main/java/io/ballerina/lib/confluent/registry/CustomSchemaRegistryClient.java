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

import static io.ballerina.lib.confluent.registry.ModuleUtils.NATIVE_CLIENT;
import static io.ballerina.lib.confluent.registry.Utils.CLIENT_INVOCATION_ERROR;

public final class CustomSchemaRegistryClient {

    public static final String AVRO = "AVRO";

    private CustomSchemaRegistryClient() {}

    public static void generateSchemaRegistryClient(BObject registryClient, BMap<BString, Object> config) {
        BString baseUrl = (BString) config.get(ModuleUtils.BASE_URL);
        long identityMapCapacity = config.getIntValue(ModuleUtils.IDENTITY_MAP_CAPACITY);
        BMap<BString, Object> originals = (BMap<BString, Object>) config.getMapValue(ModuleUtils.ORIGINALS);
        BMap<BString, Object> httpHeaders = (BMap<BString, Object>) config.getMapValue(ModuleUtils.HEADERS);
        Map<String, String> configurations = new HashMap<>();
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
