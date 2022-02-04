package com.robertodoering.download_manager;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;

import java.io.File;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.Result;

public class DownloadManagerPlugin implements FlutterPlugin, MethodChannel.MethodCallHandler {
  private DownloadManager manager;

  private MethodChannel methodChannel;

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    final Context context = flutterPluginBinding.getApplicationContext();
    final BinaryMessenger messenger = flutterPluginBinding.getBinaryMessenger();

    manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

    methodChannel = new MethodChannel(messenger, "download_manager");
    methodChannel.setMethodCallHandler(this);
  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    if (call.method.equals("enqueue")) {
      final String url = call.argument("url");
      final String path = call.argument("path");
      final String name = call.argument("name");

      try {
        result.success(enqueue(url, path, name));
      } catch (Exception e) {
        result.error("1", e.toString(), null);
      }
    } else {
      result.notImplemented();
    }
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    methodChannel.setMethodCallHandler(null);
  }

  private long enqueue(String url, String path, String name) {
    final DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
    request.allowScanningByMediaScanner();
    request.setDestinationUri(Uri.fromFile(new File(path, name)));
    request.setTitle(name);
    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

    return manager.enqueue(request);
  }
}
