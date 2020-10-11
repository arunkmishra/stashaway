package com.stashorg.service

import com.stashorg.model._
import com.stashorg.util.ConsoleLogger

class CustomerService(customerRepository: => CustomerRepository) {

  private val logger = new ConsoleLogger(this.getClass.getCanonicalName)
  private val depositService: DepositService = new DepositService()

  def findCustomerAndDeposit(
    referenceNumber: ReferenceNumber,
    depositAmount: Money
  ): Either[DepositFailure, CustomerRepository] =
    customerRepository.findCustomerByRefNo(referenceNumber) match {
      case Some(customer) =>
        logger.info(
          s"depositing $depositAmount to customer with ref no. : $referenceNumber"
        )
        depositService
          .depositInCustomerAccount(customer, depositAmount)
          .fold(
            err => Left(err),
            c => Right(customerRepository.updateCustomer(referenceNumber, c))
          )
      case None =>
        logger.warn(s"No customer found with ref no. : $referenceNumber")
        Left(
          DepositFailure(s"No customer found with ref no. : $referenceNumber")
        )
    }

}
