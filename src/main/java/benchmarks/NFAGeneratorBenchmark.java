package benchmarks;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import re2dfa.fsm.util.FSMFactory;

public class NFAGeneratorBenchmark {

    @Param({"abcdef(a|b*|c*|ddddddd*ddd*aa*)|(k|e*)*"})
    public String regex;

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    public void NFAGenerator() {
        FSMFactory.getFAGraph("nfa").build(regex);
    }

    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder().include(NFAGeneratorBenchmark.class.getSimpleName()).forks(2).build();
        new Runner(options).run();
    }
}
