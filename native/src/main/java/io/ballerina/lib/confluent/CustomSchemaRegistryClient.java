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

import io.ballerina.runtime.api.creators.ValueCreator;
import io.ballerina.runtime.api.utils.StringUtils;
import io.ballerina.runtime.api.values.BArray;
import io.ballerina.runtime.api.values.BMap;
import io.ballerina.runtime.api.values.BString;
import io.confluent.kafka.schemaregistry.client.CachedSchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.rest.exceptions.RestClientException;
import org.apache.avro.Schema;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class CustomSchemaRegistryClient {

    SchemaRegistryClient schemaRegistryClient;

    public CustomSchemaRegistryClient(BMap<?, ?> config) {
        BString baseUrl = (BString) config.get(StringUtils.fromString("baseUrl"));
        long identityMapCapacity = config.getIntValue(StringUtils.fromString("identityMapCapacity"));
        BMap<?, ?> originals = config.getMapValue(StringUtils.fromString("originals"));
        BMap<?, ?> httpHeaders = config.getMapValue(StringUtils.fromString("headers"));

        Map<String, String> configurations = new HashMap<>();
        for (BString key: (BString[]) originals.getKeys()) {
            configurations.put(key.getValue()
                    .replaceAll("^\"|\"$", ""), originals.get(key).toString());
        }
        Map<String, String> headers = new HashMap<>();
        for (BString header: (BString[]) httpHeaders.getKeys()) {
            headers.put(header.getValue(), httpHeaders.get(header).toString());
        }
        this.schemaRegistryClient = new CachedSchemaRegistryClient(baseUrl.getValue(), (int) identityMapCapacity,
                                                                   configurations, headers);
    }

    public int register(BString subject, BString schema) throws IOException, RestClientException {
        Schema.Parser parser = new Schema.Parser();
        Schema schema1 = parser.parse(schema.getValue());
        return schemaRegistryClient.register(subject.getValue(), schema1);
    }

//    public int getId(String s, Schema schema) throws IOException, RestClientException {
//        return schemaRegistryClient.getId(s, schema);
//    }

    public Object getById(int i) {
        try {
            return StringUtils.fromString(schemaRegistryClient.getById(i).toString());
        } catch (RestClientException | IOException e) {
            return e;
        }
    }

//    public Collection<String> getAllSubjects() throws IOException, RestClientException {
//        return schemaRegistryClient.getAllSubjects();
//    }

    public BArray idToBytes(int value) {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.putInt(value);
        BArray byteArray = ValueCreator.createArrayValue(buffer.array());
        return byteArray;
    }

//    public Integer deleteSchemaVersion(String s, String s1) throws IOException, RestClientException {
//        return schemaRegistryClient.deleteSchemaVersion(s, s1);
//    }

//    public int register(String s, Schema schema, int i, int i1) throws IOException, RestClientException {
//        return schemaRegistryClient.register(s, schema, i, i1);
//    }
//
//    public Schema getByID(int i) throws IOException, RestClientException {
//        return schemaRegistryClient.getByID(i);
//    }
//
//    public Schema getBySubjectAndID(String s, int i) throws IOException, RestClientException {
//        return schemaRegistryClient.getBySubjectAndID(s, i);
//    }
//
//
//    public Schema getBySubjectAndId(String s, int i) throws IOException, RestClientException {
//        return schemaRegistryClient.getBySubjectAndId(s, i);
//    }
//
//    @Override
//    public SchemaMetadata getLatestSchemaMetadata(String s) throws IOException, RestClientException {
//        return schemaRegistryClient.getLatestSchemaMetadata(s);
//    }
//
//    @Override
//    public SchemaMetadata getSchemaMetadata(String s, int i) throws IOException, RestClientException {
//        return schemaRegistryClient.getSchemaMetadata(s, i);
//    }
//
//    @Override
//    public int getVersion(String s, Schema schema) throws IOException, RestClientException {
//        return schemaRegistryClient.getVersion(s, schema);
//    }
//
//    @Override
//    public List<Integer> getAllVersions(String s) throws IOException, RestClientException {
//        return schemaRegistryClient.getAllVersions(s);
//    }
//
//    @Override
//    public boolean testCompatibility(String s, Schema schema) throws IOException, RestClientException {
//        return schemaRegistryClient.testCompatibility(s, schema);
//    }
//
//    @Override
//    public String updateCompatibility(String s, String s1) throws IOException, RestClientException {
//        return schemaRegistryClient.updateCompatibility(s, s1);
//    }
//
//    @Override
//    public String getCompatibility(String s) throws IOException, RestClientException {
//        return schemaRegistryClient.getCompatibility(s);
//    }
//
//    @Override
//    public String setMode(String s) throws IOException, RestClientException {
//        return schemaRegistryClient.setMode(s);
//    }
//
//    @Override
//    public String setMode(String s, String s1) throws IOException, RestClientException {
//        return schemaRegistryClient.setMode(s, s1);
//    }
//
//    @Override
//    public String getMode() throws IOException, RestClientException {
//        return schemaRegistryClient.getMode();
//    }
//
//    @Override
//    public String getMode(String s) throws IOException, RestClientException {
//        return schemaRegistryClient.getMode(s);
//    }
//
//    @Override
//    public Collection<String> getAllSubjects() throws IOException, RestClientException {
//        return schemaRegistryClient.getAllSubjects();
//    }
//
//    @Override
//    public int getId(String s, Schema schema) throws IOException, RestClientException {
//        return schemaRegistryClient.getId(s, schema);
//    }
//
//    @Override
//    public List<Integer> deleteSubject(String s) throws IOException, RestClientException {
//        return schemaRegistryClient.deleteSubject(s);
//    }
//
//    @Override
//    public List<Integer> deleteSubject(Map<String, String> map, String s) throws IOException, RestClientException {
//        return schemaRegistryClient.deleteSubject(map, s);
//    }
//
//    @Override
//    public Integer deleteSchemaVersion(String s, String s1) throws IOException, RestClientException {
//        return schemaRegistryClient.deleteSchemaVersion(s, s1);
//    }
//
//    @Override
//    public Integer deleteSchemaVersion(Map<String, String> map, String s, String s1)
//                                       throws IOException, RestClientException {
//        return schemaRegistryClient.deleteSchemaVersion(map, s, s1);
//    }

//    public void reset() {
//        schemaRegistryClient.reset();
//    }
}
