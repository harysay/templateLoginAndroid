package id.go.kebumenkab.realcount.model

import com.google.gson.annotations.SerializedName

data class ServiceResponse(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)
