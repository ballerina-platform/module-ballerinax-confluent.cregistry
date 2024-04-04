import ballerina/io;
import ballerinax/confluent.cregistry;

configurable string subject = ?;
configurable string baseUrl = ?;
configurable int identityMapCapacity = ?;
configurable map<anydata> originals = ?;
configurable map<string> headers = ?;

public function main() returns error? {
    cregistry:ConnectionConfig ConnectionConfig = {
        baseUrl,
        identityMapCapacity,
        originals,
        headers
    };
    cregistry:Client schemaRegistryClient = check new (ConnectionConfig);

    string schema = string `
        {
            "namespace": "example.avro",
            "type": "record",
            "name": "Student",
            "fields": [
                {"name": "name", "type": "string"},
                {"name": "favorite_color", "type": ["string", "null"]}
            ]
        }`;
    
    int registerId = check schemaRegistryClient.register(subject, schema);
    io:println("Registered Id for the schema: ", registerId);

    int getId = check schemaRegistryClient.getId(subject, schema);
    io:println("Id for the given schema: ", getId);
}
