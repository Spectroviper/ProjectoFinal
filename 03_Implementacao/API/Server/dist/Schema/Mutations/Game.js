"use strict";
var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.REMOVE_GAME = exports.ADD_GAME = exports.DELETE_GAME = exports.UPDATE_GAME = exports.CREATE_GAME = void 0;
const graphql_1 = require("graphql");
const Games_1 = require("../../Entities/Games");
const Date_1 = require("../CustomTypes/Date");
const Messages_1 = require("../TypeDef/Messages");
const Users_1 = require("../../Entities/Users");
const UserGames_1 = require("../../Entities/UserGames");
const UserAchievements_1 = require("../../Entities/UserAchievements");
const typeorm_1 = require("typeorm");
const Achievements_1 = require("../../Entities/Achievements");
const ChallengeAchievements_1 = require("../../Entities/ChallengeAchievements");
const UserChallengeAchievements_1 = require("../../Entities/UserChallengeAchievements");
const DataSource_1 = require("../../DataSource");
exports.CREATE_GAME = {
    type: Messages_1.MessageType,
    description: "Creates a Game in the Database",
    args: {
        GameName: { type: graphql_1.GraphQLString },
        About: { type: graphql_1.GraphQLString },
        Console: { type: graphql_1.GraphQLString },
        Developer: { type: graphql_1.GraphQLString },
        Publisher: { type: graphql_1.GraphQLString },
        Genre: { type: graphql_1.GraphQLString },
        CreatedBy: { type: graphql_1.GraphQLString },
        ReleaseDate: { type: Date_1.CustomDate },
        Image: { type: graphql_1.GraphQLString },
    },
    resolve(parent, args) {
        return __awaiter(this, void 0, void 0, function* () {
            yield DataSource_1.appDataSource.getRepository(Games_1.Games).insert(args);
            return { successfull: true, message: "GAME CREATED SUCCESSFULLY" };
        });
    },
};
exports.UPDATE_GAME = {
    type: Messages_1.MessageType,
    description: "Updates a Game's information in the Database",
    args: {
        GameId: { type: graphql_1.GraphQLID },
        NewGameName: { type: graphql_1.GraphQLString },
        NewAbout: { type: graphql_1.GraphQLString },
        NewConsole: { type: graphql_1.GraphQLString },
        NewDeveloper: { type: graphql_1.GraphQLString },
        NewPublisher: { type: graphql_1.GraphQLString },
        NewGenre: { type: graphql_1.GraphQLString },
        NewReleaseDate: { type: Date_1.CustomDate },
        NewImage: { type: graphql_1.GraphQLString },
    },
    resolve(parent, args) {
        return __awaiter(this, void 0, void 0, function* () {
            const { GameId, NewGameName, NewAbout, NewConsole, NewDeveloper, NewPublisher, NewGenre, NewReleaseDate, NewImage, } = args;
            const gameRepository = DataSource_1.appDataSource.getRepository(Games_1.Games);
            const game = yield gameRepository.findOneBy({ GameId });
            if (!game) {
                throw new Error("GAME DOES NOT EXIST!");
            }
            yield gameRepository.update({ GameId }, {
                GameName: NewGameName,
                About: NewAbout,
                Console: NewConsole,
                Developer: NewDeveloper,
                Publisher: NewPublisher,
                Genre: NewGenre,
                ReleaseDate: NewReleaseDate,
                Image: NewImage,
            });
            return {
                successfull: true,
                message: "GAME UPDATED SUCCESSFULLY",
            };
        });
    },
};
exports.DELETE_GAME = {
    type: Messages_1.MessageType,
    description: "Deletes a Game and its related information from the Database",
    args: {
        GameId: { type: graphql_1.GraphQLID },
    },
    resolve(parent, args) {
        return __awaiter(this, void 0, void 0, function* () {
            const gameIdNum = parseInt(args.GameId);
            if (isNaN(gameIdNum))
                throw new Error("Invalid GameId");
            const queryRunner = DataSource_1.appDataSource.createQueryRunner();
            yield queryRunner.connect();
            yield queryRunner.startTransaction();
            try {
                const game = yield queryRunner.manager.findOne(Games_1.Games, {
                    where: { GameId: gameIdNum },
                    relations: ['Achievements'],
                });
                if (!game)
                    throw new Error("Game not found");
                yield queryRunner.manager.delete(UserGames_1.UserGames, { Game: { GameId: gameIdNum } });
                const achievementIds = game.Achievements.map(a => a.AchievementId);
                if (achievementIds.length > 0) {
                    yield queryRunner.manager.delete(UserAchievements_1.UserAchievements, { Achievement: { AchievementId: (0, typeorm_1.In)(achievementIds) } });
                    yield queryRunner.manager.delete(ChallengeAchievements_1.ChallengeAchievements, { Achievement: { AchievementId: (0, typeorm_1.In)(achievementIds) } });
                    yield queryRunner.manager.delete(UserChallengeAchievements_1.UserChallengeAchievements, { Achievement: { AchievementId: (0, typeorm_1.In)(achievementIds) } });
                    yield queryRunner.manager.delete(Achievements_1.Achievements, { AchievementId: (0, typeorm_1.In)(achievementIds) });
                }
                yield queryRunner.manager.delete(Games_1.Games, { GameId: gameIdNum });
                yield queryRunner.commitTransaction();
                return { successfull: true, message: "GAME DELETED SUCCESSFULLY" };
            }
            catch (error) {
                yield queryRunner.rollbackTransaction();
                console.error("FAILED TO DELETE GAME:", error);
                return { successfull: false, message: "FAILED TO DELETE GAME." };
            }
            finally {
                yield queryRunner.release();
            }
        });
    }
};
exports.ADD_GAME = {
    type: Messages_1.MessageType,
    description: "Adds a relation between a User and a Game",
    args: {
        UserId: { type: graphql_1.GraphQLID },
        GameId: { type: graphql_1.GraphQLID },
    },
    resolve(parent, args) {
        return __awaiter(this, void 0, void 0, function* () {
            const { UserId, GameId } = args;
            const queryRunner = DataSource_1.appDataSource.createQueryRunner();
            yield queryRunner.connect();
            yield queryRunner.startTransaction();
            try {
                const user = yield queryRunner.manager.findOne(Users_1.Users, {
                    where: { UserId },
                    relations: ['UserGames', 'UserGames.Game', 'UserAchievements'],
                });
                const game = yield queryRunner.manager.findOne(Games_1.Games, {
                    where: { GameId },
                    relations: ['Achievements'],
                });
                if (!user || !game) {
                    throw new Error("USER OR GAME DO NOT EXIST!");
                }
                const hasGame = user.UserGames.some((ug) => ug.Game.GameId === game.GameId);
                if (hasGame) {
                    throw new Error("GAME IS ALREADY INCLUDED IN USER'S LIBRARY");
                }
                const userGame = new UserGames_1.UserGames();
                userGame.User = user;
                userGame.Game = game;
                yield queryRunner.manager.save(UserGames_1.UserGames, userGame);
                const userAchievementsToInsert = [];
                for (const achievement of game.Achievements) {
                    const ua = new UserAchievements_1.UserAchievements();
                    ua.User = user;
                    ua.Achievement = achievement;
                    ua.Clear = false;
                    ua.TotalCollected = 0;
                    ua.UnlockDate = null;
                    userAchievementsToInsert.push(ua);
                }
                if (userAchievementsToInsert.length > 0) {
                    yield queryRunner.manager.save(UserAchievements_1.UserAchievements, userAchievementsToInsert);
                }
                yield queryRunner.commitTransaction();
                return { successfull: true, message: "GAME ADDED SUCCESSFULLY" };
            }
            catch (err) {
                yield queryRunner.rollbackTransaction();
                throw err;
            }
            finally {
                yield queryRunner.release();
            }
        });
    },
};
exports.REMOVE_GAME = {
    type: Messages_1.MessageType,
    description: "Deletes a relation between a User and a Game",
    args: {
        UserId: { type: graphql_1.GraphQLID },
        GameId: { type: graphql_1.GraphQLID },
    },
    resolve(parent, args) {
        return __awaiter(this, void 0, void 0, function* () {
            const { UserId, GameId } = args;
            const queryRunner = DataSource_1.appDataSource.createQueryRunner();
            yield queryRunner.connect();
            yield queryRunner.startTransaction();
            try {
                const user = yield queryRunner.manager.findOne(Users_1.Users, {
                    where: { UserId },
                    relations: ['UserGames', 'UserAchievements'],
                });
                const game = yield queryRunner.manager.findOne(Games_1.Games, {
                    where: { GameId },
                    relations: ['Achievements'],
                });
                if (!user || !game) {
                    throw new Error("USER OR GAME NOT FOUND");
                }
                const userGame = yield queryRunner.manager.findOne(UserGames_1.UserGames, {
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
                const relatedUserAchievements = yield queryRunner.manager.find(UserAchievements_1.UserAchievements, {
                    where: {
                        User: { UserId: user.UserId },
                        Achievement: { AchievementId: (0, typeorm_1.In)(gameAchievementIds) },
                    },
                });
                if (relatedUserAchievements.length > 0) {
                    const uaIds = relatedUserAchievements.map((ua) => ua.UserAchievementId);
                    yield queryRunner.manager.delete(UserAchievements_1.UserAchievements, uaIds);
                }
                yield queryRunner.manager.delete(UserGames_1.UserGames, userGame.UserGameId);
                yield queryRunner.commitTransaction();
                return { successfull: true, message: "GAME REMOVED SUCCESSFULLY" };
            }
            catch (error) {
                yield queryRunner.rollbackTransaction();
                throw error;
            }
            finally {
                yield queryRunner.release();
            }
        });
    },
};
