package org.lathanh.play.android2017.demo.live_data;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.annotation.Nullable;

import org.lathanh.play.android2017.BR;

/**
 * The LoadingViewModel is a ViewModel that allows the view to know when to
 * show a loading indicator until the actual ViewModel is ready.
 *
 * The {@link BaseObservable} class that this extends and the {@code Bindable}
 * annotations are used by the DataBinding demos and have no effect on the
 * other demos.
 */
public class LoadingViewModel
    extends BaseObservable {

  protected final DataModel dataModel;
  protected ViewModel viewModel;
  private long onBindTimeNanos;

  public LoadingViewModel(DataModel dataModel) {
    this.dataModel = dataModel;
  }

  public LoadingViewModel(DataModel dataModel) {
    super(dataModel);
  }

  public void setOnBindTimeNanos(long onBindTimeNanos) {
    this.onBindTimeNanos = onBindTimeNanos;
  }

  @Bindable
  public String getBindTime() {
    long elapsed = System.nanoTime() - onBindTimeNanos;
    return String.format("%.2f ms", elapsed / 1000000f);
  }

  /**
   * When the ViewModel has become ready, notify that all the properties that
   * are from that have changed.
   */
  public void setViewModel(ViewModel viewModel) {
    super.setViewModel(viewModel);
    notifyPropertyChanged(BR.viewModel);
    notifyPropertyChanged(BR.string);
    notifyPropertyChanged(BR.delay);
    notifyPropertyChanged(BR.bindTime);
  }


  /**
   * @return {@code null} if the ViewModel hasn't been created (from the
   *         {@link #getDataModel() DataModel} yet.
   */
  @Bindable
  @Nullable
  public ViewModel getViewModel() {
    return viewModel;
  }

  public void setViewModel(ViewModel viewModel) {
    this.viewModel = viewModel;
  }

  public DataModel getDataModel() {
    return dataModel;
  }

  @SuppressWarnings("unused")
  public boolean isAdapted() {
    return viewModel != null;
  }
}
