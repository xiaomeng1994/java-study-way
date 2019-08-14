package java8;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * java8异步编程学习
 */
@Slf4j
public class CompletableFutureStudy {

    @Test
    public void test() throws Exception {
        log.info("主线程：{}", Thread.currentThread().getName());
        CompletableFuture noReturn = CompletableFuture.runAsync(() -> {
            //执行逻辑,无返回值
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("异步编程-》无返回值-》方法test-》线程：{}", Thread.currentThread().getName());
        });
        while (!noReturn.isDone()) {
            log.info("异步无返回值对象正在执行中：{}", JSONObject.toJSONString(noReturn));
            TimeUnit.SECONDS.sleep(1);
        }
        log.info("异步无返回值对象执行完成：{}，返回值：{}", JSONObject.toJSONString(noReturn), noReturn.get());


        log.info("------------------------我是华丽的分割线------------------------");

        CompletableFuture<String> hasReturn = CompletableFuture.supplyAsync(() -> {
            //执行逻辑,无返回值
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String returnStr = "异步编程-》有返回值-》方法test";
            log.info(returnStr + "-》线程：{}", Thread.currentThread().getName());
            return returnStr;
        });
        while (!hasReturn.isDone()) {
            log.info("异步有回值对象正在执行中：{}", JSONObject.toJSONString(hasReturn));
            TimeUnit.SECONDS.sleep(1);
        }
        log.info("异步有回值对象执行完成：{}，返回值：{}", JSONObject.toJSONString(hasReturn), hasReturn.get());


    }


}
