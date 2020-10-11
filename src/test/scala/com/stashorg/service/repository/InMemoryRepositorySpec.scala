package com.stashorg.service.repository

import org.scalatest.flatspec.AnyFlatSpec

class InMemoryRepositorySpec extends AnyFlatSpec {

  val inMemoryRepositoryNumberToString: InMemoryRepository[Int, String] =
    new InMemoryRepository[Int, String]

  "def isEmpty" should "return true when there are zero records in repository" in {
    assert(inMemoryRepositoryNumberToString.isEmpty)
  }

  it should "return false when there are records in repository" in {
    inMemoryRepositoryNumberToString.add(1, "One")
    assert(!inMemoryRepositoryNumberToString.isEmpty)
  }

  "def add" should "add record in repository" in {
    inMemoryRepositoryNumberToString.add(2, "two")
    assert(inMemoryRepositoryNumberToString.findByKey(2).isDefined)
  }

  "def update" should "update the entry with new given value" in {
    inMemoryRepositoryNumberToString.update(2, "TWO")
    assert(inMemoryRepositoryNumberToString.findByKey(2).contains("TWO"))
  }

  "def findByKey" should "return some key 2" in {
    assert(inMemoryRepositoryNumberToString.findByKey(2).isDefined)
    assert(inMemoryRepositoryNumberToString.findByKey(2).contains("TWO"))
  }

  it should "return None if for key 3" in {
    assert(inMemoryRepositoryNumberToString.findByKey(3).isEmpty)
  }
}
