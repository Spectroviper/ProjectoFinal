import { GraphQLID, GraphQLString } from "graphql";
import { Games } from "../../Entities/Games";
import { CustomDate } from "../CustomTypes/Date";
import { MessageType } from "../TypeDef/Messages";
import { Users } from "../../Entities/Users";
import { UserGames } from "../../Entities/UserGames";
import { UserAchievements } from "../../Entities/UserAchievements";
import { In } from "typeorm";
import { Achievements } from "../../Entities/Achievements";
import { ChallengeAchievements } from "../../Entities/ChallengeAchievements";
import { UserChallengeAchievements } from "../../Entities/UserChallengeAchievements";
import { appDataSource } from "../../DataSource";

export const CREATE_GAME = {
  type: MessageType,
  description: "Creates a Game in the Database",
  args: {
    GameName:    { type: GraphQLString },
    About:       { type: GraphQLString },
    Console:     { type: GraphQLString },
    Developer:   { type: GraphQLString },
    Publisher:   { type: GraphQLString },
    Genre:       { type: GraphQLString },
    CreatedBy:   { type: GraphQLString },
    ReleaseDate: { type: CustomDate },
    Image:       { type: GraphQLString },
  },
  async resolve(parent: any, args: any) {
    await appDataSource.getRepository(Games).insert(args);
    return { successfull: true, message: "GAME CREATED SUCCESSFULLY" };
  },
};


export const UPDATE_GAME = {
  type: MessageType,
  description: "Updates a Game's information in the Database",
  args: {
    GameId:        { type: GraphQLID },
    NewGameName:   { type: GraphQLString },
    NewAbout:      { type: GraphQLString },
    NewConsole:    { type: GraphQLString },
    NewDeveloper:  { type: GraphQLString },
    NewPublisher:  { type: GraphQLString },
    NewGenre:      { type: GraphQLString },
    NewReleaseDate:{ type: CustomDate },
    NewImage:      { type: GraphQLString },
  },
  async resolve(parent: any, args: any) {
    const {
      GameId,
      NewGameName,
      NewAbout,
      NewConsole,
      NewDeveloper,
      NewPublisher,
      NewGenre,
      NewReleaseDate,
      NewImage,
    } = args;

    const gameRepository = appDataSource.getRepository(Games);
    const game = await gameRepository.findOneBy({ GameId });

    if (!game) {
      throw new Error("GAME DOES NOT EXIST!");
    }

    await gameRepository.update(
      { GameId },
      {
        GameName:    NewGameName,
        About:       NewAbout,
        Console:     NewConsole,
        Developer:   NewDeveloper,
        Publisher:   NewPublisher,
        Genre:       NewGenre,
        ReleaseDate: NewReleaseDate,
        Image:       NewImage,
      }
    );

    return {
      successfull: true,
      message: "GAME UPDATED SUCCESSFULLY",
    };
  },
};

export const DELETE_GAME = {
  type: MessageType,
  description: "Deletes a Game and its related information from the Database",
  args: {
    GameId: { type: GraphQLID },
  },
  async resolve(parent: any, args: any) {
    const gameIdNum = parseInt(args.GameId);
    if (isNaN(gameIdNum)) throw new Error("Invalid GameId");

    const queryRunner = appDataSource.createQueryRunner();

    await queryRunner.connect();
    await queryRunner.startTransaction();

    try {
      const game = await queryRunner.manager.findOne(Games, {
        where: { GameId: gameIdNum },
        relations: ['Achievements'],
      });
      if (!game) throw new Error("Game not found");

      await queryRunner.manager.delete(UserGames, { Game: { GameId: gameIdNum } });

      const achievementIds = game.Achievements.map(a => a.AchievementId);

      if (achievementIds.length > 0) {
        await queryRunner.manager.delete(UserAchievements, { Achievement: { AchievementId: In(achievementIds) } });
        await queryRunner.manager.delete(ChallengeAchievements, { Achievement: { AchievementId: In(achievementIds) } });
        await queryRunner.manager.delete(UserChallengeAchievements, { Achievement: { AchievementId: In(achievementIds) } });
        await queryRunner.manager.delete(Achievements, { AchievementId: In(achievementIds) });
      }

      await queryRunner.manager.delete(Games, { GameId: gameIdNum });

      await queryRunner.commitTransaction();

      return { successfull: true, message: "GAME DELETED SUCCESSFULLY" };
    } catch (error) {
      await queryRunner.rollbackTransaction();
      console.error("FAILED TO DELETE GAME:", error);
      return { successfull: false, message: "FAILED TO DELETE GAME." };
    } finally {
      await queryRunner.release();
    }
  }
};



export const ADD_GAME = {
  type: MessageType,
  description: "Adds a relation between a User and a Game",
  args: {
    UserId: { type: GraphQLID },
    GameId: { type: GraphQLID },
  },
  async resolve(parent: any, args: any) {
    const { UserId, GameId } = args;
    const queryRunner = appDataSource.createQueryRunner();

    await queryRunner.connect();
    await queryRunner.startTransaction();

    try {
      const user = await queryRunner.manager.findOne(Users, {
        where: { UserId },
        relations: ['UserGames', 'UserGames.Game', 'UserAchievements'],
      });

      const game = await queryRunner.manager.findOne(Games, {
        where: { GameId },
        relations: ['Achievements'],
      });

      if (!user || !game) {
        throw new Error("USER OR GAME DO NOT EXIST!");
      }

      const hasGame = user.UserGames.some(
        (ug) => ug.Game.GameId === game.GameId
      );

      if (hasGame) {
        throw new Error("GAME IS ALREADY INCLUDED IN USER'S LIBRARY");
      }

      const userGame = new UserGames();
      userGame.User = user;
      userGame.Game = game;
      await queryRunner.manager.save(UserGames, userGame);

      const userAchievementsToInsert: UserAchievements[] = [];

      for (const achievement of game.Achievements) {
        const ua = new UserAchievements();
        ua.User = user;
        ua.Achievement = achievement;
        ua.Clear = false;
        ua.TotalCollected = 0;
        ua.UnlockDate = null;
        userAchievementsToInsert.push(ua);
      }

      if (userAchievementsToInsert.length > 0) {
        await queryRunner.manager.save(UserAchievements, userAchievementsToInsert);
      }

      await queryRunner.commitTransaction();

      return { successfull: true, message: "GAME ADDED SUCCESSFULLY" };
    } catch (err) {
      await queryRunner.rollbackTransaction();
      throw err;
    } finally {
      await queryRunner.release();
    }
  },
};


export const REMOVE_GAME = {
  type: MessageType,
  description: "Deletes a relation between a User and a Game",
  args: {
    UserId: { type: GraphQLID },
    GameId: { type: GraphQLID },
  },
  async resolve(parent: any, args: any) {
    const { UserId, GameId } = args;

    const queryRunner = appDataSource.createQueryRunner();
    await queryRunner.connect();
    await queryRunner.startTransaction();

    try {
      const user = await queryRunner.manager.findOne(Users, {
        where: { UserId },
        relations: ['UserGames', 'UserAchievements'],
      });

      const game = await queryRunner.manager.findOne(Games, {
        where: { GameId },
        relations: ['Achievements'],
      });

      if (!user || !game) {
        throw new Error("USER OR GAME NOT FOUND");
      }

      const userGame = await queryRunner.manager.findOne(UserGames, {
        where: {
          User: { UserId: user.UserId },
          Game: { GameId: game.GameId },
        },
        relations: ['Game', 'User'],
      });

      if (!userGame) {
        throw new Error("USER DOES NOT HAVE THIS GAME");
      }

      const gameAchievementIds = game.Achievements.map((ach) => ach.AchievementId);

      const relatedUserAchievements = await queryRunner.manager.find(UserAchievements, {
        where: {
          User: { UserId: user.UserId },
          Achievement: { AchievementId: In(gameAchievementIds) },
        },
      });

      if (relatedUserAchievements.length > 0) {
        const uaIds = relatedUserAchievements.map((ua) => ua.UserAchievementId);
        await queryRunner.manager.delete(UserAchievements, uaIds);
      }

      await queryRunner.manager.delete(UserGames, userGame.UserGameId);

      await queryRunner.commitTransaction();

      return { successfull: true, message: "GAME REMOVED SUCCESSFULLY" };
    } catch (error) {
      await queryRunner.rollbackTransaction();
      throw error;
    } finally {
      await queryRunner.release();
    }
  },
};