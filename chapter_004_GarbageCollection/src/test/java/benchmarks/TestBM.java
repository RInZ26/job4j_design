package benchmarks;

import org.openjdk.jmh.annotations.*;

public class TestBM {

    @Benchmark
    @Fork(value = 1)
    @BenchmarkMode(Mode.All)
    @Warmup(iterations = 3)
    @Measurement(iterations = 5)
    public String string() {
        String s = "Foo";
        StringBuilder stringBuilder = new StringBuilder(s);
        for (int c = 0; c < 1000; c++) {
            //stringBuilder = stringBuilder.append("Bar");
            s += "Bar";
        }
        return s;
    }
}
