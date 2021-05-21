package ro.pdm.muno_pdm.utils.http

/**
 * Basic object to intercept an error or the actual response for the response of an http request
 */
class MunoResponse<T>(var value: T? = null,  var errorMessage: String? = null)