package com.stashorg.util
import java.util.Calendar

import com.stashorg.util.log.Logger

class ConsoleLogger(name: String) extends Logger {

  override def appName: String = name
  def info(msg: String) =
    println(s"${Calendar.getInstance().getTime} [$appName]: INFO - $msg")
  def warn(msg: String) =
    println(s"${Calendar.getInstance().getTime} [$appName]: WARN - $msg")
  def error(msg: String) =
    println(s"${Calendar.getInstance().getTime} [$appName]: ERROR - $msg !!")
  def debug(msg: String) =
    println(s"${Calendar.getInstance().getTime} [$appName]: DEBUG - $msg")

}
