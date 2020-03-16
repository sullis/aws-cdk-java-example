package com.myorg;

import software.amazon.awscdk.core.App;
import software.amazon.awscdk.cxapi.CloudAssembly;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.Test;
import static com.google.common.truth.Truth.assertThat;

public class FooStackTest {
    private final static ObjectMapper MAPPER =
        new ObjectMapper().configure(SerializationFeature.INDENT_OUTPUT, true);

    @Test
    public void testStack() throws Exception {
        App app = new App();
        FooStack stack = new FooStack(app, "test");

        CloudAssembly assembly = app.synth();
        Object template = assembly.getStackArtifact(stack.getArtifactId()).getTemplate();
        String json = MAPPER.writeValueAsString(template);
        assertThat(json).contains("AWS::SQS::Queue");
        assertThat(json).contains("AWS::SNS::Topic");
    }
}