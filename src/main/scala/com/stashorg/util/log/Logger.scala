package com.stashorg.util.log

trait Logger {

  def appName: String

  def info(msg: String)
  def warn(msg: String)
  def error(msg: String)
  def debug(msg: String)

}
