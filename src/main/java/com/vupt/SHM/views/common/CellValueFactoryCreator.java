package com.vupt.SHM.views.common;

import com.vupt.SHM.constant.DatetimePattern;
import com.vupt.SHM.constant.DepartmentType;
import com.vupt.SHM.constant.EquipmentStatus;
import com.vupt.SHM.dto.CategoryDto;
import com.vupt.SHM.dto.DepartmentDto;
import com.vupt.SHM.dto.EmployeeDto;
import com.vupt.SHM.dto.EquipmentDto;
import com.vupt.SHM.entity.AppAuthority;
import com.vupt.SHM.entity.AppFunction;
import com.vupt.SHM.entity.Department;
import com.vupt.SHM.entity.EquipmentHistory;
import com.vupt.SHM.entity.Role;

import java.util.Date;

import com.vupt.SHM.utils.DateTimeUtils;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;


public class CellValueFactoryCreator {
    public static void setEmployeeTableViewCellFactory(TableColumn<EmployeeDto, Long> colId, TableColumn<EmployeeDto, String> colName, TableColumn<EmployeeDto, String> colUsername, TableColumn<EmployeeDto, DepartmentDto> colDepartment, TableColumn<EmployeeDto, String> colContact, TableColumn<EmployeeDto, Boolean> colIsManager) {
        if (colId != null)
            colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        if (colName != null)
            colName.setCellValueFactory(new PropertyValueFactory<>("fullName"));

        if (colUsername != null)
            colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));

        if (colDepartment != null) {
            colDepartment.setCellValueFactory(new PropertyValueFactory<>("departmentDto"));
            colDepartment.setCellFactory(column -> {
                return new TableCell<EmployeeDto, DepartmentDto>() {
                    @Override
                    protected void updateItem(DepartmentDto item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setText(null);
                        } else {
                            setText(item.getName()); // Hiển thị tên sản phẩm
                        }
                    }
                };
            });
        }

        if (colContact != null)
            colContact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        if (colIsManager != null) {
            colIsManager.setCellValueFactory(param -> new SimpleBooleanProperty(((EmployeeDto) param.getValue()).isManager()));
            colIsManager.setCellFactory(CheckBoxTableCell.forTableColumn(colIsManager));
        }
    }


    public static void setEquipmentTableViewCellFactory(TableColumn<EquipmentDto, Long> colId, TableColumn<EquipmentDto, String> colName, TableColumn<EquipmentDto, String> colCode, TableColumn<EquipmentDto, Integer> colYear, TableColumn<EquipmentDto, EquipmentStatus> colStatus, TableColumn<EquipmentDto, Integer> colCount, TableColumn<EquipmentDto, Long> colPrice, TableColumn<EquipmentDto, CategoryDto> colCategory, TableColumn<EquipmentDto, DepartmentDto> colDepartment, TableColumn<EquipmentDto, String> colNote) {

        if (colId != null)
            colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        if (colName != null) {

            colName.setCellValueFactory(new PropertyValueFactory<>("name"));

            colName.setCellFactory((Callback<TableColumn<EquipmentDto, String>, TableCell<EquipmentDto, String>>) new Object());
        }


        if (colCode != null)
            colCode.setCellValueFactory(new PropertyValueFactory<>("code"));

        if (colYear != null) {

            colYear.setCellValueFactory(new PropertyValueFactory<>("year"));

            colYear.setCellFactory(column -> {
                return new TableCell<EquipmentDto, Integer>() {
                    @Override
                    protected void updateItem(Integer item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setText(null);
                        } else {
                            setText(item == -1 ? "" : item.toString()); // Hiển thị tên sản phẩm
                        }
                    }
                };
            });
        }
        if (colStatus != null) {
            colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
            colStatus.setCellFactory(column -> {
                return new TableCell<EquipmentDto, EquipmentStatus>() {
                    @Override
                    protected void updateItem(EquipmentStatus item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setText(null);
                        } else {
                            setText(item.getTitle()); // Hiển thị tên sản phẩm
                        }
                    }
                };
            });
        }

        if (colCount != null) {

            colCount.setCellValueFactory(new PropertyValueFactory<>("count"));
        }
        if (colPrice != null) {
            colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
            colPrice.setCellFactory(column -> {
                return new TableCell<EquipmentDto, Long>() {
                    @Override
                    protected void updateItem(Long item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setText(null);
                        } else {
                            setText(item == -1 ? "" : item.toString()); // Hiển thị tên sản phẩm
                        }
                    }
                };
            });
        }

        if (colCategory != null) {
            colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
            colCategory.setCellFactory(column -> {
                return new TableCell<EquipmentDto, CategoryDto>() {
                    @Override
                    protected void updateItem(CategoryDto item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setText(null);
                        } else {
                            setText(item.getName()); // Hiển thị tên sản phẩm
                        }
                    }
                };
            });
        }

        if (colDepartment != null) {
            colDepartment.setCellValueFactory(new PropertyValueFactory<>("department"));
            colDepartment.setCellFactory(column -> {
                return new TableCell<EquipmentDto, DepartmentDto>() {
                    @Override
                    protected void updateItem(DepartmentDto item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setText(null);
                        } else {
                            setText(item.getName()); // Hiển thị tên sản phẩm
                        }
                    }
                };
            });
        }

        if (colNote != null) {
            colNote.setCellValueFactory(new PropertyValueFactory<>("note"));
        }
    }


    public static void setDepartmentTableViewCellFactory(TableColumn<DepartmentDto, Long> colId, TableColumn<DepartmentDto, String> colName, TableColumn<DepartmentDto, String> colCode, TableColumn<DepartmentDto, DepartmentType> colType, TableColumn<DepartmentDto, Boolean> colIsSuspended, TableColumn<DepartmentDto, DepartmentDto> colParent) {
        if (colId != null) {
            colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        }
        if (colName != null) {
            colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        }
        if (colCode != null) {
            colCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        }
        if (colType != null) {
            colType.setCellValueFactory(new PropertyValueFactory<>("type"));
            colType.setCellFactory(column -> {
                return new TableCell<DepartmentDto, DepartmentType>() {
                    @Override
                    protected void updateItem(DepartmentType item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setText(null);
                        } else {
                            setText(item.getTitle()); // Hiển thị tên sản phẩm
                        }
                    }
                };
            });
        }

        if (colIsSuspended != null) {
            colIsSuspended.setCellValueFactory(param -> new SimpleBooleanProperty(((DepartmentDto) param.getValue()).isSuspended()));
            colIsSuspended.setCellFactory(CheckBoxTableCell.forTableColumn(colIsSuspended));
        }

        if (colParent != null) {
            colParent.setCellValueFactory(new PropertyValueFactory<>("parent"));
            colParent.setCellFactory(column -> {
                return new TableCell<DepartmentDto, DepartmentDto>() {
                    @Override
                    protected void updateItem(DepartmentDto item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setText(null);
                        } else {
                            setText(item.getName()); // Hiển thị tên sản phẩm
                        }
                    }
                };
            });
        }
    }


    public static void setEquipmentHistoryTableViewCellFactory(TableColumn<EquipmentHistory, Long> colId, TableColumn<EquipmentHistory, String> colName, TableColumn<EquipmentHistory, String> colCode, TableColumn<EquipmentHistory, Department> colDepartmentOld, TableColumn<EquipmentHistory, Department> colDepartmentNew, TableColumn<EquipmentHistory, Date> colTransportDate, TableColumn<EquipmentHistory, String> colMessage) {
        if (colId != null)
            colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        if (colName != null) {
            colName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEquipment().getName()));
        }
        if (colCode != null) {
            colCode.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEquipment().getCode()));
        }
        if (colDepartmentOld != null) {
            colDepartmentOld.setCellValueFactory(new PropertyValueFactory<>("fromDepartment"));
            colDepartmentOld.setCellFactory(column -> {
                return new TableCell<EquipmentHistory, Department>() {
                    @Override
                    protected void updateItem(Department item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setText(null);
                        } else {
                            setText(item.getName()); // Hiển thị tên sản phẩm
                        }
                    }
                };
            });
        }
        if (colDepartmentNew != null) {
            colDepartmentNew.setCellValueFactory(new PropertyValueFactory<>("toDepartment"));
            colDepartmentNew.setCellFactory(column -> {
                return new TableCell<EquipmentHistory, Department>() {
                    @Override
                    protected void updateItem(Department item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setText(null);
                        } else {
                            setText(item.getName()); // Hiển thị tên sản phẩm
                        }
                    }
                };
            });
        }
        if (colTransportDate != null) {
            colTransportDate.setCellValueFactory(new PropertyValueFactory<>("transportDate"));
            colTransportDate.setCellFactory(column -> {
                return new TableCell<EquipmentHistory, Date>() {
                    @Override
                    protected void updateItem(Date item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setText(null);
                        } else {
                            setText(DateTimeUtils.format(DatetimePattern.DATETIME, item)); // Hiển thị tên sản phẩm
                        }
                    }
                };
            });
        }
        if (colMessage != null) {
            colMessage.setCellValueFactory(new PropertyValueFactory<>("message"));
        }
    }


    public static void setAppAuthorityTableViewCellFactory(TableColumn<AppAuthority, Long> colId, TableColumn<AppAuthority, String> colName, TableColumn<AppAuthority, String> colCode, TableColumn<AppAuthority, AppFunction> colAppFunction) {
        if (colId != null)
            colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        if (colName != null)
            colName.setCellValueFactory(new PropertyValueFactory<>("name"));

        if (colCode != null) {

            colCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        }
        if (colAppFunction != null) {

            colAppFunction.setCellValueFactory(new PropertyValueFactory<>("appFunction"));
            colAppFunction.setCellFactory(column -> {
                return new TableCell<AppAuthority, AppFunction>() {
                    @Override
                    protected void updateItem(AppFunction item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setText(null);
                        } else {
                            setText(item.getName()); // Hiển thị tên sản phẩm
                        }
                    }
                };
            });
        }
    }


    public static void setRoleTableViewCellFactory(TableColumn<Role, Long> colId, TableColumn<Role, String> colName, TableColumn<Role, String> colCode) {
        if (colId != null) {
            colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        }
        if (colName != null)
            colName.setCellValueFactory(new PropertyValueFactory<>("name"));

        if (colCode != null)
            colCode.setCellValueFactory(new PropertyValueFactory<>("code"));
    }
}

