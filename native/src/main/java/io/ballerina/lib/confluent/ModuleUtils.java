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

import io.ballerina.runtime.api.Environment;
import io.ballerina.runtime.api.Module;
import io.ballerina.runtime.api.utils.StringUtils;
import io.ballerina.runtime.api.values.BString;

/**
 * Utility functions of the Schema Registry module.
 *
 * @since 0.1.0
 */
public class ModuleUtils {

    private ModuleUtils() {}
    public static final BString BASE_URL = StringUtils.fromString("baseUrl");
    public static final BString IDENTITY_MAP_CAPACITY = StringUtils.fromString("identityMapCapacity");
    public static final BString ORIGINALS = StringUtils.fromString("originals");
    public static final BString HEADERS = StringUtils.fromString("headers");

    private static Module avroModule = null;

    public static Module getModule() {
        return avroModule;
    }

    @SuppressWarnings("unused")
    public static void setModule(Environment env) {
        avroModule = env.getCurrentModule();
    }

}
