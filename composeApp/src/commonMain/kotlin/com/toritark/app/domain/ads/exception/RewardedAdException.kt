package com.toritark.app.domain.ads.exception

open class RewardedAdException(message: String?) : Exception(message)
class NoRewardedAdException : RewardedAdException("No ad available")
class FailedToShowRewardedAdException : RewardedAdException("Failed to show ad")
class RewardedAdNotFinishedException : RewardedAdException("Rewarded ad not finished")
class RewardedAdNoBonusAddedException : RewardedAdException("No bonus added on the server")
