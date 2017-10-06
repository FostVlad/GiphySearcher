package com.goloveschenko.gifsearcher.domain;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public abstract class UseCase<Request, Response> {
    private Disposable disposable;

    protected abstract Observable<Response> build(Request request);

    public void execute(Request request, DisposableObserver<Response> disposableObserver) {
        disposable = build(request)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(disposableObserver);
    }

    public void dispose() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }
}
