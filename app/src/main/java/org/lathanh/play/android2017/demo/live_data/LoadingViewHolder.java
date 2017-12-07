package org.lathanh.play.android2017.demo.live_data;

import android.support.v7.widget.RecyclerView;

/**
 * The ViewHolder when using Data Binding is simple; it just uses the layout's
 * ViewDataBinding to do the binding.
 */
public class LoadingViewHolder extends RecyclerView.ViewHolder {
  private AdaptingDemoDataBindingImprovedListItemBinding binding;

  public LoadingViewHolder(AdaptingDemoDataBindingImprovedListItemBinding
                               binding) {
    super(binding.getRoot());
    this.binding = binding;
  }

  public void setLoadingViewModel(DataBindingDemo_Models.LoadingViewModel loadingViewModel) {
    binding.setLoadingViewModel(loadingViewModel);
  }
} // class LoadingViewHolder
