package hu.dpc.phee.importerng;

import com.bazaarvoice.jolt.Chainr;
import com.bazaarvoice.jolt.JsonUtils;
import org.json.JSONObject;

import java.util.List;

public class ParserDemo {

    public static void main(String[] args){
        String transaction1 = "{\n" +
                "  \"partitionId\": 1,\n" +
                "  \"value\": {\n" +
                "    \"resources\": [\n" +
                "      {\n" +
                "        \"resource\": \"PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiPz4KPGJwbW46ZGVmaW5pdGlvbnMgeG1sbnM6YnBtbj0iaHR0cDovL3d3dy5vbWcub3JnL3NwZWMvQlBNTi8yMDEwMDUyNC9NT0RFTCIgeG1sbnM6YnBtbmRpPSJodHRwOi8vd3d3Lm9tZy5vcmcvc3BlYy9CUE1OLzIwMTAwNTI0L0RJIiB4bWxuczpkYz0iaHR0cDovL3d3dy5vbWcub3JnL3NwZWMvREQvMjAxMDA1MjQvREMiIHhtbG5zOnplZWJlPSJodHRwOi8vY2FtdW5kYS5vcmcvc2NoZW1hL3plZWJlLzEuMCIgeG1sbnM6ZGk9Imh0dHA6Ly93d3cub21nLm9yZy9zcGVjL0RELzIwMTAwNTI0L0RJIiB4bWxuczptb2RlbGVyPSJodHRwOi8vY2FtdW5kYS5vcmcvc2NoZW1hL21vZGVsZXIvMS4wIiBpZD0iRGVmaW5pdGlvbnNfMXRxa2w5NyIgdGFyZ2V0TmFtZXNwYWNlPSJodHRwOi8vYnBtbi5pby9zY2hlbWEvYnBtbiIgZXhwb3J0ZXI9IkNhbXVuZGEgTW9kZWxlciIgZXhwb3J0ZXJWZXJzaW9uPSI0LjguMSIgbW9kZWxlcjpleGVjdXRpb25QbGF0Zm9ybT0iQ2FtdW5kYSBDbG91ZCIgbW9kZWxlcjpleGVjdXRpb25QbGF0Zm9ybVZlcnNpb249IjEuMC4wIj4KICA8YnBtbjpwcm9jZXNzIGlkPSJIZWxsb1Byb2Nlc3MiIGlzRXhlY3V0YWJsZT0idHJ1ZSI+CiAgICA8YnBtbjpzdGFydEV2ZW50IGlkPSJTdGFydEV2ZW50XzEiPgogICAgICA8YnBtbjpvdXRnb2luZz5GbG93XzFqZWtiZms8L2JwbW46b3V0Z29pbmc+CiAgICA8L2JwbW46c3RhcnRFdmVudD4KICAgIDxicG1uOnNlcXVlbmNlRmxvdyBpZD0iRmxvd18xamVrYmZrIiBzb3VyY2VSZWY9IlN0YXJ0RXZlbnRfMSIgdGFyZ2V0UmVmPSJIZWxsb1Rhc2siIC8+CiAgICA8YnBtbjpzZXJ2aWNlVGFzayBpZD0iSGVsbG9UYXNrIiBuYW1lPSJIZWxsb1Rhc2siPgogICAgICA8YnBtbjpleHRlbnNpb25FbGVtZW50cz4KICAgICAgICA8emVlYmU6dGFza0RlZmluaXRpb24gdHlwZT0iaGVsbG8td29ybGQiIC8+CiAgICAgIDwvYnBtbjpleHRlbnNpb25FbGVtZW50cz4KICAgICAgPGJwbW46aW5jb21pbmc+Rmxvd18xamVrYmZrPC9icG1uOmluY29taW5nPgogICAgICA8YnBtbjpvdXRnb2luZz5GbG93XzFxeWx6cnA8L2JwbW46b3V0Z29pbmc+CiAgICA8L2JwbW46c2VydmljZVRhc2s+CiAgICA8YnBtbjplbmRFdmVudCBpZD0iRXZlbnRfMG44dGlrciI+CiAgICAgIDxicG1uOmluY29taW5nPkZsb3dfMXF5bHpycDwvYnBtbjppbmNvbWluZz4KICAgIDwvYnBtbjplbmRFdmVudD4KICAgIDxicG1uOnNlcXVlbmNlRmxvdyBpZD0iRmxvd18xcXlsenJwIiBzb3VyY2VSZWY9IkhlbGxvVGFzayIgdGFyZ2V0UmVmPSJFdmVudF8wbjh0aWtyIiAvPgogIDwvYnBtbjpwcm9jZXNzPgogIDxicG1uZGk6QlBNTkRpYWdyYW0gaWQ9IkJQTU5EaWFncmFtXzEiPgogICAgPGJwbW5kaTpCUE1OUGxhbmUgaWQ9IkJQTU5QbGFuZV8xIiBicG1uRWxlbWVudD0iSGVsbG9Qcm9jZXNzIj4KICAgICAgPGJwbW5kaTpCUE1ORWRnZSBpZD0iRmxvd18xamVrYmZrX2RpIiBicG1uRWxlbWVudD0iRmxvd18xamVrYmZrIj4KICAgICAgICA8ZGk6d2F5cG9pbnQgeD0iMjE1IiB5PSIxMTciIC8+CiAgICAgICAgPGRpOndheXBvaW50IHg9IjQxMCIgeT0iMTE3IiAvPgogICAgICA8L2JwbW5kaTpCUE1ORWRnZT4KICAgICAgPGJwbW5kaTpCUE1ORWRnZSBpZD0iRmxvd18xcXlsenJwX2RpIiBicG1uRWxlbWVudD0iRmxvd18xcXlsenJwIj4KICAgICAgICA8ZGk6d2F5cG9pbnQgeD0iNTEwIiB5PSIxMTciIC8+CiAgICAgICAgPGRpOndheXBvaW50IHg9IjcxMiIgeT0iMTE3IiAvPgogICAgICA8L2JwbW5kaTpCUE1ORWRnZT4KICAgICAgPGJwbW5kaTpCUE1OU2hhcGUgaWQ9Il9CUE1OU2hhcGVfU3RhcnRFdmVudF8yIiBicG1uRWxlbWVudD0iU3RhcnRFdmVudF8xIj4KICAgICAgICA8ZGM6Qm91bmRzIHg9IjE3OSIgeT0iOTkiIHdpZHRoPSIzNiIgaGVpZ2h0PSIzNiIgLz4KICAgICAgPC9icG1uZGk6QlBNTlNoYXBlPgogICAgICA8YnBtbmRpOkJQTU5TaGFwZSBpZD0iQWN0aXZpdHlfMXlkZXd5Nl9kaSIgYnBtbkVsZW1lbnQ9IkhlbGxvVGFzayI+CiAgICAgICAgPGRjOkJvdW5kcyB4PSI0MTAiIHk9Ijc3IiB3aWR0aD0iMTAwIiBoZWlnaHQ9IjgwIiAvPgogICAgICA8L2JwbW5kaTpCUE1OU2hhcGU+CiAgICAgIDxicG1uZGk6QlBNTlNoYXBlIGlkPSJFdmVudF8wbjh0aWtyX2RpIiBicG1uRWxlbWVudD0iRXZlbnRfMG44dGlrciI+CiAgICAgICAgPGRjOkJvdW5kcyB4PSI3MTIiIHk9Ijk5IiB3aWR0aD0iMzYiIGhlaWdodD0iMzYiIC8+CiAgICAgIDwvYnBtbmRpOkJQTU5TaGFwZT4KICAgIDwvYnBtbmRpOkJQTU5QbGFuZT4KICA8L2JwbW5kaTpCUE1ORGlhZ3JhbT4KPC9icG1uOmRlZmluaXRpb25zPgo\\u003d\",\n" +
                "        \"resourceName\": \"../../zeebe-demo-dpc/src/main/resources/hello-process.bpmn\"\n" +
                "      }\n" +
                "    ],\n" +
                "    \"processesMetadata\": [\n" +
                "      {\n" +
                "        \"version\": 1,\n" +
                "        \"resourceName\": \"../../zeebe-demo-dpc/src/main/resources/hello-process.bpmn\",\n" +
                "        \"bpmnProcessId\": \"HelloProcess\",\n" +
                "        \"processDefinitionKey\": 2251799813685249,\n" +
                "        \"checksum\": \"Uqmos5xCW+XiU9aB9hg+SA\\u003d\\u003d\"\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  \"key\": 2251799813686090,\n" +
                "  \"sourceRecordPosition\": 1724,\n" +
                "  \"valueType\": \"DEPLOYMENT\",\n" +
                "  \"timestamp\": 1628079595249,\n" +
                "  \"recordType\": \"EVENT\",\n" +
                "  \"rejectionReason\": \"\",\n" +
                "  \"brokerVersion\": \"1.1.0\",\n" +
                "  \"intent\": \"CREATED\",\n" +
                "  \"rejectionType\": \"NULL_VAL\",\n" +
                "  \"position\": 1725\n" +
                "}";
        String transaction2 = "{\"key\":\"2251799813686090\"," +
                "\"valueType\":\"DEPLOYMENT\"," +
                "\"timestamp\":\"1628079595249\"," +
                "\"recordType\":\"EVENT\"," +
                "\"type\":\"Event\"" +
                "}";
        parse(transaction1);
    }

    public static void parse(String transaction){
        System.out.println("Setup started");
        String eventParserPath = "/parsers/event_deployment_created_spec.json";
        List<Object> chainrSpecJSON = JsonUtils.classpathToList(eventParserPath);
        Chainr chainr = Chainr.fromSpec(chainrSpecJSON);
        System.out.println("Setup ended");

        Object transformedOutput = chainr.transform(JsonUtils.jsonToObject(transaction));
        if (transformedOutput == null) {
            System.out.println("Did not parse");
        }else{
            System.out.println((JsonUtils.toJsonString(transformedOutput)));
        }

    }

    public static String jsonPrettyPrint(String jsonString) {
        return new JSONObject(jsonString).toString();
    }
}
