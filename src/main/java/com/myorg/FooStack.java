package com.myorg;

import software.constructs.Construct;
import software.amazon.awscdk.Duration;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.certificatemanager.Certificate;
import software.amazon.awscdk.services.certificatemanager.CertificateValidation;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.Runtime;
import software.amazon.awscdk.services.lambda.Tracing;
import software.amazon.awscdk.services.logs.RetentionDays;
import software.amazon.awscdk.services.sns.Topic;
import software.amazon.awscdk.services.sns.subscriptions.SqsSubscription;
import software.amazon.awscdk.services.sqs.Queue;

public class FooStack extends Stack {
    public FooStack(final Construct parent, final String id) {
        this(parent, id, null);
    }

    public FooStack(final Construct parent, final String id, final StackProps props) {
        super(parent, id, props);

        final Certificate cert = Certificate.Builder.create(this, "FooCertificate")
                .domainName("foo.example.com")
                .validation(CertificateValidation.fromDns())
                .build();

        final Queue queue = Queue.Builder.create(this, "FooQueue")
                .visibilityTimeout(Duration.seconds(300))
                .build();

        final Topic topic = Topic.Builder.create(this, "FooTopic")
            .displayName("My First Topic Yeah")
            .build();

        topic.addSubscription(new SqsSubscription(queue));

        final Code code = Code.fromInline("Hello world");
        final Function func = Function.Builder.create(this, "FooLambda")
                .description("Lambda Function description")
                .code(code)
                .handler("myHandler")
                .functionName("myFunctionName")
                .logRetention(RetentionDays.ONE_MONTH)
                .memorySize(1024)
                .tracing(Tracing.ACTIVE)
                .profiling(false)
                .timeout(Duration.seconds(10))
                .runtime(Runtime.NODEJS_12_X)
                .allowPublicSubnet(false)
                .build();
    }
}
