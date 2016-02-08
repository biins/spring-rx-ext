package org.biins.spring.ext.rx.mvc;

import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.context.request.async.WebAsyncUtils;
import org.springframework.web.method.support.AsyncHandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;
import rx.Observable;

/**
 * @author Martin Janys
 */
public class ObservableHandlerMethodReturnValueHandler implements AsyncHandlerMethodReturnValueHandler {

    public boolean isAsyncReturnValue(Object returnValue, MethodParameter returnType) {
        return (returnValue != null && Observable.class.isAssignableFrom(returnType.getParameterType()));
    }

    public boolean supportsReturnType(MethodParameter returnType) {
        return Observable.class.isAssignableFrom(returnType.getParameterType());
    }

    @SuppressWarnings("unchecked")
    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
        if (returnValue == null) {
            mavContainer.setRequestHandled(true);
        }
        else {
            DeferredResult<Object> deferredResult = new DeferredResult<>();

            Observable<Object> observable = (Observable<Object>) returnValue;
            observable.subscribe(deferredResult::setResult, deferredResult::setErrorResult);

            WebAsyncUtils.getAsyncManager(webRequest).startDeferredResultProcessing(deferredResult, mavContainer);
        }
    }
}
