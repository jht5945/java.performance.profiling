package me.hatter.test.performance.profiling;

import java.lang.reflect.Method;

public class ReflectTest {

    public static int doAdd(int i) {
        return ++i;
    }

    static interface Invoker {
        Object invoke(Object[] objs);
    }

    static class Invoker0 implements Invoker {

        @Override
        public Object invoke(Object[] objs) {
            return doAdd((Integer) objs[0]);
        }
    }

    static final long COUNT = 1000000000L;

    public static void main(String[] args) throws Exception {
        // warm up
        main1(args);
        main1_2(args);
        main2(args);
        main3(args);
        System.out.println();
        System.out.println();
        System.out.println("TEST1:");
        main1(args);
        System.out.println("TEST1_2:");
        main1_2(args);
        System.out.println("TEST2:");
        main2(args);
        System.out.println("TEST3:");
        main3(args);
    }

    public static void main1(String[] args) throws Exception {
        long start = System.currentTimeMillis();
        int x = 0;
        for (long i = 0; i < COUNT; i++) {
            x = doAdd(x);
        }
        long end = System.currentTimeMillis();
        System.out.println((end - start) + " ms cost");
    }

    public static void main1_2(String[] args) throws Exception {
        long start = System.currentTimeMillis();
        int x = 0;
        for (long i = 0; i < COUNT; i++) {
            x = (++x);
        }
        long end = System.currentTimeMillis();
        System.out.println((end - start) + " ms cost");
    }

    public static void main2(String[] args) throws Exception {
        long start = System.currentTimeMillis();
        Method doAddMethod = ReflectTest.class.getDeclaredMethod("doAdd", new Class[]{int.class});
        int x = 0;
        for (long i = 0; i < COUNT; i++) {
            x = (int) (Integer) doAddMethod.invoke(null, new Object[]{x});
        }
        long end = System.currentTimeMillis();
        System.out.println((end - start) + " ms cost");
    }

    public static void main3(String[] args) throws Exception {
        long start = System.currentTimeMillis();
        int x = 0;
        Invoker invoker = new Invoker0();
        for (long i = 0; i < COUNT; i++) {
            x = (Integer) invoker.invoke(new Object[]{x});
        }
        long end = System.currentTimeMillis();
        System.out.println((end - start) + " ms cost");
    }
}