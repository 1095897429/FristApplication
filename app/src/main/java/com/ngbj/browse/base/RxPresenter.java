package com.ngbj.browse.base;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Date:2018/7/18
 * author:zl
 * 备注：实现了基础契约类中的基本业务逻辑【View绑定与解绑】
 *       每个界面的逻辑实现它就可以避免了View 同样的操作
 *       请求过程中Activity已经退出，如果回到主线程更新UI，APP肯定崩溃 -- 切断水管
 */
public class RxPresenter<T extends BaseContract.BaseView> implements BaseContract.BasePresenter<T> {

    protected T mView;//实例变量
    private CompositeDisposable mCompositeDisposable;

    public void addSubscribe(Disposable disposable){
        if(mCompositeDisposable == null){
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(disposable);
    }

    public void removeSubscribe(Disposable disposable){
        if(mCompositeDisposable != null)
            mCompositeDisposable.remove(disposable);
    }

    /** dispose() 即可切断所有的水管，使下游收不到事件，就不会去更新UI了 */
    private void unSubscribe() {
        if(mCompositeDisposable != null){
            mCompositeDisposable.dispose();
        }
    }

    @Override
    public void attachView(T view) {
        mView = view;
    }

    /** 当p和view之间分离，则取消订阅  */
    @Override
    public void detachView() {
        mView = null;
        unSubscribe();
    }
}