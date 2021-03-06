package cn.think.in.java.learing.concurrent.one;

/**
 * 伪共享
 */
public class FalseSharing implements Runnable {

  public final static int NUM_THREADS = 4; // change
  public final static long ITERATIONS = 500L * 1000L * 1000L;
  private final int arrayIndex;

  private static VolatileLong2[] longs = new VolatileLong2[NUM_THREADS];

  static {
    for (int i = 0; i < longs.length; i++) {
      longs[i] = new VolatileLong2();
    }
  }

  public FalseSharing(final int arrayIndex) {
    this.arrayIndex = arrayIndex;
  }

  public static void main(final String[] args) throws Exception {
    long start = System.nanoTime();
    runTest();
    System.out.println("duration = " + (System.nanoTime() - start));
  }

  private static void runTest() throws InterruptedException {
    Thread[] threads = new Thread[NUM_THREADS];

    for (int i = 0; i < threads.length; i++) {
      threads[i] = new Thread(new FalseSharing(i));
    }

    for (Thread t : threads) {
      t.start();
    }

    for (Thread t : threads) {
      t.join();
    }
  }

  public void run() {
    long i = ITERATIONS + 1;
    while (0 != --i) {
      longs[arrayIndex].value = i;
    }
  }

  public final static class VolatileLong {

    public volatile long value = 0L;
  }

  // long padding避免false sharing
  // 按理说jdk7以后long padding应该被优化掉了，但是从测试结果看padding仍然起作用
  public final static class VolatileLong2 {

    // 一个long  8 个字节，然后 * 7 = 56 ，加上指针 8 字节，共计 64 字节
    volatile long p0, p1, p2, p3, p4, p5, p6;// 前面的核心将其填充满
    public volatile long value = 0L;// 当前核心
    volatile long q0, q1, q2, q3, q4, q5, q6;// 后面的核心将其填充满
  }

  // jdk8新特性，Contended注解避免false sharing  8048832021
  //                                          38833841052
  //                                          8076264753
  // Restricted on user classpath
  // Unlock: -XX:-RestrictContended
//  @sun.misc.Contended
  public final static class VolatileLong3 {

    public volatile long value = 0L;
  }
}