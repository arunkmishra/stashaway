package com.stashorg.model

trait DepositPlan {
  def portfolios: List[Portfolio]

  def getTotalDepositAllocation: Money =
    portfolios.foldLeft(Money.zero())((acc, folio) => folio.sum(acc))

}

case class OneTimePlan(portfolios: List[Portfolio]) extends DepositPlan
object OneTimePlan {
  val emptyOneTimePlan: OneTimePlan = OneTimePlan(Nil)
}

case class MonthlyPlan(portfolios: List[Portfolio]) extends DepositPlan
object MonthlyPlan {
  val emptyMonthlyPlan: MonthlyPlan = MonthlyPlan(Nil)
}

