package org.lathanh.play.android2017.demo.live_data;

/**
 * The model of the data as it comes from the data source.
 * In other words, this is a Java object that closely matches the data
 * source's model, and simply holds that data.
 */
public class DataModel {
  private final String name;
  private final int delayMs;

  public DataModel(int position, int delayMs) {
    this.name = Integer.toString(position);
    this.delayMs = delayMs;
  }

  public String getName() {
    return name;
  }

  public int getDelayMs() {
    return delayMs;
  }
}
