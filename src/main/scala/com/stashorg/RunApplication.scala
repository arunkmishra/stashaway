package com.stashorg

import com.stashorg.model._
import com.stashorg.service.{CustomerRepository, CustomerService}
import com.stashorg.util.ApplicationLogger

object RunApplication {

  def main(args: Array[String]): Unit = {

    val logger = new ApplicationLogger(this.getClass.getSimpleName)

    val retirementPortfolio = RetirementPortfolio(Money(500, SGP))
    val emergencyPortfolio = EmergencyPortfolio(Money(10000, SGP))

    var customer1 = Customer(
      ReferenceNumber(11111),
      StashAwaySimpleWallet(Money(0)),
      Some(OneTimePlan(List(retirementPortfolio, emergencyPortfolio))),
      None,
      List(
        retirementPortfolio.copy(amount = Money(100, SGP)),
        emergencyPortfolio
      )
    )

    var customer = Customer(
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

    var customerRepository: CustomerRepository = CustomerRepository(customer, customer1)

    //customer change
    val customerService = new CustomerService(customerRepository)

    def processResult(result: Either[String, CustomerRepository]): Unit =
      result.fold(err => logger.warn(s"transaction failed with err: $err"), c => customerRepository = c)

    processResult(customerService
      .findCustomerAndDeposit(ReferenceNumber(11111), Money(10700)))

    logger.info(s"showing info for customer : ")
    customerRepository.showAllCustomersInRepository()

    processResult(customerService
      .findCustomerAndDeposit(ReferenceNumber(22222), Money(10750)))
    customerRepository.showAllCustomersInRepository()

    processResult(customerService
      .findCustomerAndDeposit(ReferenceNumber(22222), Money(150)))
    logger.info(s"showing info for customer : ")
    customerRepository.showAllCustomersInRepository()
  }

}
/*
Sun Oct 11 03:51:26 IST 2020 [CustomerService]: INFO - depositing 10700 SGP to customer with ref no. : ReferenceNumber(11111)
Sun Oct 11 03:51:26 IST 2020 [DepositService]: DEBUG - running only one time plan
Sun Oct 11 03:51:26 IST 2020 [DepositService]: INFO - adding amount 10500 SGP to plan : OneTimePlan
Sun Oct 11 03:51:26 IST 2020 [RunApplication$]: INFO - showing info for customer :

Customer ref no. -> ReferenceNumber(11111)
Wallet balance -> 200 SGP
Portfolios with Balance -> List(Retirement [600 SGP] , Emergency [20000 SGP] )
Deposit Plan -> OneTimePlan(List(Retirement [500 SGP] , Emergency [10000 SGP] ))
Total balance: 20600 SGP


Customer ref no. -> ReferenceNumber(22222)
Wallet balance -> 0 SGP
Portfolios with Balance -> List(Retirement [0 SGP] , Emergency [0 SGP] , HighRisk [400 SGP] )
Deposit Plan -> OneTimePlan(List(Retirement [500 SGP] , Emergency [10000 SGP] )), MonthlyPlan(List(Retirement [100 SGP] , Emergency [0 SGP] , HighRisk [100 SGP] ))
Total balance: 400 SGP

Sun Oct 11 03:51:26 IST 2020 [CustomerService]: INFO - depositing 10750 SGP to customer with ref no. : ReferenceNumber(22222)
Sun Oct 11 03:51:26 IST 2020 [DepositService]: DEBUG - Running one time plan first
Sun Oct 11 03:51:26 IST 2020 [DepositService]: INFO - adding amount 10500 SGP to plan : OneTimePlan
Sun Oct 11 03:51:26 IST 2020 [DepositService]: DEBUG - Running monthly plan
Sun Oct 11 03:51:26 IST 2020 [DepositService]: INFO - adding amount 200 SGP to plan : MonthlyPlan

Customer ref no. -> ReferenceNumber(11111)
Wallet balance -> 0 SGP
Portfolios with Balance -> List(Retirement [100 SGP] , Emergency [10000 SGP] )
Deposit Plan -> OneTimePlan(List(Retirement [500 SGP] , Emergency [10000 SGP] ))
Total balance: 10100 SGP


Customer ref no. -> ReferenceNumber(22222)
Wallet balance -> 50 SGP
Portfolios with Balance -> List(Retirement [600 SGP] , Emergency [10000 SGP] , HighRisk [500 SGP] )
Deposit Plan -> OneTimePlan(List(Retirement [500 SGP] , Emergency [10000 SGP] )), MonthlyPlan(List(Retirement [100 SGP] , Emergency [0 SGP] , HighRisk [100 SGP] ))
Total balance: 11100 SGP

Sun Oct 11 03:51:26 IST 2020 [CustomerService]: INFO - depositing 150 SGP to customer with ref no. : ReferenceNumber(22222)
Sun Oct 11 03:51:26 IST 2020 [DepositService]: DEBUG - Running one time plan first
Sun Oct 11 03:51:26 IST 2020 [DepositService]: DEBUG - Running monthly plan
Sun Oct 11 03:51:26 IST 2020 [RunApplication$]: INFO - showing info for customer :

Customer ref no. -> ReferenceNumber(11111)
Wallet balance -> 0 SGP
Portfolios with Balance -> List(Retirement [100 SGP] , Emergency [10000 SGP] )
Deposit Plan -> OneTimePlan(List(Retirement [500 SGP] , Emergency [10000 SGP] ))
Total balance: 10100 SGP


Customer ref no. -> ReferenceNumber(22222)
Wallet balance -> 150 SGP
Portfolios with Balance -> List(Retirement [0 SGP] , Emergency [0 SGP] , HighRisk [400 SGP] )
Deposit Plan -> OneTimePlan(List(Retirement [500 SGP] , Emergency [10000 SGP] )), MonthlyPlan(List(Retirement [100 SGP] , Emergency [0 SGP] , HighRisk [100 SGP] ))
Total balance: 400 SGP

[success] Total time: 8 s, completed 11 Oct, 2020 3:51:26 AM

*/