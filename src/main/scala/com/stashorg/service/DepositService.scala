package com.stashorg.service

import com.stashorg.model._
import com.stashorg.service.deposit.{
  MonthlyPlanDepositAllocation,
  OneTimeDepositAllocation
}
import com.stashorg.util.ConsoleLogger

class DepositService() {

  private val logger = new ConsoleLogger(this.getClass.getCanonicalName)

  def depositInCustomerAccount(
    customer: Customer,
    depositAmount: Money
  ): Either[DepositFailure, Customer] =
    (customer.oneTimePlan, customer.monthlyPlan) match {
      case (Some(oneTime), Some(monthly)) =>
        logger.debug(
          s"Running one time plan first"
        )
        val (remainingDeposit, updatedPortfolios) =
          OneTimeDepositAllocation(oneTime).runPlanForDepositPlan(
            depositAmount + customer.wallet.amount,
            customer.portfolios
          )
        logger.debug("Running monthly plan ")
        val (finalRemainingAmount, updatedPortfoliosAfterMonthlyPlan) =
          MonthlyPlanDepositAllocation(monthly)
            .runPlanForDepositPlan(remainingDeposit.amount, updatedPortfolios)
        Right(
          customer.copy(
            wallet = finalRemainingAmount,
            portfolios = updatedPortfoliosAfterMonthlyPlan
          )
        )
      case (Some(oneTime), None) =>
        logger.debug("running only one time plan")
        val (remainingDeposit, updatedPortfolios) =
          OneTimeDepositAllocation(oneTime).runPlanForDepositPlan(
            depositAmount + customer.wallet.amount,
            customer.portfolios
          )
        Right(
          customer
            .copy(wallet = remainingDeposit, portfolios = updatedPortfolios)
        )
      case (None, Some(monthly)) =>
        logger.debug("running only monthly plan")
        val (remainingDeposit, updatedPortfolios) =
          MonthlyPlanDepositAllocation(monthly).runPlanForDepositPlan(
            depositAmount + customer.wallet.amount,
            customer.portfolios
          )
        Right(
          customer
            .copy(wallet = remainingDeposit, portfolios = updatedPortfolios)
        )
      case (None, None) =>
        logger.warn("No plan declared. Please declare at least one plan")
        Left(
          DepositFailure("No plan declared. Please declare at least one plan")
        )
    }

}
