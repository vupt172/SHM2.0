package com.vupt.SHM.model;

import com.vupt.SHM.constant.TreeNodeType;
import com.vupt.SHM.entity.AppAuthority;
import com.vupt.SHM.entity.AppFunction;
import com.vupt.SHM.entity.Role;
import lombok.Data;

@Data
public class TreeNode {
    private Object content;
    private TreeNodeType type;

    public TreeNode(Object content, TreeNodeType type) {
        this.content = content;
        this.type = type;
    }


    public String toString() {
        switch (type) {
            case APP_FUNCTION:
                return ((AppFunction) this.content).getName();
            case ROLE:
                return ((Role) this.content).getName();
            case APP_AUTHORITY:
                return ((AppAuthority) this.content).getName();
            case COMMON:
                return this.content.toString();
        }
        return "";
    }
}

