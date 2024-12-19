package com.vupt.SHM.views;

import com.vupt.SHM.views.IWindowController;

public interface BaseController<T> extends IWindowController {
    void initialize();

    void initTableView();

    void createEntityView();

    void updateEntityView();

    void deleteEntityView();

    void save(T paramT);

    void reload();

    void refresh();

    void clear();
}

