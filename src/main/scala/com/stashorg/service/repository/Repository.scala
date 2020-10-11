package com.stashorg.service.repository

trait Repository[K, V] {

  def isEmpty: Boolean

  def getAll: Map[K, V]

  def add(key: K, value: V): RepositoryResult

  def update(key: K, updatedValue: V): RepositoryResult

  def findByKey(keyToSearch: K): Option[V]

  def showAllCustomersInRepository(): Unit

}
