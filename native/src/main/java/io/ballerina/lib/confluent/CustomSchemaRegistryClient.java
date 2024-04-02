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

package io.ballerina.lib.confluent;

import io.ballerina.runtime.api.utils.StringUtils;
import io.ballerina.runtime.api.values.BMap;
import io.ballerina.runtime.api.values.BString;
import io.confluent.kafka.schemaregistry.client.CachedSchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.rest.exceptions.RestClientException;
import org.apache.avro.Schema;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static io.ballerina.lib.confluent.ModuleUtils.BASE_URL;
import static io.ballerina.lib.confluent.ModuleUtils.HEADERS;
import static io.ballerina.lib.confluent.ModuleUtils.IDENTITY_MAP_CAPACITY;
import static io.ballerina.lib.confluent.ModuleUtils.ORIGINALS;

public class CustomSchemaRegistryClient {

    SchemaRegistryClient schemaRegistryClient;

    public CustomSchemaRegistryClient(BMap<?, ?> config) {
        BString baseUrl = (BString) config.get(BASE_URL);
        long identityMapCapacity = config.getIntValue(IDENTITY_MAP_CAPACITY);
        BMap<?, ?> originals = config.getMapValue(ORIGINALS);
        BMap<?, ?> httpHeaders = config.getMapValue(HEADERS);

        Map<String, String> configurations = new HashMap<>();
        for (BString key: (BString[]) originals.getKeys()) {
            configurations.put(key.getValue().replaceAll("^\"|\"$", ""),
                               originals.get(key).toString());
        }
        Map<String, String> headers = new HashMap<>();
        for (BString header: (BString[]) httpHeaders.getKeys()) {
            headers.put(header.getValue(), httpHeaders.get(header).toString());
        }
        this.schemaRegistryClient = new CachedSchemaRegistryClient(baseUrl.getValue(), (int) identityMapCapacity,
                                                                   configurations, headers);
    }

    public Object register(BString subject, BString schema) {
        Schema.Parser parser = new Schema.Parser();
        Schema avroSchema = parser.parse(schema.getValue());
        try {
            return schemaRegistryClient.register(subject.getValue(), avroSchema);
        } catch (IOException | RestClientException e) {
            return Utils.createError(e.getMessage());
        }
    }

    public Object getId(BString subject, BString schema) {
        Schema.Parser parser = new Schema.Parser();
        Schema avroSchema = parser.parse(schema.getValue());
        try {
            return schemaRegistryClient.getId(subject.getValue(), avroSchema);
        } catch (IOException | RestClientException e) {
            return Utils.createError(e.getMessage());
        }
    }

    public Object getById(int i) {
        try {
            return StringUtils.fromString(schemaRegistryClient.getById(i).toString());
        } catch (RestClientException | IOException e) {
            return Utils.createError(e.getMessage());
        }
    }
}
