package com.stashorg.service.repository

trait RepositoryResult

case class RepositoryFailure(err: String) extends RepositoryResult
case object RepositorySuccess extends RepositoryResult
