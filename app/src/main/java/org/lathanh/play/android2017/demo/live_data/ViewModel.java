package org.lathanh.play.android2017.demo.live_data;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

/**
 * The data as it should be displayed.
 * While it often has a strong resemblance to a data model, it may also often:
 * <ul>
 *   <li>transform/format the data for display,</li>
 *   <li>contain data from multiple data models, and</li>
 *   <li>contain data/state not found in and data model.</li>
 * </ul>
 *
 * The {@code @Bindable} annotations are used by the DataBinding demos and
 * have no effect on the other demos.
 */
public class ViewModel extends BaseObservable {
  private String string;
  private String delay;

  public ViewModel(String string, String delay) {
    this.string = string;
    this.delay = delay;
  }

  @Bindable
  public String getString() {
    return string;
  }

  @Bindable
  public String getDelay() {
    return delay;
  }
}
