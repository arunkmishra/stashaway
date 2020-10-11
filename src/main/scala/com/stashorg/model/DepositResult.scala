package com.stashorg.model

trait DepositResult

case class DepositFailure(err: String) extends DepositResult
case class DepositSuccess(msg: String = "Successfully allocated") extends DepositResult
