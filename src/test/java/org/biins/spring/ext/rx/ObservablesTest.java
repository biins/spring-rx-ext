package org.biins.spring.ext.rx;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureTask;
import org.springframework.web.context.request.async.DeferredResult;
import rx.Observable;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import static org.junit.Assert.*;

/**
 * @author Martin Janys
 */
public class ObservablesTest {

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testToObservableDeferredResult() throws Exception {
        String input = "test";
        DeferredResult<String> deferredResult = new DeferredResult<>();
        new Thread(() -> {
            sleep(500);
            deferredResult.setResult(input);
        }).start();
        String result = Observables.toObservable(deferredResult)
                .toBlocking()
                .single();
        assertEquals(input, result);
    }

    @Test
    public void testToObservableListenableFuture() throws Exception {
        String input = "test";
        ListenableFutureTask<String> listenableFuture = new ListenableFutureTask<>(() -> {
            sleep(500);
            return input;
        });
        new Thread(listenableFuture::run).start();
        String result = Observables.toObservable(listenableFuture)
                .toBlocking()
                .single();
        assertEquals(input, result);
    }

    @Test
    public void testToObservableFuture() throws Exception {
        String input = "test";
        FutureTask<String> listenableFuture = new FutureTask<>(() -> {
            sleep(500);
            return input;
        });
        new Thread(listenableFuture::run).start();
        String result = Observables.toObservable(listenableFuture)
                .toBlocking()
                .single();
        assertEquals(input, result);
    }

    @Test
    public void testToDeferredResult() throws Exception {
        String input = "test";
        DeferredResult<String> deferredResult = Observables.toDeferredResult(Observable.create(subscriber -> {
            subscriber.onNext(input);
            subscriber.onCompleted();
        }));
        assertEquals(input, deferredResult.getResult());
    }
}