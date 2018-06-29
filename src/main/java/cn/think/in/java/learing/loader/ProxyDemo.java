package cn.think.in.java.learing.loader;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

public class ProxyDemo {

  interface IHello {

    void sayHello();
  }

  static class Hello implements IHello {

    @Override
    public void sayHello() {
      System.out.println("hello world");
    }
  }

  static class DynamicProxy implements InvocationHandler {

    Object o;

    Object bind(Object o) {
      this.o = o;

      return Proxy
          .newProxyInstance(o.getClass().getClassLoader(), o.getClass().getInterfaces(), this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
      System.out.println("welcome");
      return method.invoke(o, args);
    }
  }

  public static void main(String[] args) throws InterruptedException {
    List list = new ArrayList();
    while (true) {
      System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
      IHello h = (IHello) new DynamicProxy().bind(new Hello());
      h.sayHello();
      Thread.sleep(100);
      list.add(h);
    }

  }

}
