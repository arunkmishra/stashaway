package com.stashorg.model

sealed trait DepositPlan {
  def portfolios: Seq[Portfolio]

  def getTotalDepositAllocation: Money =
    portfolios.foldLeft(Money.zero())((acc, folio) => folio.sum(acc))

}

case class OneTimePlan(portfolios: Seq[Portfolio]) extends DepositPlan
case class MonthlyPlan(portfolios: Seq[Portfolio]) extends DepositPlan
