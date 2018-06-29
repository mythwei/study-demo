package cn.think.in.java.learing.concurrent.one;

public class ThreeTuple<A, B, C> extends TwoTuple<A, B> {

  public final C third;
  public ThreeTuple(A first, B second, C third) {
    super(first, second);
    this.third = third;

  }
}
