package com.myorg;

import software.amazon.awscdk.core.App;

public final class FooApp {
    public static void main(final String[] args) {
        App app = new App();

        new FooStack(app, "FooStack");

        app.synth();
    }
}
