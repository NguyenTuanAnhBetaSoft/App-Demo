package com.betasoft.appdemo.data.api.responseremote

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class DataResponseRemote(

	@field:SerializedName("nextCursor")
	val nextCursor: String? = null,

	@field:SerializedName("items")
	val items: List<ItemsItem?>? = null
)

/*data class Stats(

	@field:SerializedName("share_count")
	val shareCount: Int? = null,

	@field:SerializedName("comment_count")
	val commentCount: Int? = null,

	@field:SerializedName("like_count")
	val likeCount: Int? = null,

	@field:SerializedName("bookmark_count")
	val bookmarkCount: Int? = null,

	@field:SerializedName("vote_count")
	val voteCount: Int? = null,

	@field:SerializedName("following_count")
	val followingCount: Int? = null,

	@field:SerializedName("post_count")
	val postCount: Int? = null,

	@field:SerializedName("follower_count")
	val followerCount: Int? = null
)*/

/*data class CreatedAt(

	@field:SerializedName("_nanoseconds")
	val nanoseconds: Int? = null,

	@field:SerializedName("_seconds")
	val seconds: Int? = null
)*/

@Parcelize
data class UserProfile(

//	@field:SerializedName("free_credit_balance")
//	val freeCreditBalance: Int? = null,
//
//	@field:SerializedName("enableFollowToShowPrompt")
//	val enableFollowToShowPrompt: Boolean? = null,

//	@field:SerializedName("stats")
//	val stats: Stats? = null,
//
//	@field:SerializedName("displayName")
//	val displayName: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

//	@field:SerializedName("created_at")
//	val createdAt: Int? = null,

//	@field:SerializedName("bio")
//	val bio: String? = null,

	@field:SerializedName("avatar")
	val avatar: String? = null,

//	@field:SerializedName("id")
//	val id: String? = null,
//
//	@field:SerializedName("trial_credit_balance")
//	val trialCreditBalance: Int? = null,
//
//	@field:SerializedName("username")
//	val username: String? = null,
//
//	@field:SerializedName("subscription_monthly_credit")
//	val subscriptionMonthlyCredit: Int? = null,
//
//	@field:SerializedName("subscription_start_at")
//	val subscriptionStartAt: Int? = null,
//
//	@field:SerializedName("subscription_active")
//	val subscriptionActive: Boolean? = null,
//
//	@field:SerializedName("subscription_end_at")
//	val subscriptionEndAt: Int? = null,
//
//	@field:SerializedName("subscription_type")
//	val subscriptionType: String? = null,
//
//	@field:SerializedName("trail_credit_balance")
//	val trailCreditBalance: Int? = null,
//
//	@field:SerializedName("credit_balance")
//	val creditBalance: Any? = null,
//
//	@field:SerializedName("dalle2_credit_balance")
//	val dalle2CreditBalance: Int? = null,
//
//	@field:SerializedName("subscription_montly_credit")
//	val subscriptionMontlyCredit: Int? = null
): Parcelable

@Parcelize
data class ItemsItem(

//	@field:SerializedName("configs")
//	val configs: Configs? = null,

	@field:SerializedName("ai_model")
	val aiModel: String? = null,

	@field:SerializedName("image_url")
	val image_url: String? = null,

//	@field:SerializedName("is_prompt_private")
//	val isPromptPrivate: Boolean? = null,
//
//	@field:SerializedName("link")
//	val link: String? = null,
//
//	@field:SerializedName("description")
//	val description: String? = null,
//
//	@field:SerializedName("created_at")
//	val createdAt: CreatedAt? = null,

//	@field:SerializedName("image_width")
//	val imageWidth: Int? = null,
//
//	@field:SerializedName("ava_score")
//	val avaScore: Any? = null,
//
//	@field:SerializedName("title")
//	val title: String? = null,

	@field:SerializedName("userProfile")
	val userProfile: UserProfile? = null,

//	@field:SerializedName("sampler")
//	val sampler: Any? = null,
//
//	@field:SerializedName("is_nsfw")
//	val isNsfw: Boolean? = null,
//
//	@field:SerializedName("image_height")
//	val imageHeight: Int? = null,
//
//	@field:SerializedName("image_seed")
//	val imageSeed: Int? = null,
//
//	@field:SerializedName("user_id")
//	val userId: String? = null,
//
//	@field:SerializedName("stats")
//	val stats: Stats? = null,
//
//	@field:SerializedName("sd_version")
//	val sdVersion: Any? = null,
//
//	@field:SerializedName("variation_source")
//	val variationSource: Any? = null,

	@field:SerializedName("id")
	val id: String? = null,

//	@field:SerializedName("source_id")
//	val sourceId: String? = null,

	@field:SerializedName("prompt")
	val prompt: String? = null
): Parcelable

/*data class VariationSource(

	@field:SerializedName("image_url")
	val imageUrl: String? = null
)*/

/*
data class Configs(

	@field:SerializedName("strength")
	val strength: Any? = null,

	@field:SerializedName("seed")
	val seed: String? = null,

	@field:SerializedName("negative_prompt")
	val negativePrompt: String? = null,

	@field:SerializedName("steps")
	val steps: String? = null,

	@field:SerializedName("cfg_scale")
	val cfgScale: String? = null,

	@field:SerializedName("sampler")
	val sampler: String? = null,

	@field:SerializedName("width")
	val width: Int? = null,

	@field:SerializedName("height")
	val height: Int? = null
)
*/
