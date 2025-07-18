import { GraphQLID, GraphQLList, GraphQLString } from 'graphql'
import { Users } from '../../Entities/Users';
import { UserType} from '../TypeDef/UserType'
import { UserGameType } from '../TypeDef/UserGameType';
import { UserGames } from '../../Entities/UserGames';
import { Friends } from '../../Entities/Friends';
import { appDataSource } from "../../DataSource";
import { Challenges } from '../../Entities/Challenges';
import { UserAchievements } from '../../Entities/UserAchievements';
import { UserAchievementType } from '../TypeDef/UserAchievementType';

export const GET_ALL_USERS = {
  type: new GraphQLList(UserType),
  description: "Get all Users in the Database except the one with the given UserId",
  args: {
    UserId: { type: GraphQLID },
  },
  async resolve(parent: any, args: any) {
    const { UserId } = args;

    return await appDataSource
      .getRepository(Users)
      .createQueryBuilder("user")
      .where("user.UserId != :userId", { userId: UserId })
      .getMany();
  },
};

export const GET_USER_BY_USERID = {
  type: UserType,
  description: "Get a User by ID with only Challenge-related information",
  args: {
    UserId: { type: GraphQLID }
  },
  async resolve(parent: any, args: any) {
    const { UserId } = args;

    const user = await appDataSource
      .getRepository(Users)
      .createQueryBuilder("user")
      .leftJoinAndSelect("user.UserChallenges", "userChallenges")
      .leftJoinAndSelect("userChallenges.Challenge", "challenge")
      .where("user.UserId = :id", { id: UserId })
      .getOne();

    if (!user) {
      throw new Error("USER DOES NOT EXIST");
    }

    return user;
  }
};

export const GET_ALL_USERS_BY_TOTALPOINTS = {
  type: new GraphQLList(UserType),
  description: "Get all Users in the Database ordered by TotalPoints",
  async resolve() {
    return await appDataSource
      .getRepository(Users)
      .createQueryBuilder('user')
      .orderBy('user.TotalPoints', 'DESC')
      .getMany();
  }
};

export const GET_ALL_USER_FRIENDS = {
  type: new GraphQLList(UserType),
  description: "Search all the Friends a User is related to",
  args: {
    UserId: { type: GraphQLID },
  },
  async resolve(parent: any, args: any) {
    const { UserId } = args;

    try {
      const friends = await appDataSource
        .getRepository(Friends)
        .createQueryBuilder("friend")
        .leftJoinAndSelect("friend.UserSender", "sender")
        .leftJoinAndSelect("friend.UserReceiver", "receiver")
        .where("(sender.UserId = :userId OR receiver.UserId = :userId) AND friend.State = :state", {
          userId: UserId,
          state: "Friends",
        })
        .getMany();

      const relatedUsers: Users[] = friends.map(friend => {
        if (friend.UserSender.UserId === parseInt(UserId)) {
          return friend.UserReceiver;
        } else {
          return friend.UserSender;
        }
      });

      return relatedUsers;
    } catch (err) {
      console.error("Error fetching user friends:", err);
      throw new Error("Failed to fetch user friends.");
    }
  },
};

export const GET_ALL_USERS_NOT_IN_CHALLENGE = {
  type: new GraphQLList(UserType),
  description: "Get all Users NOT participating in a specific Challenge",
  args: {
    ChallengeId: { type: GraphQLID },
  },
  async resolve(parent: any, args: any) {
    const { ChallengeId } = args;

    const challengeExists = await appDataSource
      .getRepository(Challenges)
      .createQueryBuilder("challenge")
      .where("challenge.ChallengeId = :challengeId", { challengeId: ChallengeId })
      .getExists();

    if (!challengeExists) {
      throw new Error("CHALLENGE DOES NOT EXIST!");
    }

    const userRepository = appDataSource.getRepository(Users);

    const subQuery = userRepository
      .createQueryBuilder("userSub")
      .leftJoin("userSub.UserChallenges", "userChallengeSub")
      .select("userSub.UserId")
      .where("userChallengeSub.ChallengeId = :challengeId", { challengeId: ChallengeId });

    const users = await userRepository
      .createQueryBuilder("user")
      .where(`user.UserId NOT IN (${subQuery.getQuery()})`)
      .setParameters(subQuery.getParameters())
      .orderBy("user.UserName", "ASC")
      .getMany();

    return users;
  },
};

export const SEARCH_USERS = {
  type: new GraphQLList(UserType),
  description: "Search Users in the Database by UserName",
  args: {
    UserName: { type: GraphQLString }
  },
  async resolve(parent: any, args: any) {
    const { UserName } = args;

    try {
      const query = appDataSource
        .getRepository(Users)
        .createQueryBuilder('user');

      if (UserName) {
        query.andWhere('LOWER(user.UserName) LIKE LOWER(:userName)', {
          userName: `%${UserName}%`,
        });
      }

      const users = await query.getMany();
      return users;
    } catch (err) {
      console.error('Search failed:', err);
      throw new Error('Failed to search users.');
    }
  },
};

export const SEARCH_USER_GAMES = {
  type: new GraphQLList(UserGameType),
  description: "Search all the Games a User is related to",
  args: {
    UserId: { type: GraphQLID },
    GameName: { type: GraphQLString }
  },
  async resolve(parent: any, args: any) {
    const { UserId, GameName } = args;

    try {
      const query = appDataSource
        .getRepository(UserGames)
        .createQueryBuilder('userGame')
        .leftJoinAndSelect('userGame.Game', 'game')
        .where('userGame.User = :userId', { userId: UserId });

      if (GameName) {
        query.andWhere('LOWER(game.GameName) LIKE LOWER(:gameName)', {
          gameName: `%${GameName}%`
        });
      }

      return await query.getMany();
    } catch (err) {
      console.error('Search user games failed:', err);
      throw new Error('Failed to search user games.');
    }
  }
};


export const GET_USER_USER_GAMES = {
  type: new GraphQLList(UserGameType),
  description: "Returns all UserGames for a given user",
  args: {
    UserId: { type: GraphQLID },
  },
  async resolve(parent: any, args: any) {
    const { UserId } = args;

    return await appDataSource.transaction(async (manager) => {
      const user = await manager
        .createQueryBuilder(Users, "user")
        .where("user.UserId = :userId", { userId: UserId })
        .getOne();

      if (!user) {
        throw new Error("USER DOES NOT EXIST!");
      }

      const userGames = await manager
        .createQueryBuilder(UserGames, "ug")
        .leftJoinAndSelect("ug.Game", "game")
        .where("ug.UserId = :userId", { userId: UserId })
        .getMany();

      return userGames;
    });
  },
};

export const GET_USER_USER_ACHIEVEMENTS = {
  type: new GraphQLList(UserAchievementType),
  description: "Returns all UserAhievements for a given user",
  args: {
    UserId: { type: GraphQLID },
  },
  async resolve(parent: any, args: any) {
    const { UserId } = args;

    return await appDataSource.transaction(async (manager) => {
      const user = await manager
        .createQueryBuilder(Users, "user")
        .where("user.UserId = :userId", { userId: UserId })
        .getOne();

      if (!user) {
        throw new Error("USER DOES NOT EXIST!");
      }

      const userAchievements = await manager
        .createQueryBuilder(UserAchievements, "ua")
        .leftJoinAndSelect("ua.Achievement", "achievement")
        .where("ua.UserId = :userId", { userId: UserId })
        .getMany();

      return userAchievements;
    });
  },
};