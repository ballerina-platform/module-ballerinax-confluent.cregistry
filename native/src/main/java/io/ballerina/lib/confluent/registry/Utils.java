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

import io.ballerina.runtime.api.creators.ErrorCreator;
import io.ballerina.runtime.api.creators.ValueCreator;
import io.ballerina.runtime.api.values.BError;
import io.ballerina.runtime.api.values.BMap;
import io.ballerina.runtime.api.values.BString;
import io.confluent.kafka.schemaregistry.client.rest.exceptions.RestClientException;

import java.util.Map;

import static io.ballerina.lib.confluent.registry.ModuleUtils.getModule;
import static io.ballerina.runtime.api.utils.StringUtils.fromString;

public class Utils {

    private Utils() {
    }

    public static final String ERROR_TYPE = "Error";
    public static final String ERROR_DETAILS = "ErrorDetails";
    public static final String STATUS = "status";
    public static final String ERROR_CODE = "errorCode";
    public static final String CLIENT_INVOCATION_ERROR = "Schema registry client invocation error";

    public static BError createError(String message, Throwable throwable) {
        BError cause = ErrorCreator.createError(throwable);
        BMap<BString, Object> errorDetails = getErrorDetails(throwable);
        return ErrorCreator.createError(getModule(), ERROR_TYPE, fromString(message), cause, errorDetails);
    }

    private static BMap<BString, Object> getErrorDetails(Throwable throwable) {
        if (throwable instanceof RestClientException) {
            int status = ((RestClientException) throwable).getStatus();
            int errorCode = ((RestClientException) throwable).getErrorCode();
            return ValueCreator.createRecordValue(getModule(), ERROR_DETAILS,
                                                  Map.of(STATUS, status, ERROR_CODE, errorCode));
        }
        return null;
    }
}
