package benchmarks;

import org.openjdk.jmh.annotations.*;
import re2dfa.fsm.factories.FSMFactory;
import re2dfa.scanner.RegexReader;

import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
public class NFAGeneratorBenchmark {

    @Param({"abcdef*g*(a|b|c*|\\e)|(abcf|eerk)ow33*jrke(aasw)(aasw)*"})
    public String regexes;

    @Benchmark
    @Fork(value = 1, warmups = 1)
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void nfaFactoryBenchmark() {
        FSMFactory.getFAGraph("dfa").build(RegexReader.getTokenList(regexes));
    }
}
