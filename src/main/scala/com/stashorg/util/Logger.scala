package com.stashorg.util

trait Logger {
  val logger = new Logger(this.getClass.getSimpleName)
    class Logger(appName: String) {
      def info(msg: String) = println(s"[INFO]: $appName - $msg")
      def warn(msg: String) = println(s"[WARN]: $appName - $msg")
      def error(msg: String) = println(s"[ERROR]: $appName - $msg !!")
    }
}