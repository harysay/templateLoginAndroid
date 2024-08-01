package id.go.kebumenkab.realcount.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("data")
	val data: DataItemUser? = null,

	@field:SerializedName("type")
	val type: Int? = null,

	@field:SerializedName("status")
	val status: String? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("token")
	val token: String? = null
)

data class DataItemUser(

	@field:SerializedName("tps_id")
	val tpsId: String? = null,

	@field:SerializedName("nip")
	val nip: String? = null,

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("group_id")
	val groupId: String? = null,

	@field:SerializedName("id")
	val id: String? = null
)
