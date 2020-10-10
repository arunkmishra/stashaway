package com.stashorg

import com.stashorg.model._
import com.stashorg.service.CustomerService
import com.stashorg.util.Logger

object RunApplication extends Logger {

  def main(args: Array[String]): Unit = {

    logger.info("---------- Starting Process ----------------")
    val retirementPortfolio = RetirementPortfolio(Money(500, SGP))
    val emergencyPortfolio = EmergencyPortfolio(Money(10000, SGP))

    var customer1 = Customer(
      ReferenceNumber(11),
      Wallet(Money(0)),
      Some(OneTimePlan(List(retirementPortfolio, emergencyPortfolio))), None,
      List(retirementPortfolio.copy(amount = Money(100, SGP)), emergencyPortfolio))

    var customer = Customer(
      ReferenceNumber(12),
      Wallet(Money(0)),
      Some(OneTimePlan(List(retirementPortfolio, emergencyPortfolio))),
      Some(MonthlyPlan(List(RetirementPortfolio(Money(100)), EmergencyPortfolio(Money.zero()), HighRiskPortfolio(Money(100))))),
      List(retirementPortfolio.copy(amount = Money(0)), emergencyPortfolio.copy(amount = Money(0)), HighRiskPortfolio(Money(400))))

    val customerService = new CustomerService(List(customer1, customer))

    customer1 = customerService.processCustomerDeposit(customer1, Money(10700))
    logger.info(s"showing info for customer : ${customer1}")

    customer = customerService.processCustomerDeposit(customer, Money(10750))
    customer = customerService.processCustomerDeposit(customer, Money(150))
    logger.info(s"showing info for customer : ${customer} ")
  }


}
