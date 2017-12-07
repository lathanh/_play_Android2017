package org.lathanh.play.android2017.demo.live_data;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/** A ViewHolder for the adapting_demo_list_item layout. */
public class ViewHolder extends RecyclerView.ViewHolder {

  public final TextView label;
  public final TextView delay;
  public final TextView bind;

  public ViewHolder(View itemView) {
    super(itemView);
    this.label = (TextView) itemView.findViewById(R.id.label);
    this.delay = (TextView) itemView.findViewById(R.id.delay);
    this.bind = (TextView) itemView.findViewById(R.id.bind);
  }
}
