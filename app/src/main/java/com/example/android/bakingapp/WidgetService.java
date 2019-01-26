package com.example.android.bakingapp;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import  	android.widget.RemoteViewsService;

public class WidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return (new ListProvider(this.getApplicationContext(), intent));
    }

}
