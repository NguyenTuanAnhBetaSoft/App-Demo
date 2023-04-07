package com.betasoft.appdemo.data.api.responseremote

data class UserProfile(
    val avatar: String,
    val bio: String,
    val created_at: Int,
    val credit_balance: Double,
    val dalle2_credit_balance: Int,
    val displayName: String,
    val enableFollowToShowPrompt: Boolean,
    val free_credit_balance: Int,
    val id: String,
    val name: String,
    val stats: StatsX,
    val subscription_active: Boolean,
    val subscription_end_at: Int,
    val subscription_monthly_credit: Int,
    val subscription_montly_credit: Int,
    val subscription_start_at: Int,
    val subscription_type: String,
    val trail_credit_balance: Int,
    val trial_credit_balance: Int,
    val username: String
)