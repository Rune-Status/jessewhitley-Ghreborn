package Ghreborn.util;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Benchmark {

	private static String name;
	private static long startTime;

	private static final Map<String, Long> benchmarks = new HashMap<>();

	public static void start(String name) {
		if (Benchmark.name != null) {
			stop();
		}
		
		Benchmark.name = name;
		startTime = System.nanoTime();
	}

	public static void stop() {
		long benchmark = System.nanoTime() - startTime;

		Long previousBenchmark = benchmarks.get(name);
		if (previousBenchmark != null) {
			benchmark += previousBenchmark;
			benchmark /= 2;
		}

		benchmarks.put(name, benchmark);
		name = null;
	}

	public static void save() {
		try {
			PrintWriter writer = new PrintWriter("Data/benchmark/" + System.currentTimeMillis() + ".log");
			for (Entry<String, Long> entry : benchmarks.entrySet()) {
				writer.println(entry.getKey() + ": " + (entry.getValue() / 1_000_000) + " ms");
			}
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}