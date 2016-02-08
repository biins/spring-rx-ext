package org.biins.spring.ext.rx.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import rx.Observable;

/**
 * @author Martin Janys
 */
@Controller
@RequestMapping("/api")
public class ObservableStringController {

    @ResponseBody
    @RequestMapping(value = "/async", produces = "application/text")
    public Observable<String> async() {
        return Observable.just("hello");
    }

}
