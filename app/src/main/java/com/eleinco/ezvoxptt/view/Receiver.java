package com.eleinco.ezvoxptt.view;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.view.KeyEvent;

import android.content.Context;

public class Receiver extends BroadcastReceiver {

  @Override
  public void onReceive(Context context, Intent intent) {

    Intent service = new Intent(context, Servicio.class);
    context.startService(service);
  }
} 
