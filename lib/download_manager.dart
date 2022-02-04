import 'dart:async';

import 'package:flutter/services.dart';

class DownloadManager {
  static const _channel = MethodChannel('download_manager');

  static Future<int?> enqueue({
    required String url,
    required String path,
    required String name,
  }) async {
    return _channel.invokeMethod('enqueue', {
      'url': url,
      'path': path,
      'name': name,
    });
  }
}
