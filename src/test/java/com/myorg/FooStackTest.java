package com.myorg;

import software.amazon.awscdk.core.App;
import software.amazon.awscdk.cxapi.CloudAssembly;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class FooStackTest {
    private final static ObjectMapper MAPPER =
        new ObjectMapper().configure(SerializationFeature.INDENT_OUTPUT, true);

    @Test
    public void testStack() throws Exception {
        App app = new App();
        FooStack stack = new FooStack(app, "test");

        CloudAssembly assembly = app.synth();
        Map<String, Object> template = (Map<String, Object>) assembly.getStackArtifact(stack.getArtifactId()).getTemplate();
        assertThat(template).containsKey("Resources");

        String json = MAPPER.writeValueAsString(template);
        assertThat(json).contains("AWS::SQS::Queue");
        assertThat(json).contains("AWS::SNS::Topic");
        assertThat(json).contains("AWS::Lambda::Function");
    }
}