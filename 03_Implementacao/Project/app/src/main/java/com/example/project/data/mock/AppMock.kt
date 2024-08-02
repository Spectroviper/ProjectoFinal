package com.example.project.data.mock

import com.example.project.data.model.Achievement
import com.example.project.data.model.Game
import com.example.project.data.model.User
import com.example.project.data.model.UserAchievement
import com.example.project.data.model.UserGame
import com.example.project.R

object AppMock {


    var users = listOf<User>(
        User(
            1,
            "Kanto",
            R.drawable.bug,
            1,
            40,
            1200,
            "20/03/2010",
            "10/03/2009",
            "I like playing games"
        ),
        User(
            2,
            "Johto",
            R.drawable.water,
            10,
            40,
            1000,
            "24/03/2015",
            "01/01/2009",
            "Hello"
        ),
        User(
            3,
            "Hoenn",
            R.drawable.dark,
            2,
            40,
            1200,
            "20/03/2010",
            "10/03/2009",
            "I like playing games"
        ),
        User(
            4,
            "Sinnoh",
            R.drawable.dragon,
            1000,
            40,
            1200,
            "20/03/2010",
            "10/03/2009",
            "I like playing games"
        ),
        User(
            5,
            "Unova",
            R.drawable.electric,
            12,
            40,
            1200,
            "20/03/2010",
            "10/03/2009",
            "I like playing games"
        ),
        User(
            6,
            "Kalos",
            R.drawable.fairy,
            13,
            40,
            1200,
            "20/03/2010",
            "10/03/2009",
            "I like playing games"
        ),
        User(
            7,
            "Alola",
            R.drawable.fighting,
            14,
            40,
            1200,
            "20/03/2010",
            "10/03/2009",
            "I like playing games"
        ),
        User(
            8,
            "Galar",
            R.drawable.fairy,
            16,
            40,
            1200,
            "20/03/2010",
            "10/03/2009",
            "I like playing games"
        ),
    )

    var games = listOf<Game>(
        Game(
            1,
            "Pokemon Fire Red",
            R.drawable.fire,
            "GameBoy Advance",
            "Game Freak",
            "Nintendo",
            "RPG",
            "07/09/2004",
            "The story of Pokémon FireRed is the traditional Pokémon story. It's the remake of the games that started everything after all. You're a 10-year-old kid who, after receiving their starter Pokémon from Professor Oak, sets out on their Pokémon journey across the Kanto region."
        ),

        Game(
            2,
            "Pokemon Leaf Green",
            R.drawable.flying,
            "GameBoy Advance",
            "Game Freak",
            "Nintendo",
            "RPG",
            "07/09/2004",
            "The story of Pokémon LeafGreen is the traditional Pokémon story. It's the remake of the games that started everything after all. You're a 10-year-old kid who, after receiving their starter Pokémon from Professor Oak, sets out on their Pokémon journey across the Kanto region."
        ),

        Game(
            3,
            "Sly Cooper and the Thievius Raccoonus",
            R.drawable.ghost,
            "Playstation 2",
            "Sucker Punch Productions",
            "Sony Computer Entertainment",
            "Platform, Stealth",
            "23/09/2002",
            "The game follows master thief Sly Cooper and his gang, Bentley the Turtle and Murray the Hippo, as they seek out criminals known as the Fiendish Five to recover the pages of the \"Thievius Raccoonus\", the book of the accumulation of all of Sly's ancestors' thieving moves."
        ),
    )

    var achievements = listOf<Achievement>(
        Achievement(
            1,
            "Coin Collector",
            3,
            R.drawable.fire,
            10,
            1,
            "Collect a single coin",
        ),
        Achievement(
            2,
            "Foxy Lady",
            3,
            R.drawable.rock,
            20,
            1,
            "Collect a police file from Carmelita",
        ),
        Achievement(
            3,
            "Coin Recycler",
            3,
            R.drawable.psychic,
            15,
            60,
            "Collect 60 coins",
        ),
        Achievement(
            4,
            "Test",
            3,
            R.drawable.psychic,
            15,
            60,
            "Collect 60 coins",
        ),
        Achievement(
            5,
            "Test2",
            3,
            R.drawable.psychic,
            15,
            60,
            "Collect 60 coins",
        ),
        Achievement(
            6,
            "Test3",
            3,
            R.drawable.psychic,
            15,
            60,
            "Collect 60 coins",
        ),
    )

    var userGame = listOf<UserGame>(
        UserGame(
            1,
            3,
            true,
        ) ,
        UserGame(
            1,
            2,
            true,
        ),
    )

    var userAchievement = listOf<UserAchievement>(
        UserAchievement(
            1,
            1,
            1,
            true,
            "18/01/2010",
            R.drawable.dragon,
            "1",
        ),

        UserAchievement(
            2,
            1,
            3,
            false,
            "18/11/2009",
            R.drawable.grass,
            "15",
        ),
    )

}