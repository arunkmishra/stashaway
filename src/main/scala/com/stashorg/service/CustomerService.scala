package com.stashorg.service

import com.stashorg.model._
import com.stashorg.util.Logger

import scala.annotation.tailrec


class CustomerService(customers: List[Customer]) extends Logger {
  type WalletWithPortfolio = (Wallet, List[Portfolio])
  //val logger = new Logger("customer-service")

  def deposit(referenceNumber: ReferenceNumber, depositAmount: Money): Option[Customer] = {
    val customer = findCustomer(referenceNumber)
    val res = if(customer.isDefined){
      logger.info(s"found customer ${customer.get.referenceNumber} and starting deposit processing")
      Some(processCustomerDeposit(customer.get, depositAmount))
    }
    else{
      logger.error(s"customer with ref no: $referenceNumber not found")
      None
    }
    logger.info("------------------------------------")
    res
  }

  def processCustomerDeposit(customer: Customer, depositAmount: Money): Customer = {
    val otp = customer.oneTimePlan.getOrElse(OneTimePlan.emptyOneTimePlan)
    val mp = customer.monthlyPlan.getOrElse(MonthlyPlan.emptyMonthlyPlan)
    val res = runPlan(depositAmount + customer.wallet.amount, otp, mp, customer.portfolios) /*match {
      case Left(err) => println(s"Deposit Failed due to: $err")
        customer
      case Right(v) => customer.copy(portfolios = v)
    }*/
    customer.copy(wallet = res._1, portfolios = res._2)
  }

  def findCustomer(referenceNumber: ReferenceNumber): Option[Customer] =
    customers
      .find(customer => customer.referenceNumber.number == referenceNumber.number)


  def runPlan(actualDeposit: Money, oneTimePlan: OneTimePlan, monthlyPlan: MonthlyPlan, portfolio: List[Portfolio])
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

  def addMoneyInPlan(plan: DepositPlan, deposit: Money, folios: List[Portfolio])(implicit ev: Ordering[Money])
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

  def updateAmount(portfolio: Portfolio, portfolios: List[Portfolio]): List[Portfolio] = {
    portfolios.map{
      case folio if folio.name == portfolio.name => folio.addMoney(portfolio.amount)
      case folio => folio
    }
  }


}
