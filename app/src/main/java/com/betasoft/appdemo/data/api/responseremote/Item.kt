package com.betasoft.appdemo.data.api.responseremote

data class Item(
    val ai_model: String,
    val ava_score: Double,
    val configs: Configs,
    val created_at: CreatedAt,
    val description: String,
    val id: String,
    val image_height: Int,
    val image_seed: Int,
    val image_url: String,
    val image_width: Int,
    val is_nsfw: Boolean,
    val is_prompt_private: Boolean,
    val link: String,
    val prompt: String,
    val sampler: Any,
    val sd_version: Any,
    val source_id: String,
    val stats: Stats,
    val title: String,
    val userProfile: UserProfile,
    val user_id: String,
    val variation_source: VariationSource
)