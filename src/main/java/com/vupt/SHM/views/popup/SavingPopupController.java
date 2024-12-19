package com.vupt.SHM.views.popup;

public interface SavingPopupController<T> {
  void save();
  
  void close();
  
  T getValueFromForm();
  
  void setValueToForm();
  
  boolean checkValidation();
}
