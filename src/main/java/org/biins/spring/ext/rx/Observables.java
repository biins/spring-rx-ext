package org.biins.spring.ext.rx;

import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.context.request.async.DeferredResult;
import rx.Observable;

import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;

/**
 * @author Martin Janys
 */
public class Observables {

    @SuppressWarnings("unchecked")
    public static <T> Observable<T> toObservable(DeferredResult<T> deferredResult) {
        return Observable.create(subscriber -> {
            deferredResult.setResultHandler(result -> {
                if (result instanceof Throwable) {
                    subscriber.onError((Throwable) result);
                }
                else {
                    subscriber.onNext((T)result);
                    subscriber.onCompleted();
                }
            });
        });
    }

    public static <T> Observable<T> toObservable(ListenableFuture<T> listenableFuture) {
        return Observable.create(subscriber -> {
            listenableFuture.addCallback(result -> {
                subscriber.onNext(result);
                subscriber.onCompleted();
            }, subscriber::onError);
        });
    }

    public static <T> Observable<T> toObservable(Future<T> future) {
        return Observable.from(future);
    }

    public static <T> DeferredResult<T> toDeferredResult(Observable<T> observable) {
        DeferredResult<T> deferredResult = new DeferredResult<>();
        observable.subscribe(deferredResult::setResult, deferredResult::setErrorResult);
        return deferredResult;
    }

}
