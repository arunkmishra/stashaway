package com.stashorg.service

import com.stashorg.model._
import com.stashorg.util.Logger

import scala.annotation.tailrec


class CustomerService(customers: List[Customer]) extends Logger {
  type WalletWithPortfolio = (Wallet, List[Portfolio])

  def findCustomerAndDeposit(referenceNumber: ReferenceNumber, depositAmount: Money): Option[Customer] =
    findCustomer(referenceNumber) match {
      case Some(customer) => logger.info(s"found customer ${customer.referenceNumber} and starting deposit processing")
        Some(processCustomerDeposit(customer, depositAmount))
      case _ => logger.error(s"customer with ref no: $referenceNumber not found")
        None
    }



  private def processCustomerDeposit(customer: Customer, depositAmount: Money): Customer = {
    val otp = customer.oneTimePlan.getOrElse(OneTimePlan.emptyOneTimePlan)
    val mp = customer.monthlyPlan.getOrElse(MonthlyPlan.emptyMonthlyPlan)
    val res = runPlan(depositAmount + customer.wallet.amount, otp, mp, customer.portfolios)
    customer.copy(wallet = res._1, portfolios = res._2)
  }

  private def findCustomer(referenceNumber: ReferenceNumber): Option[Customer] =
    customers
      .find(customer => customer.referenceNumber.number == referenceNumber.number)


  private def runPlan(actualDeposit: Money, oneTimePlan: OneTimePlan, monthlyPlan: MonthlyPlan, portfolio: List[Portfolio])
  : WalletWithPortfolio = {
    val (remainingDeposit, folios) = if(actualDeposit >= oneTimePlan.getTotalDepositAllocation) {
      logger.info("remaining = " + (actualDeposit - oneTimePlan.getTotalDepositAllocation))
      (actualDeposit - oneTimePlan.getTotalDepositAllocation, addMoneyInPlan(oneTimePlan, actualDeposit, portfolio).right.get)
    } else (actualDeposit, portfolio)

    def tailrec(remAmt: Money, resultFolio: List[Portfolio]): WalletWithPortfolio =
      if(remAmt >= monthlyPlan.getTotalDepositAllocation && monthlyPlan.getTotalDepositAllocation != Money.zero()){
        val amt = remAmt - monthlyPlan.getTotalDepositAllocation
        val resFolio = addMoneyInPlan(monthlyPlan, remAmt, resultFolio) match {
          case Right(v) => v
          case _ => resultFolio
        }
        tailrec(amt, resFolio)
      } else (Wallet(remAmt), resultFolio)
    tailrec(remainingDeposit, folios)
  }

  private def addMoneyInPlan(plan: DepositPlan, deposit: Money, folios: List[Portfolio])(implicit ev: Ordering[Money])
  : Either[Money, List[Portfolio]] = {

    logger.info(s"adding amount to plan : ${plan.toString}")
    @tailrec
    def tailDiv(res: List[Portfolio], distFolio: List[Portfolio]): List[Portfolio] = distFolio match {
      case h :: tail => tailDiv(updateAmount(h, res), tail)
      case Nil => res
    }

    if(plan.getTotalDepositAllocation >  deposit) {
      logger.warn("Not enough money to invest so keeping it safe in wallet(You can use this money in next transaction)")
      Left(Money(deposit))
    }
    else{
      val op = tailDiv(folios, plan.portfolios)
      logger.info("added deposit in folios according to plan")
      Right(op)
    }

  }

  private def updateAmount(portfolio: Portfolio, portfolios: List[Portfolio]): List[Portfolio] = {
    portfolios.map{
      case folio if folio.name == portfolio.name => folio.addMoney(portfolio.amount)
      case folio => folio
    }
  }


}
