package com.toritark.app.data.ads.model.placement

sealed class AdPlacement(
    val placementName: String? = null,
) {

    data object Default : AdPlacement()

    sealed class Banner(
        placementName: String? = null,
    ) : AdPlacement(placementName) {

        sealed class Story(
            placementName: String? = null,
        ) : Banner(placementName) {

            data object Main : Story("banner_story_main")
            data object Text : Story("banner_story_text")
        }

        sealed class Quiz(
            placementName: String? = null,
        ) : Banner(placementName) {

            data object Main : Quiz("banner_quiz_main")
        }

        sealed class LearningWords(
            placementName: String? = null,
        ) : Banner(placementName) {

            data object Main : LearningWords("banner_learning_words")
        }
    }

    sealed class Interstitial(
        placementName: String? = null,
    ) : AdPlacement(placementName) {

        sealed class LearningWords(
            placementName: String? = null,
        ) : Interstitial(placementName) {

            data object Main : LearningWords("interstitial_learning_words")
        }

        sealed class Quiz(
            placementName: String? = null,
        ) : Interstitial(placementName) {

            data object Main : Quiz("interstitial_quiz")
        }
    }

    sealed class Rewarded(
        placementName: String? = null,
    ) : AdPlacement(placementName) {

        data object Generation : Rewarded("rewarded_generation")
        data object RetellingCheck : Rewarded("rewarded_retelling_check")
    }
}