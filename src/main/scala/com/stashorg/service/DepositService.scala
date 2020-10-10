package com.stashorg.service
import com.stashorg.model._
import com.stashorg.util.ApplicationLogger

import scala.annotation.tailrec

class DepositService() {

  type DepositFailure = String
  type WalletWithPortfolio = (StashAwaySimpleWallet, Seq[Portfolio])

  val logger = new ApplicationLogger(this.getClass.getSimpleName)

  def depositInCustomerAccount(
    customer: Customer,
    depositAmount: Money
  ): Either[DepositFailure, Customer] =
    (customer.oneTimePlan, customer.monthlyPlan) match {
      case (Some(oneTime), Some(monthly)) =>
        logger.debug("Running one time plan first")
        val (remainingDeposit, updatedPortfolios) =
          runOneTimePlan(depositAmount + customer.wallet.amount, oneTime, customer.portfolios)
        logger.debug("Running monthly plan ")
        val (finalRemainingAmount, updatedPortfoliosAfterMonthlyPlan) =
          runMonthlyPlan(remainingDeposit.amount , monthly, updatedPortfolios)
        Right(
          customer.copy(
            wallet = finalRemainingAmount,
            portfolios = updatedPortfoliosAfterMonthlyPlan
          )
        )
      case (Some(oneTime), None) =>
        logger.debug("running only one time plan")
        val (remainingDeposit, updatedPortfolios) =
          runOneTimePlan(depositAmount + customer.wallet.amount, oneTime, customer.portfolios)
        Right(
          customer
            .copy(wallet = remainingDeposit, portfolios = updatedPortfolios)
        )
      case (None, Some(monthly)) =>
        logger.debug("running only monthly plan")
        val (remainingDeposit, updatedPortfolios) =
          runMonthlyPlan(depositAmount + customer.wallet.amount, monthly, customer.portfolios)
        Right(
          customer
            .copy(wallet = remainingDeposit, portfolios = updatedPortfolios)
        )
      case (None, None) =>
        logger.warn("No plan declared. Please declare at least one plan")
        Left("No plan declared. Please declare at least one plan")
    }

  def runOneTimePlan(actualDeposit: Money,
                     oneTimePlan: OneTimePlan,
                     portfolios: Seq[Portfolio]): WalletWithPortfolio = {
    logger.info(s"amount in one to deposit $actualDeposit")
    if (actualDeposit >= oneTimePlan.getTotalDepositAllocation)
      (
        StashAwaySimpleWallet(
          actualDeposit - oneTimePlan.getTotalDepositAllocation
        ),
        addMoneyInPlan(oneTimePlan, actualDeposit, portfolios)
      )
    else
      (StashAwaySimpleWallet(actualDeposit), portfolios)
  }

  def runMonthlyPlan(actualDeposit: Money,
                     monthlyPlan: MonthlyPlan,
                     portfolios: Seq[Portfolio]): WalletWithPortfolio = {
    logger.info(s"amount in monthly to deposit $actualDeposit")
    @tailrec
    def tailrec(remAmt: Money,
                resultFolio: Seq[Portfolio]): WalletWithPortfolio =
      if (remAmt >= monthlyPlan.getTotalDepositAllocation && monthlyPlan.getTotalDepositAllocation != Money.zero())
        tailrec(
          remAmt - monthlyPlan.getTotalDepositAllocation,
          addMoneyInPlan(monthlyPlan, remAmt, resultFolio)
        )
      else
        (StashAwaySimpleWallet(remAmt), resultFolio)

    tailrec(actualDeposit, portfolios)
  }

  private def addMoneyInPlan(
    plan: DepositPlan,
    deposit: Money,
    folios: Seq[Portfolio]
  ): Seq[Portfolio] = {
    logger.info(
      s"adding amount ${plan.getTotalDepositAllocation} to plan : ${plan.getClass.getSimpleName}"
    )
    @tailrec
    def tailDiv(res: Seq[Portfolio],
                distFolio: Seq[Portfolio]): Seq[Portfolio] = distFolio match {
      case h :: tail => tailDiv(updateAmount(h, res), tail)
      case Nil       => res
    }

    tailDiv(folios, plan.portfolios)
  }

  private def updateAmount(portfolio: Portfolio,
                           portfolios: Seq[Portfolio]): Seq[Portfolio] =
    portfolios.map {
      case folio if folio.name == portfolio.name =>
        folio.addMoney(portfolio.amount)
      case folio => folio
    }

}
