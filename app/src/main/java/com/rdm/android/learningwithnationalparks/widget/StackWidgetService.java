package com.rdm.android.learningwithnationalparks.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class StackWidgetService extends RemoteViewsService {

    public StackWidgetService() {
    }

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new StackRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}






