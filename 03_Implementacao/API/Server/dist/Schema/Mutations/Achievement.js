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
exports.LOCK_UNLOCK_ACHIEVEMENT = exports.DELETE_ACHIEVEMENT = exports.UPDATE_ACHIEVEMENT = exports.CREATE_ACHIEVEMENT = void 0;
const graphql_1 = require("graphql");
const Achievements_1 = require("../../Entities/Achievements");
const Games_1 = require("../../Entities/Games");
const Messages_1 = require("../TypeDef/Messages");
const Users_1 = require("../../Entities/Users");
const UserAchievements_1 = require("../../Entities/UserAchievements");
const UserGames_1 = require("../../Entities/UserGames");
const ChallengeAchievements_1 = require("../../Entities/ChallengeAchievements");
const UserChallengeAchievements_1 = require("../../Entities/UserChallengeAchievements");
const DataSource_1 = require("../../DataSource");
exports.CREATE_ACHIEVEMENT = {
    type: Messages_1.MessageType,
    description: "Creates an Achievement in the Database",
    args: {
        AchievementName: { type: graphql_1.GraphQLString },
        About: { type: graphql_1.GraphQLString },
        TotalCollectable: { type: graphql_1.GraphQLInt },
        CreatedBy: { type: graphql_1.GraphQLString },
        Image: { type: graphql_1.GraphQLString },
        GameID: { type: graphql_1.GraphQLID },
    },
    resolve(parent, args) {
        return __awaiter(this, void 0, void 0, function* () {
            const { AchievementName, About, TotalCollectable, CreatedBy, Image, GameID, } = args;
            const queryRunner = DataSource_1.appDataSource.createQueryRunner();
            yield queryRunner.connect();
            yield queryRunner.startTransaction();
            try {
                const game = yield queryRunner.manager.findOne(Games_1.Games, {
                    where: { GameId: GameID },
                    relations: ['UserGames', 'UserGames.User'],
                });
                if (!game) {
                    throw new Error("GAME DOES NOT EXIST");
                }
                const achievement = new Achievements_1.Achievements();
                achievement.AchievementName = AchievementName;
                achievement.About = About;
                achievement.CreatedBy = CreatedBy;
                achievement.TotalCollectable = TotalCollectable;
                achievement.Image = Image;
                achievement.Game = game;
                const insertedAchievement = yield queryRunner.manager.save(Achievements_1.Achievements, achievement);
                const userAchievementsToInsert = [];
                for (const userGame of game.UserGames || []) {
                    const user = userGame.User;
                    const userAchievement = new UserAchievements_1.UserAchievements();
                    userAchievement.User = user;
                    userAchievement.Achievement = insertedAchievement;
                    userAchievement.Clear = false;
                    userAchievement.TotalCollected = 0;
                    userAchievement.UnlockDate = null;
                    userAchievementsToInsert.push(userAchievement);
                }
                if (userAchievementsToInsert.length > 0) {
                    yield queryRunner.manager.save(UserAchievements_1.UserAchievements, userAchievementsToInsert);
                }
                yield queryRunner.commitTransaction();
                return {
                    successfull: true,
                    message: "ACHIEVEMENT CREATED SUCCESSFULLY",
                };
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
exports.UPDATE_ACHIEVEMENT = {
    type: Messages_1.MessageType,
    description: "Updates the information of an Achievement in the Database",
    args: {
        AchievementId: { type: graphql_1.GraphQLID },
        NewAchievementName: { type: graphql_1.GraphQLString },
        NewAbout: { type: graphql_1.GraphQLString },
        NewTotalCollectable: { type: graphql_1.GraphQLInt },
        NewImage: { type: graphql_1.GraphQLString },
    },
    resolve(parent, args) {
        return __awaiter(this, void 0, void 0, function* () {
            var _a;
            const { AchievementId, NewAchievementName, NewAbout, NewTotalCollectable, NewImage, } = args;
            const queryRunner = DataSource_1.appDataSource.createQueryRunner();
            yield queryRunner.connect();
            yield queryRunner.startTransaction();
            try {
                const achievement = yield queryRunner.manager.findOne(Achievements_1.Achievements, {
                    where: { AchievementId },
                    relations: ['UserAchievements'],
                });
                if (!achievement) {
                    throw new Error("ACHIEVEMENT DOES NOT EXIST!");
                }
                yield queryRunner.manager.update(Achievements_1.Achievements, { AchievementId }, {
                    AchievementName: NewAchievementName,
                    About: NewAbout,
                    TotalCollectable: NewTotalCollectable,
                    Image: NewImage,
                });
                for (const ua of achievement.UserAchievements || []) {
                    const prev = (_a = ua.TotalCollected) !== null && _a !== void 0 ? _a : 0;
                    const clamped = Math.min(prev, NewTotalCollectable);
                    ua.TotalCollected = clamped;
                    ua.Clear = clamped >= NewTotalCollectable;
                    yield queryRunner.manager.save(UserAchievements_1.UserAchievements, ua);
                }
                yield queryRunner.commitTransaction();
                return {
                    successfull: true,
                    message: "ACHIEVEMENT UPDATED SUCCESSFULLY",
                };
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
exports.DELETE_ACHIEVEMENT = {
    type: Messages_1.MessageType,
    description: "Deletes an Achievement and its related information from the Database",
    args: {
        AchievementId: { type: graphql_1.GraphQLID },
    },
    resolve(parent, args) {
        return __awaiter(this, void 0, void 0, function* () {
            const achievementIdNum = parseInt(args.AchievementId);
            if (isNaN(achievementIdNum))
                throw new Error("Invalid AchievementId");
            const queryRunner = DataSource_1.appDataSource.createQueryRunner();
            yield queryRunner.connect();
            yield queryRunner.startTransaction();
            try {
                const achievement = yield queryRunner.manager.findOne(Achievements_1.Achievements, {
                    where: { AchievementId: achievementIdNum },
                });
                if (!achievement)
                    throw new Error("Achievement not found");
                yield queryRunner.manager.delete(UserAchievements_1.UserAchievements, { Achievement: { AchievementId: achievementIdNum } });
                yield queryRunner.manager.delete(ChallengeAchievements_1.ChallengeAchievements, { Achievement: { AchievementId: achievementIdNum } });
                yield queryRunner.manager.delete(UserChallengeAchievements_1.UserChallengeAchievements, { Achievement: { AchievementId: achievementIdNum } });
                yield queryRunner.manager.delete(Achievements_1.Achievements, { AchievementId: achievementIdNum });
                yield queryRunner.commitTransaction();
                return { successfull: true, message: "ACHIEVEMENT DELETED SUCCESSFULLY" };
            }
            catch (error) {
                yield queryRunner.rollbackTransaction();
                console.error("FAILED TO DELETE ACHIEVEMENT:", error);
                return { successfull: false, message: "FAILED TO DELETE ACHIEVEMENT." };
            }
            finally {
                yield queryRunner.release();
            }
        });
    }
};
exports.LOCK_UNLOCK_ACHIEVEMENT = {
    type: Messages_1.MessageType,
    description: "Locks or Unlocks an Achievement depending if its Clear attribute was previously True or False",
    args: {
        UserId: { type: graphql_1.GraphQLID },
        UserAchievementId: { type: graphql_1.GraphQLID },
    },
    resolve(parent, args) {
        return __awaiter(this, void 0, void 0, function* () {
            const { UserId, UserAchievementId } = args;
            const queryRunner = DataSource_1.appDataSource.createQueryRunner();
            yield queryRunner.connect();
            yield queryRunner.startTransaction();
            try {
                const user = yield queryRunner.manager
                    .createQueryBuilder(Users_1.Users, "user")
                    .leftJoinAndSelect("user.UserAchievements", "ua")
                    .where("user.UserId = :userId", { userId: UserId })
                    .getOne();
                if (!user)
                    throw new Error("USER NOT FOUND");
                const userAchievement = yield queryRunner.manager
                    .createQueryBuilder(UserAchievements_1.UserAchievements, "ua")
                    .leftJoinAndSelect("ua.Achievement", "achievement")
                    .leftJoinAndSelect("ua.User", "user")
                    .where("ua.UserAchievementId = :uaId", { uaId: UserAchievementId })
                    .getOne();
                if (!userAchievement)
                    throw new Error("USER ACHIEVEMENT NOT FOUND");
                const validIds = user.UserAchievements.map((ua) => ua.UserAchievementId);
                if (!validIds.includes(userAchievement.UserAchievementId)) {
                    throw new Error("USER ACHIEVEMENT DOES NOT BELONG TO USER");
                }
                if (userAchievement.Clear) {
                    userAchievement.Clear = false;
                    userAchievement.UnlockDate = null;
                }
                else {
                    userAchievement.Clear = true;
                    userAchievement.UnlockDate = new Date();
                }
                yield queryRunner.manager.save(userAchievement);
                const game = yield queryRunner.manager
                    .createQueryBuilder(Games_1.Games, "game")
                    .leftJoinAndSelect("game.Achievements", "achievement")
                    .where("achievement.AchievementId = :achievementId", {
                    achievementId: userAchievement.Achievement.AchievementId,
                })
                    .getOne();
                if (!game)
                    throw new Error("RELATED GAME NOT FOUND");
                const userGame = yield queryRunner.manager
                    .createQueryBuilder(UserGames_1.UserGames, "ug")
                    .where("ug.UserId = :userId", { userId: UserId })
                    .andWhere("ug.GameId = :gameId", { gameId: game.GameId })
                    .getOne();
                if (!userGame)
                    throw new Error("USERGAME RELATION NOT FOUND");
                const totalAchievements = game.Achievements.length;
                const unlocked = yield queryRunner.manager.count(UserAchievements_1.UserAchievements, {
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
                yield queryRunner.manager.save(userGame);
                yield queryRunner.commitTransaction();
                return {
                    successfull: true,
                    message: userAchievement.Clear ? "ACHIEVEMENT UNLOCKED!" : "ACHIEVEMENT LOCKED!",
                };
            }
            catch (error) {
                yield queryRunner.rollbackTransaction();
                console.error("ERROR LOCKING/UNLOCKING ACHIEVEMENT:", error);
                return {
                    successfull: false,
                    message: "FAILED TO LOCK/UNLOCK ACHIEVEMENT",
                };
            }
            finally {
                yield queryRunner.release();
            }
        });
    },
};
