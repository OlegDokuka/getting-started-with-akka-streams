package ua.akkastreams.learning;

import akka.actor.ActorSystem;
import akka.stream.ActorMaterializer;
import akka.stream.Materializer;
import akka.stream.javadsl.FileIO;
import akka.stream.javadsl.Source;
import akka.util.ByteString;

import java.math.BigInteger;
import java.nio.file.Paths;

public class Sample2FactorialToIO {
    public static void main(String[] args) {
        final ActorSystem system = ActorSystem.create("QuickStart");
        final Materializer materializer = ActorMaterializer.create(system);

        Source
                .range(1, 100)
                .scan(BigInteger.ONE, (acc, next) -> acc.multiply(BigInteger.valueOf(next)))
                .map(num -> ByteString.fromString(num.toString() + "\n"))
                .runWith(FileIO.toPath(Paths.get("build/factorials.txt")), materializer)
                .thenRun(system::terminate);
    }
}
