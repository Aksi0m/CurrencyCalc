package aksiom.currencycalc.internals.mvp;

import java.lang.ref.WeakReference;
import java.util.Date;
import java.util.List;

/**
 * Created by Aksiom on 6/29/2016.
 */
public interface MVP {

    interface Model{
        void cache(List<?> data, Date date);

        void clearCache();

        interface OnDataLoaded<D>{
            void onSuccess(D data);
            void onFail(String error);
        }
    }

    interface View{
        void showLoading();
        void showError(String error);
        void showContent();
        void showEmpty();
    }

    abstract class Presenter<V extends View>{
        private WeakReference<V> view = null;

        public final void setView(V view) {
            if (view == null) throw new NullPointerException("new view must not be null");

            if (this.view != null) dropView(this.view.get());

            this.view = new WeakReference<>(view);
        }

        public final void dropView(V view) {
            if (view == null) throw new NullPointerException("dropped view must not be null");
            this.view = null;
        }

        protected final V getView() {
            if (view == null) throw new NullPointerException("getView called when view is null. " +
                    "Ensure setView(View view) is called first.");
            return view.get();
        }

        public abstract void destroy();
    }
}
