package com.stashorg.service

import com.stashorg.model._
import com.stashorg.service.repository.Repository
import com.stashorg.util.ConsoleLogger

class CustomerService(customerRepository: Repository[ReferenceNumber, Customer]) {

  private val logger = new ConsoleLogger(this.getClass.getCanonicalName)
  private val depositService: DepositService = new DepositService()

  def findCustomerAndDeposit(referenceNumber: ReferenceNumber,
                             depositAmount: Money): DepositResult =
    customerRepository.findByKey(referenceNumber) match {
      case Some(customer) =>
        logger.info(
          s"depositing $depositAmount to customer with ref no. : $referenceNumber"
        )
        depositService
          .depositInCustomerAccount(customer, depositAmount)
          .fold(err => DepositFailure(err.err), c => {
            customerRepository.update(referenceNumber, c)
            DepositSuccess()
          })
      case None =>
        logger.warn(s"No customer found with ref no. : $referenceNumber")
        DepositFailure(s"No customer found with ref no. : $referenceNumber")
    }

}
