package com.betasoft.appdemo.data.api.responseremote

data class Configs(
    val cfg_scale: Int,
    val height: Int,
    val negative_prompt: String,
    val sampler: String,
    val seed: String,
    val steps: Int,
    val strength: Double,
    val width: Int
)