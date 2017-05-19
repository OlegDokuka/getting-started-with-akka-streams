package ua.akkastreams.learning;

import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.stream.ActorMaterializer;
import akka.stream.IOResult;
import akka.stream.Materializer;
import akka.stream.javadsl.*;
import akka.util.ByteString;

import java.math.BigInteger;
import java.nio.file.Paths;
import java.util.concurrent.CompletionStage;

public class Sample3FlowUsage {
    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("FlowUsage");
        Materializer materializer = ActorMaterializer.create(system);

        Source.range(1, 100)
                .via(factorial())
                .map(BigInteger::toString)
                .runWith(save("build/factorials.txt"), materializer)
                .thenRun(system::terminate);
    }

    private static Flow<Integer, BigInteger, NotUsed> factorial() {
        return Flow.of(Integer.class)
                .scan(BigInteger.ONE, (acc, next) -> acc.multiply(BigInteger.valueOf(next)));
    }

    private static Sink<String, CompletionStage<IOResult>> save(String filename) {
        return Flow.of(String.class)
                .map(v -> ByteString.fromString(v + "\n"))
                .toMat(FileIO.toPath(Paths.get(filename)), Keep.right());
    }
}
