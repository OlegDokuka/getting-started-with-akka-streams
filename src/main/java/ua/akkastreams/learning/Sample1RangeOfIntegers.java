package ua.akkastreams.learning;

import akka.actor.ActorSystem;
import akka.stream.ActorMaterializer;
import akka.stream.Materializer;
import akka.stream.javadsl.Source;


public class Sample1RangeOfIntegers {
    public static void main(String[] args) {
        final ActorSystem system = ActorSystem.create("QuickStart");
        final Materializer materializer = ActorMaterializer.create(system);

        Source.range(1, 100)
                .runForeach(System.out::println, materializer)
                .thenRun(system::terminate);
    }
}
