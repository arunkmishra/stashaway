package com.stashorg.service

import com.stashorg.model._
import com.stashorg.util.ApplicationLogger

class CustomerService(customerRepository: CustomerRepository) {
  type DepositError = String
  val logger = new ApplicationLogger(this.getClass.getSimpleName)
  val depositService: DepositService = new DepositService()

  def findCustomerAndDeposit(
    referenceNumber: ReferenceNumber,
    depositAmount: Money
  ): Either[DepositError, CustomerRepository] =
    customerRepository.findCustomerByRefNo(referenceNumber) match {
      case Some(customer) =>
        logger.info(s"depositing $depositAmount to customer with ref no. : $referenceNumber")
        depositService
          .depositInCustomerAccount(customer, depositAmount)
          .fold(
            err => Left(err),
            c => Right(customerRepository.updateCustomer(referenceNumber, c))
          )
      case None =>
        logger.warn(s"No customer found with ref no. : $referenceNumber")
        Left(s"No customer found with ref no. : $referenceNumber")
    }

}
