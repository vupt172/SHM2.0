package com.vupt.SHM.views.component;
 
 import com.vupt.SHM.model.TreeNode;
 import javafx.scene.control.cell.CheckBoxTreeCell;
 
 public class AppCheckBoxTreeCell extends CheckBoxTreeCell<TreeNode> {
   public void updateItem(TreeNode item, boolean empty) {
  super.updateItem(item, empty);
     
     if (empty || item == null) {
      setText(null);
     } else {
       
      setText(item.toString());
     } 
   }
 
   
   public void updateSelected(boolean selected) {
  super.updateSelected(selected);
   }
 }
