package com.myorg;

import software.amazon.awscdk.core.Construct;
import software.amazon.awscdk.core.Duration;
import software.amazon.awscdk.core.Stack;
import software.amazon.awscdk.core.StackProps;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.Runtime;
import software.amazon.awscdk.services.sns.Topic;
import software.amazon.awscdk.services.sns.subscriptions.SqsSubscription;
import software.amazon.awscdk.services.sqs.Queue;
import software.amazon.awscdk.services.lambda.Function;

public class FooStack extends Stack {
    public FooStack(final Construct parent, final String id) {
        this(parent, id, null);
    }

    public FooStack(final Construct parent, final String id, final StackProps props) {
        super(parent, id, props);

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
                .runtime(Runtime.NODEJS_12_X)
                .build();
    }
}
