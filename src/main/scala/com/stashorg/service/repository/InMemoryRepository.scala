package com.stashorg.service.repository

class InMemoryRepository[K, V]() extends Repository[K, V] {

  private var values: Map[K, V] =
    Map.empty[K, V]

  def getAll: Map[K, V] = values

  def isEmpty: Boolean = values.isEmpty

  def add(key: K, value: V): RepositoryResult = {
    values = values + (key -> value)
    RepositorySuccess
  }

  def update(key: K, updatedValue: V): RepositoryResult = {
    add(key, updatedValue)
    RepositorySuccess
  }


  def findByKey(key: K): Option[V] =
    values.get(key)

  def showAllRecordsInRepository(): Unit = values.values.foreach(println)

}
