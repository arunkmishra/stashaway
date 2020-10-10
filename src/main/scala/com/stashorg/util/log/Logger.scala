package com.stashorg.util.log

trait Logger {

  val appName: String

  def info(msg: String)
  def warn(msg: String)
  def error(msg: String)
  def debug(msg: String)

}
