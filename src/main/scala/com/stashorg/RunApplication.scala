package com.stashorg

import com.stashorg.model._
import com.stashorg.service.CustomerService
import com.stashorg.service.repository.InMemoryRepositoryForCustomer
import com.stashorg.util.ConsoleLogger

object RunApplication {

  def main(args: Array[String]): Unit = {

    val logger = new ConsoleLogger(this.getClass.getCanonicalName)

    val retirementPortfolio = RetirementPortfolio(Money(500, SGP))
    val emergencyPortfolio = EmergencyPortfolio(Money(10000, SGP))

    val customer1 = Customer(
      ReferenceNumber(11111),
      StashAwaySimpleWallet(Money(0)),
      Some(OneTimePlan(List(retirementPortfolio, emergencyPortfolio))),
      None,
      List(
        retirementPortfolio.copy(amount = Money(100, SGP)),
        emergencyPortfolio
      )
    )

    val customer = Customer(
      ReferenceNumber(22222),
      StashAwaySimpleWallet(Money(0)),
      Some(OneTimePlan(List(retirementPortfolio, emergencyPortfolio))),
      Some(
        MonthlyPlan(
          List(
            RetirementPortfolio(Money(100)),
            EmergencyPortfolio(Money.zero()),
            HighRiskPortfolio(Money(100))
          )
        )
      ),
      List(
        retirementPortfolio.copy(amount = Money(0)),
        emergencyPortfolio.copy(amount = Money(0)),
        HighRiskPortfolio(Money(400))
      )
    )

    val inMemoryCustomerRepository = InMemoryRepositoryForCustomer(customer, customer1)

    val customerService = new CustomerService(inMemoryCustomerRepository)

    def processResult(result: DepositResult): Unit =
      result match {
        case DepositFailure(err) =>
          logger.error(s"Transaction failed with error : $err")
        case DepositSuccess(_) => logger.info("Transaction successful")
      }

    processResult(
      customerService
        .findCustomerAndDeposit(ReferenceNumber(11111), Money(10700))
    )

    logger.info(s"showing info for customer : ")
    inMemoryCustomerRepository.showAllRecordsInRepository()

    processResult(
      customerService
        .findCustomerAndDeposit(ReferenceNumber(22222), Money(10750))
    )
    inMemoryCustomerRepository.showAllRecordsInRepository()

    processResult(
      customerService
        .findCustomerAndDeposit(ReferenceNumber(22222), Money(650))
    )
    logger.info(s"showing info for customer : ")
    inMemoryCustomerRepository.showAllRecordsInRepository()
  }

}
