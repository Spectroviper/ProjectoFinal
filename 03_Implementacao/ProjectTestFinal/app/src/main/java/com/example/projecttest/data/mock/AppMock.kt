package com.example.projecttest.data.mock

import com.example.projecttest.data.model.Achievement
import com.example.projecttest.data.model.Challenge
import com.example.projecttest.data.model.ChallengeAchievement
import com.example.projecttest.data.model.ChallengeInvite
import com.example.projecttest.data.model.Friend
import com.example.projecttest.data.model.Game
import com.example.projecttest.data.model.User
import com.example.projecttest.data.model.UserAchievement
import com.example.projecttest.data.model.UserChallenge
import com.example.projecttest.data.model.UserChallengeAchievement
import com.example.projecttest.data.model.UserGame

object AppMock {

    var users = mutableListOf<User>()
    var games = mutableListOf<Game>()
    var achievements = mutableListOf<Achievement>()
    var userGames = mutableListOf<UserGame>()
    var userAchievements = mutableListOf<UserAchievement>()
    var userChallengeAchievements = mutableListOf<UserChallengeAchievement>()
    var userChallenges = mutableListOf<UserChallenge>()
    var challenges = mutableListOf<Challenge>()
    var challengeInvites = mutableListOf<ChallengeInvite>()
    var challengeAchievements = mutableListOf<ChallengeAchievement>()
    var friends = mutableListOf<Friend>()



    /*init {

        // Step 1: Create core Users without relational lists
        users.addAll(
            listOf(
                User(
                    userId = "1",
                    userName = "Kanto",
                    email = "kanto@gmail.com",
                    imageUrl = "https://proximonivel.pt/wp-content/uploads/2020/06/slycooper-01-pn-r.jpg",
                    averageCompletion = 40,
                    totalPoints = 1200,
                    lastLogin = "20/03/2010",
                    memberSince = "10/03/2009",
                    biography = "I like playing games",
                    mutableListOf(),
                    mutableListOf(),
                    mutableListOf(),
                    mutableListOf(),
                    mutableListOf(),
                    mutableListOf(),
                    mutableListOf(),
                ),
                User(
                    "2",
                    "Johto",
                    "johto@gmail.com",
                    "...",
                    40,
                    1000,
                    "24/03/2015",
                    "01/01/2009",
                    "Hello",
                    mutableListOf(),
                    mutableListOf(),
                    mutableListOf(),
                    mutableListOf(),
                    mutableListOf(),
                    mutableListOf(),
                    mutableListOf(),
                ),
                User(
                    "3",
                    "Hoenn",
                    "hoenn@gmail.com",
                    "...",
                    40,
                    1200,
                    "20/03/2010",
                    "10/03/2009",
                    "I like playing games",
                    mutableListOf(),
                    mutableListOf(),
                    mutableListOf(),
                    mutableListOf(),
                    mutableListOf(),
                    mutableListOf(),
                    mutableListOf(),
                ),
                User(
                    "4",
                    "Sinnoh",
                    "sinnoh@gmail.com",
                    "...",
                    40,
                    1200,
                    "20/03/2010",
                    "10/03/2009",
                    "I like playing games,",
                    mutableListOf(),
                    mutableListOf(),
                    mutableListOf(),
                    mutableListOf(),
                    mutableListOf(),
                    mutableListOf(),
                    mutableListOf(),
                ),
                User(
                    "5",
                    "Unova",
                    "unova@gmail.com",
                    "...",
                    40,
                    1200,
                    "20/03/2010",
                    "10/03/2009",
                    "I like playing games",
                    mutableListOf(),
                    mutableListOf(),
                    mutableListOf(),
                    mutableListOf(),
                    mutableListOf(),
                    mutableListOf(),
                    mutableListOf(),
                ),
                User(
                    "6",
                    "Kalos",
                    "kalos@gmail.com",
                    "...",
                    40,
                    1200,
                    "20/03/2010",
                    "10/03/2009",
                    "I like playing games",
                    mutableListOf(),
                    mutableListOf(),
                    mutableListOf(),
                    mutableListOf(),
                    mutableListOf(),
                    mutableListOf(),
                    mutableListOf(),
                ),
                User(
                    "7",
                    "Alola",
                    "alola@gmail.com",
                    "...",
                    40,
                    1200,
                    "20/03/2010",
                    "10/03/2009",
                    "I like playing games",
                    mutableListOf(),
                    mutableListOf(),
                    mutableListOf(),
                    mutableListOf(),
                    mutableListOf(),
                    mutableListOf(),
                    mutableListOf(),
                ),
                User(
                    "8",
                    "Galar",
                    "galar@gmail.com",
                    "...",
                    40,
                    1200,
                    "20/03/2010",
                    "10/03/2009",
                    "I like playing games",
                    mutableListOf(),
                    mutableListOf(),
                    mutableListOf(),
                    mutableListOf(),
                    mutableListOf(),
                    mutableListOf(),
                    mutableListOf(),
                ),
                User(
                    "9",
                    "hadoukenbeast",
                    "ruilanca@live.com.pt",
                    "...",
                    40,
                    1200,
                    "20/03/2010",
                    "10/03/2009",
                    "I like playing games",
                    mutableListOf(),
                    mutableListOf(),
                    mutableListOf(),
                    mutableListOf(),
                    mutableListOf(),
                    mutableListOf(),
                    mutableListOf(),
                ),
            )
        )

        // Step 2: Add Games (referencing users later)
        games.addAll(
            listOf(
                Game(
                    "1",
                    "Pokemon Fire Red",
                    "...",
                    "GameBoy Advance",
                    "Game Freak",
                    "Nintendo",
                    "RPG",
                    "07/09/2004",
                    "Pokémon story...",
                    "1",
                    mutableListOf(),
                    mutableListOf()
                ),
                Game(
                    "2",
                    "Pokemon Leaf Green",
                    "...",
                    "GameBoy Advance",
                    "Game Freak",
                    "Nintendo",
                    "RPG",
                    "07/09/2004",
                    "LeafGreen story...",
                    "3",
                    mutableListOf(),
                    mutableListOf()
                ),
                Game(
                    "3",
                    "Sly Cooper and the Thievius Raccoonus",
                    "...",
                    "Playstation 2",
                    "Sucker Punch",
                    "Sony",
                    "Platform, Stealth",
                    "23/09/2002",
                    "Sly Cooper story...",
                    "9",
                    mutableListOf(),
                    mutableListOf()
                )
            )
        )

        // Step 3: Add Achievements (now games exist)
        achievements.addAll(
            listOf(
                Achievement(
                    "1",
                    "Coin Collector",
                    "3",
                    "...",
                    1,
                    "Collect a single coin",
                    mutableListOf(),
                    mutableListOf()
                ),
                Achievement(
                    "2",
                    "Foxy Lady",
                    "3",
                    "...",
                    1,
                    "Collect a police file from Carmelita",
                    mutableListOf(),
                    mutableListOf()
                ),
                Achievement(
                    "3",
                    "Coin Recycler",
                    "3",
                    "...",
                    60,
                    "Collect 60 coins",
                    mutableListOf(),
                    mutableListOf()
                )
            )
        )

        // Step 4: Add UserGame (needs users + games)
        userGames.addAll(
            listOf(
                //UserGame("1", "2", "1", null),
                //UserGame("2", "9", "3", 0)
            )
        )

        // Step 5: Add UserAchievements (needs users + achievements)
        userAchievements.addAll(
            listOf(
                UserAchievement("1", "1", "1", true, "18/01/2010", "1"),
                UserAchievement("2", "1", "3", false, "18/11/2009", "15")
            )
        )

        // Step 6: Add Challenges
        challenges.addAll(
            listOf(
                Challenge(
                    "1", "Pokemon", "...", "Timer", "07/09/2004", "16/09/2004", "2",
                    mutableListOf(), mutableListOf(), mutableListOf()
                ),
                Challenge(
                    "2", "Sly", "...", "FirstToFinish", "", "", "9",
                    mutableListOf(), mutableListOf(), mutableListOf()
                )
            )
        )

        // Step 7: Add UserChallenges (users + challenges)
        userChallenges.addAll(
            listOf(
                UserChallenge("1", "9", "2", true, 0f),
                UserChallenge("2", "8", "2", false, 10f),
                UserChallenge("3", "1", "1", true, 0f),
                UserChallenge("4", "3", "1", false, 0f),
                UserChallenge("5", "4", "2", false, 0f),
                UserChallenge("6", "7", "2", false, 0f)
            )
        )

        userChallenges.forEach { userChallenge ->
            userChallenge.challenge =
                challenges.find { it.challengeId == userChallenge.challengeId }

            challengeInvites.addAll(
                listOf(
                    ChallengeInvite("1", "Accepted", "", "2", false, "9", "2"),
                    ChallengeInvite("2", "Accepted", "", "8", true, "9", "2"),
                    ChallengeInvite("3", "Accepted", "", "1", false, "1", "1"),
                    ChallengeInvite("4", "Accepted", "", "3", false, "1", "1"),
                    ChallengeInvite("5", "Accepted", "", "4", true, "9", "2"),
                    ChallengeInvite("6", "Accepted", "", "7", false, "9", "2"),
                    ChallengeInvite("7", "Pending", "", "5", true, "9", "2"),
                    ChallengeInvite("8", "Pending", "", "1", false, "9", "2"),
                    ChallengeInvite("9", "Rejected", "", "3", false, "9", "2"),
                )
            )

            challengeAchievements.addAll(
                listOf(
                    ChallengeAchievement("1", "2", "3"),
                    ChallengeAchievement("2", "2", "2"),
                )
            )

            // Step 8: Add Friends
            friends.addAll(
                listOf(
                    Friend("1", "9", "1", "Rejected", "29/6/2024"),
                    Friend("2", "2", "9", "Pending", "29/6/2024"),
                    Friend("3", "4", "9", "Friends", "29/6/2024"),
                    Friend("4", "9", "3", "Pending", "29/6/2024")
                )
            )

            // Step 9: Link User lists now that everything is populated
            users.forEach { user ->
                user.userGames = userGames.filter { it.userId == user.userId }
                user.userAchievements = userAchievements.filter { it.userId == user.userId }
                user.userChallengeAchievements =
                    userChallengeAchievements.filter { it.userId == user.userId }
                user.userChallenges = userChallenges.filter { it.userId == user.userId }
                user.challengeInvites = challengeInvites.filter { it.userId == user.userId }
                user.friendSenders = friends.filter { it.userSenderId == user.userId }
                user.friendReceivers = friends.filter { it.userReceiverId == user.userId }
            }

            games.forEach { game ->
                game.userGames = userGames.filter { it.gameId == game.gameId }
                game.achievements = achievements.filter { it.gameId == game.gameId }
            }

            achievements.forEach { achievement ->
                achievement.userAchievements =
                    userAchievements.filter { it.achievementId == achievement.achievementId }
                achievement.challengeAchievements =
                    challengeAchievements.filter { it.achievementId == achievement.achievementId }
            }

            challenges.forEach { challenge ->
                challenge.userChallenges =
                    userChallenges.filter { it.challengeId == challenge.challengeId }
                challenge.challengeAchievement =
                    challengeAchievements.filter { it.challengeId == challenge.challengeId }
                challenge.challengeInvites =
                    challengeInvites.filter { it.challengeId == challenge.challengeId }
            }
        }
    }*/
}
