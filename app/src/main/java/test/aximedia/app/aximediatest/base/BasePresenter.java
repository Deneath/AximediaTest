package test.aximedia.app.aximediatest.base;

public class BasePresenter<V extends IView> {

    protected V view;

    public void setView(V view){
        this.view = view;
    }
}
