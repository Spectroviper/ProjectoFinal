import { Achievements } from "../Entities/Achievements";
import { Games } from "../Entities/Games";
import { Users } from "../Entities/Users";
import { Friends } from "../Entities/Friends";
import { UserGames } from "../Entities/UserGames";
import { UserAchievements } from "../Entities/UserAchievements";
import { Challenges } from "../Entities/Challenges";
import { ChallengeInvites } from "../Entities/ChallengeInvites";
import { UserChallenges } from "../Entities/UserChallenges";
import { ChallengeAchievements } from "../Entities/ChallengeAchievements";
import { UserChallengeAchievements } from "../Entities/UserChallengeAchievements";
import dotenv from 'dotenv';
import { appDataSource } from "../DataSource";
import path from "path";

dotenv.config({ path: path.resolve(__dirname, '../../.env') });


async function seed() {
  console.log('Seed script running...');

  console.log('DB user:', process.env.MYSQL_USER);
  console.log('DB password:', process.env.MYSQL_PASSWORD)
  console.log('Current working dir:', process.cwd());
  console.log('__dirname:', __dirname);

  await appDataSource.initialize();

  //USERS
const usersSeedData: Array<Partial<Users>> = [
  {
    UserName: "playerOne",
    Email: "playerone@example.com",
    Biography: "Casual gamer and achievement hunter.",
    MemberSince: new Date("2023-01-15T10:00:00Z"),
    LastLogin: new Date("2025-07-18T08:00:00Z"),
    TotalPoints: 1500,
    AverageCompletion: 75,
    Image: "/Images/default.png"
  },
  {
    UserName: "proGamer",
    Email: "pro@example.com",
    Biography: "Competitive player focused on speedruns.",
    MemberSince: new Date("2022-06-01T12:30:00Z"),
    LastLogin: new Date("2025-07-17T21:00:00Z"),
    TotalPoints: 3200,
    AverageCompletion: 90,
    Image: "/Images/default.png"
  },
  {
    UserName: "collector99",
    Email: "collector99@example.com",
    Biography: "Collecting all the things.",
    MemberSince: new Date("2024-03-20T09:45:00Z"),
    LastLogin: new Date("2025-07-15T17:30:00Z"),
    TotalPoints: 2400,
    AverageCompletion: 60,
    Image: "/Images/default.png"
  },
  {
    UserName: "noobMaster",
    Email: "noob@example.com",
    Biography: "Here to have fun, not to win.",
    MemberSince: new Date("2024-08-12T14:15:00Z"),
    LastLogin: new Date("2025-07-18T09:00:00Z"),
    TotalPoints: 800,
    AverageCompletion: 35,
    Image: "/Images/default.png"
  },
  {
    UserName: "mysteryPlayer",
    Email: "mystery@example.com",
    Biography: "",
    MemberSince: new Date("2023-11-05T18:00:00Z"),
    LastLogin: new Date("2025-07-17T22:00:00Z"),
    TotalPoints: 1800,
    AverageCompletion: 50,
    Image: "/Images/default.png"
  }
];

const users = Users.create(usersSeedData);
await Users.save(users);


//GAMES
const gamesSeedData: Array<Partial<Games>> = [
  {
    GameName: "Legend of Code",
    About: "An epic coding adventure game where players solve puzzles to progress.",
    Console: "PC",
    Developer: "CodeMasters Studio",
    Publisher: "GameWorks",
    Genre: "Adventure",
    ReleaseDate: new Date("2023-05-15"),
    Image: "/Images/default.png",
    CreatedBy: "1"
  },
  {
    GameName: "Speed Runner X",
    About: "Fast-paced platformer focusing on speed and precision.",
    Console: "Xbox Series X",
    Developer: "FastGames Inc.",
    Publisher: "SpeedyPublish",
    Genre: "Platformer",
    ReleaseDate: new Date("2024-02-20"),
    Image: "/Images/default.png",
    CreatedBy: "2"
  },
  {
    GameName: "Mystery Quest",
    About: "A thrilling mystery game with multiple endings.",
    Console: "PlayStation 5",
    Developer: "MysteryDev",
    Publisher: "Quest Publishers",
    Genre: "Puzzle",
    ReleaseDate: new Date("2022-11-01"),
    Image: "/Images/default.png",
    CreatedBy: "2"
  },
  {
    GameName: "Battle Arena",
    About: "",
    Console: "PC",
    Developer: "Arena Studios",
    Publisher: "FightGames",
    Genre: "Fighting",
    ReleaseDate: new Date("2023-07-30"),
    Image: "/Images/default.png",
    CreatedBy: "3"
  },
  {
    GameName: "Space Explorers",
    About: "Explore galaxies and discover new planets.",
    Console: "Nintendo Switch",
    Developer: "GalaxySoft",
    Publisher: "Space Games",
    Genre: "Simulation",
    ReleaseDate: new Date("2023-12-10"),
    Image: "/Images/default.png",
    CreatedBy: "1"
  }
];

const games = Games.create(gamesSeedData);
await Games.save(games);

//ACHIEVEMENTS

const game1 = await Games.findOneBy({ GameId: 1 });
const game2 = await Games.findOneBy({ GameId: 2 });

const achievementsSeedData: Array<Partial<Achievements>> = [
  {
    AchievementName: "First Steps",
    About: "Complete the tutorial level.",
    TotalCollectable: 1,
    Image: "/Images/default.png",
    CreatedBy: "1",
    Game: game1!
  },
  {
    AchievementName: "Speed Demon",
    About: "Finish a level in under 2 minutes.",
    TotalCollectable: 1,
    Image: "/Images/default.png",
    CreatedBy: "2",
    Game: game2!
  },
  {
    AchievementName: "Treasure Hunter",
    About: "Collect all hidden items in a single level.",
    TotalCollectable: 10,
    Image: "/Images/default.png",
    CreatedBy: "1",
    Game: game1!
  },
  {
    AchievementName: "Champion",
    About: "Win 100 matches.",
    TotalCollectable: 100,
    Image: "/Images/default.png",
    CreatedBy: "1",
    Game: game2!
  },
  {
    AchievementName: "Explorer",
    About: "Visit all locations.",
    TotalCollectable: 5,
    Image: "/Images/default.png",
    CreatedBy: "1",
    Game: game1!
  }
];

const achievements = Achievements.create(achievementsSeedData);
await Achievements.save(achievements);

// FRIENDS

const user1 = await Users.findOneBy({ UserId: 1 });
const user2 = await Users.findOneBy({ UserId: 2 });
const user3 = await Users.findOneBy({ UserId: 3 });
const user4 = await Users.findOneBy({ UserId: 4 });
const user5 = await Users.findOneBy({ UserId: 5 });

const friendsSeedData: Array<Partial<Friends>> = [
  {
    State: "Friends",
    Date: new Date("2025-07-01T12:00:00Z"),
    UserSender: user1!,
    UserReceiver: user2!
  },
  {
    State: "Pending",
    Date: new Date("2025-07-10T14:30:00Z"),
    UserSender: user3!,
    UserReceiver: user1!
  },
  {
    State: "Rejected",
    Date: new Date("2025-07-12T16:45:00Z"),
    UserSender: user4!,
    UserReceiver: user5!
  },
  {
    State: "Friends",
    Date: new Date("2025-07-14T11:15:00Z"),
    UserSender: user2!,
    UserReceiver: user3!
  },
  {
    State: "Pending",
    Date: new Date("2025-07-16T09:00:00Z"),
    UserSender: user5!,
    UserReceiver: user1!
  }
];

const friends = Friends.create(friendsSeedData);
await Friends.save(friends);

// USERGAMES

const game3 = await Games.findOneBy({ GameId: 3 });

const userGamesSeedData: Array<Partial<UserGames>> = [
  {
    AverageCompletion: 75.5,
    User: user1!,
    Game: game1!
  },
  {
    AverageCompletion: 60,
    User: user2!,
    Game: game2!
  },
  {
    AverageCompletion: 90,
    User: user2!,
    Game: game1!
  },
  {
    AverageCompletion: 45,
    User: user3!,
    Game: game3!
  },
  {
    AverageCompletion: 100,
    User: user1!,
    Game: game2!
  }
];

const userGames = UserGames.create(userGamesSeedData);
await UserGames.save(userGames);

// USERACHIEVEMENTS

const achievement1 = await Achievements.findOneBy({ AchievementId: 1 });
const achievement2 = await Achievements.findOneBy({ AchievementId: 2 });
const achievement3 = await Achievements.findOneBy({ AchievementId: 3 });
const achievement4 = await Achievements.findOneBy({ AchievementId: 4 });

const userAchievementsSeedData: Array<Partial<UserAchievements>> = [
  {
    Clear: true,
    UnlockDate: new Date("2025-07-01T10:00:00Z"),
    TotalCollected: 1,
    Achievement: achievement1!,
    User: user1!
  },
  {
    Clear: false,
    UnlockDate: null,
    TotalCollected: 0,
    Achievement: achievement2!,
    User: user2!
  },
  {
    Clear: true,
    UnlockDate: new Date("2025-07-15T14:30:00Z"),
    TotalCollected: 5,
    Achievement: achievement3!,
    User: user1!
  },
  {
    Clear: false,
    UnlockDate: null,
    TotalCollected: 0,
    Achievement: achievement4!,
    User: user2!
  },
  {
    Clear: true,
    UnlockDate: new Date("2025-07-17T09:00:00Z"),
    TotalCollected: 3,
    Achievement: achievement2!,
    User: user1!
  }
];

const userAchievements = UserAchievements.create(userAchievementsSeedData);
await UserAchievements.save(userAchievements);


// CHALLENGES

const challengesSeedData: Array<Partial<Challenges>> = [
  {
    ChallengeName: "First to Finish",
    Type: "endfirst",
    IsStarted: true,
    StartDate: new Date("2025-06-01T08:00:00Z"),
    EndDate: new Date("2025-07-01T08:00:00Z"),
    Image: "/Images/default.png",
    CreatedBy: "1"
  },
  {
    ChallengeName: "Speedrun Master",
    Type: "speedrun",
    IsStarted: false,
    StartDate: undefined,
    EndDate: undefined,
    Image: "/Images/default.png",
    CreatedBy: "2"
  },
  {
    ChallengeName: "Collector's Quest",
    Type: "collection",
    IsStarted: true,
    StartDate: new Date("2025-05-15T10:00:00Z"),
    EndDate: new Date("2025-08-15T10:00:00Z"),
    Image: "/Images/default.png",
    CreatedBy: "1"
  },
  {
    ChallengeName: "Survivor Challenge",
    Type: "survival",
    IsStarted: false,
    StartDate: undefined,
    EndDate: undefined,
    Image: "/Images/default.png",
    CreatedBy: "2"
  },
  {
    ChallengeName: "Ultimate Champion",
    Type: "endfirst",
    IsStarted: true,
    StartDate: new Date("2025-07-01T12:00:00Z"),
    EndDate: new Date("2025-07-31T12:00:00Z"),
    Image: "/Images/default.png",
    CreatedBy: "1"
  }
];

const challenges = Challenges.create(challengesSeedData);
await Challenges.save(challenges);

// CHALLENGE INVITES

const challenge1 = await Challenges.findOneBy({ ChallengeId: 1 });
const challenge2 = await Challenges.findOneBy({ ChallengeId: 2 });
const challenge3 = await Challenges.findOneBy({ ChallengeId: 3 });

const challengeInvitesSeedData: Array<Partial<ChallengeInvites>> = [
  {
    State: "Pending",
    Date: new Date("2025-07-10T10:00:00Z"),
    IsRequest: true,
    Challenge: challenge1!,
    User: user4!
  },
  {
    State: "Accepted",
    Date: new Date("2025-06-15T14:00:00Z"),
    IsRequest: false,
    Challenge: challenge2!,
    User: user1!
  },
  {
    State: "Rejected",
    Date: new Date("2025-07-01T09:30:00Z"),
    IsRequest: true,
    Challenge: challenge2!,
    User: user3!
  },
  {
    State: "Pending",
    Date: new Date("2025-07-12T11:15:00Z"),
    IsRequest: false,
    Challenge: challenge3!,
    User: user4!
  },
  {
    State: "Accepted",
    Date: new Date("2025-07-14T16:45:00Z"),
    IsRequest: true,
    Challenge: challenge1!,
    User: user2!
  }
];

const challengeInvites = ChallengeInvites.create(challengeInvitesSeedData);
await ChallengeInvites.save(challengeInvites);

// USER CHALLENGES

const userChallengesSeedData: Array<Partial<UserChallenges>> = [
  {
    IsLeader: true,
    AverageCompletion: 85.5,
    Challenge: challenge1!,
    User: user1!
  },
  {
    IsLeader: false,
    AverageCompletion: 60,
    Challenge: challenge1!,
    User: user2!
  },
  {
    IsLeader: false,
    AverageCompletion: 40,
    Challenge: challenge2!,
    User: user1!
  },
  {
    IsLeader: true,
    AverageCompletion: 70,
    Challenge: challenge2!,
    User: user2!
  },
  {
    IsLeader: true,
    AverageCompletion: 55,
    Challenge: challenge3!,
    User: user1!
  }
];

const userChallenges = UserChallenges.create(userChallengesSeedData);
await UserChallenges.save(userChallenges);

// CHALLENGE ACHIEVEMENTS

const challengeAchievementsSeedData: Array<Partial<ChallengeAchievements>> = [
  {
    Achievement: achievement1!,
    Challenge: challenge1!
  },
  {
    Achievement: achievement2!,
    Challenge: challenge1!
  },
  {
    Achievement: achievement3!,
    Challenge: challenge2!
  },
  {
    Achievement: achievement1!,
    Challenge: challenge3!
  },
  {
    Achievement: achievement2!,
    Challenge: challenge3!
  }
];

const challengeAchievements = ChallengeAchievements.create(challengeAchievementsSeedData);
await ChallengeAchievements.save(challengeAchievements);

// USER CHALLENGE ACHIEVEMENTS

const userChallengeAchievementsSeedData: Array<Partial<UserChallengeAchievements>> = [
  {
    Clear: true,
    UnlockDate: new Date("2025-07-01T10:00:00Z"),
    TotalCollected: 1,
    User: user1!,
    Challenge: challenge1!,
    Achievement: achievement1!,
  },
  {
    Clear: false,
    UnlockDate: null,
    TotalCollected: 0,
    User: user1!,
    Challenge: challenge1!,
    Achievement: achievement2!,
  },
  {
    Clear: true,
    UnlockDate: new Date("2025-06-20T12:00:00Z"),
    TotalCollected: 5,
    User: user2!,
    Challenge: challenge1!,
    Achievement: achievement1!,
  },
  {
    Clear: false,
    UnlockDate: null,
    TotalCollected: 0,
    User: user2!,
    Challenge: challenge2!,
    Achievement: achievement3!,
  }
];

const userChallengeAchievements = UserChallengeAchievements.create(userChallengeAchievementsSeedData);
await UserChallengeAchievements.save(userChallengeAchievements);

await appDataSource.destroy();

  console.log('Seed script finished.');
}





seed().catch((error) => {
  console.error('Seed script error:', error);
  process.exit(1);
});