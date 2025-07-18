import { GraphQLID, GraphQLInt, GraphQLString } from "graphql";
import { Achievements } from "../../Entities/Achievements";
import { Games } from "../../Entities/Games";
import { MessageType } from "../TypeDef/Messages";
import { Users } from "../../Entities/Users";
import { UserAchievements } from "../../Entities/UserAchievements";
import { UserGames } from "../../Entities/UserGames";
import { ChallengeAchievements } from "../../Entities/ChallengeAchievements";
import { UserChallengeAchievements } from "../../Entities/UserChallengeAchievements";
import { appDataSource } from "../../DataSource";

export const CREATE_ACHIEVEMENT = {
  type: MessageType,
  description: "Creates an Achievement in the Database",
  args: {
    AchievementName:     { type: GraphQLString },
    About:               { type: GraphQLString },
    TotalCollectable:    { type: GraphQLInt },
    CreatedBy:           { type: GraphQLString },
    Image:               { type: GraphQLString },
    GameID:              { type: GraphQLID },
  },
  async resolve(parent: any, args: any) {
    const {
      AchievementName,
      About,
      TotalCollectable,
      CreatedBy,
      Image,
      GameID,
    } = args;

    const queryRunner = appDataSource.createQueryRunner();
    await queryRunner.connect();
    await queryRunner.startTransaction();

    try {
      const game = await queryRunner.manager.findOne(Games, {
        where: { GameId: GameID },
        relations: ['UserGames', 'UserGames.User'],
      });

      if (!game) {
        throw new Error("GAME DOES NOT EXIST");
      }

      const achievement = new Achievements();
      achievement.AchievementName = AchievementName;
      achievement.About = About;
      achievement.CreatedBy = CreatedBy;
      achievement.TotalCollectable = TotalCollectable;
      achievement.Image = Image;
      achievement.Game = game;

      const insertedAchievement = await queryRunner.manager.save(Achievements, achievement);

      const userAchievementsToInsert: UserAchievements[] = [];

      for (const userGame of game.UserGames || []) {
        const user = userGame.User;

        const userAchievement = new UserAchievements();
        userAchievement.User = user;
        userAchievement.Achievement = insertedAchievement;
        userAchievement.Clear = false;
        userAchievement.TotalCollected = 0;
        userAchievement.UnlockDate = null;

        userAchievementsToInsert.push(userAchievement);
      }

      if (userAchievementsToInsert.length > 0) {
        await queryRunner.manager.save(UserAchievements, userAchievementsToInsert);
      }

      await queryRunner.commitTransaction();
      return {
        successfull: true,
        message: "ACHIEVEMENT CREATED SUCCESSFULLY",
      };

    } catch (err) {
      await queryRunner.rollbackTransaction();
      throw err;
    } finally {
      await queryRunner.release();
    }
  },
};


export const UPDATE_ACHIEVEMENT = {
  type: MessageType,
  description: "Updates the information of an Achievement in the Database",
  args: {
    AchievementId:         { type: GraphQLID },
    NewAchievementName:    { type: GraphQLString },
    NewAbout:              { type: GraphQLString },
    NewTotalCollectable:   { type: GraphQLInt },
    NewImage:              { type: GraphQLString },
  },
  async resolve(parent: any, args: any) {
    const {
      AchievementId,
      NewAchievementName,
      NewAbout,
      NewTotalCollectable,
      NewImage,
    } = args;

    const queryRunner = appDataSource.createQueryRunner();

    await queryRunner.connect();
    await queryRunner.startTransaction();

    try {
      const achievement = await queryRunner.manager.findOne(Achievements, {
        where: { AchievementId },
        relations: ['UserAchievements'],
      });

      if (!achievement) {
        throw new Error("ACHIEVEMENT DOES NOT EXIST!");
      }

      await queryRunner.manager.update(
        Achievements,
        { AchievementId },
        {
          AchievementName:   NewAchievementName,
          About:             NewAbout,
          TotalCollectable:  NewTotalCollectable,
          Image:             NewImage,
        }
      );

      for (const ua of achievement.UserAchievements || []) {
        const prev = ua.TotalCollected ?? 0;
        const clamped = Math.min(prev, NewTotalCollectable);

        ua.TotalCollected = clamped;
        ua.Clear = clamped >= NewTotalCollectable;

        await queryRunner.manager.save(UserAchievements, ua);
      }

      await queryRunner.commitTransaction();

      return {
        successfull: true,
        message: "ACHIEVEMENT UPDATED SUCCESSFULLY",
      };
    } catch (err) {
      await queryRunner.rollbackTransaction();
      throw err;
    } finally {
      await queryRunner.release();
    }
  },
};


export const DELETE_ACHIEVEMENT = {
  type: MessageType,
  description: "Deletes an Achievement and its related information from the Database",
  args: {
    AchievementId: { type: GraphQLID },
  },
  async resolve(parent: any, args: any) {
    const achievementIdNum = parseInt(args.AchievementId);
    if (isNaN(achievementIdNum)) throw new Error("Invalid AchievementId");

    const queryRunner = appDataSource.createQueryRunner();

    await queryRunner.connect();
    await queryRunner.startTransaction();

    try {
      const achievement = await queryRunner.manager.findOne(Achievements, {
        where: { AchievementId: achievementIdNum },
      });
      if (!achievement) throw new Error("Achievement not found");

      await queryRunner.manager.delete(UserAchievements, { Achievement: { AchievementId: achievementIdNum } });
      await queryRunner.manager.delete(ChallengeAchievements, { Achievement: { AchievementId: achievementIdNum } });
      await queryRunner.manager.delete(UserChallengeAchievements, { Achievement: { AchievementId: achievementIdNum } });

      await queryRunner.manager.delete(Achievements, { AchievementId: achievementIdNum });

      await queryRunner.commitTransaction();

      return { successfull: true, message: "ACHIEVEMENT DELETED SUCCESSFULLY" };
    } catch (error) {
      await queryRunner.rollbackTransaction();
      console.error("FAILED TO DELETE ACHIEVEMENT:", error);
      return { successfull: false, message: "FAILED TO DELETE ACHIEVEMENT." };
    } finally {
      await queryRunner.release();
    }
  }
};

export const LOCK_UNLOCK_ACHIEVEMENT = {
  type: MessageType,
  description:
    "Locks or Unlocks an Achievement depending if its Clear attribute was previously True or False",
  args: {
    UserId: { type: GraphQLID },
    UserAchievementId: { type: GraphQLID },
  },
  async resolve(parent: any, args: any) {
    const { UserId, UserAchievementId } = args;

    const queryRunner = appDataSource.createQueryRunner();
    await queryRunner.connect();
    await queryRunner.startTransaction();

    try {
      const user = await queryRunner.manager
        .createQueryBuilder(Users, "user")
        .leftJoinAndSelect("user.UserAchievements", "ua")
        .where("user.UserId = :userId", { userId: UserId })
        .getOne();

      if (!user) throw new Error("USER NOT FOUND");

      const userAchievement = await queryRunner.manager
        .createQueryBuilder(UserAchievements, "ua")
        .leftJoinAndSelect("ua.Achievement", "achievement")
        .leftJoinAndSelect("ua.User", "user")
        .where("ua.UserAchievementId = :uaId", { uaId: UserAchievementId })
        .getOne();

      if (!userAchievement) throw new Error("USER ACHIEVEMENT NOT FOUND");

      const validIds = user.UserAchievements.map((ua) => ua.UserAchievementId);
      if (!validIds.includes(userAchievement.UserAchievementId)) {
        throw new Error("USER ACHIEVEMENT DOES NOT BELONG TO USER");
      }

      if (userAchievement.Clear) {
        userAchievement.Clear = false;
        userAchievement.UnlockDate = null;
      } else {
        userAchievement.Clear = true;
        userAchievement.UnlockDate = new Date();
      }

      await queryRunner.manager.save(userAchievement);

      const game = await queryRunner.manager
        .createQueryBuilder(Games, "game")
        .leftJoinAndSelect("game.Achievements", "achievement")
        .where("achievement.AchievementId = :achievementId", {
          achievementId: userAchievement.Achievement.AchievementId,
        })
        .getOne();

      if (!game) throw new Error("RELATED GAME NOT FOUND");

      const userGame = await queryRunner.manager
        .createQueryBuilder(UserGames, "ug")
        .where("ug.UserId = :userId", { userId: UserId })
        .andWhere("ug.GameId = :gameId", { gameId: game.GameId })
        .getOne();

      if (!userGame) throw new Error("USERGAME RELATION NOT FOUND");

      const totalAchievements = game.Achievements.length;

      const unlocked = await queryRunner.manager.count(UserAchievements, {
        where: {
          User: { UserId: parseInt(UserId) },
          Clear: true,
          Achievement: { Game: { GameId: game.GameId } },
        },
        relations: ['Achievement', 'Achievement.Game'],
      });

      userGame.AverageCompletion =
        totalAchievements === 0
          ? 0
          : Math.floor((unlocked / totalAchievements) * 100);

      await queryRunner.manager.save(userGame);

      await queryRunner.commitTransaction();

      return {
        successfull: true,
        message: userAchievement.Clear ? "ACHIEVEMENT UNLOCKED!" : "ACHIEVEMENT LOCKED!",
      };
    } catch (error) {
      await queryRunner.rollbackTransaction();
      console.error("ERROR LOCKING/UNLOCKING ACHIEVEMENT:", error);
      return {
        successfull: false,
        message: "FAILED TO LOCK/UNLOCK ACHIEVEMENT",
      };
    } finally {
      await queryRunner.release();
    }
  },
};