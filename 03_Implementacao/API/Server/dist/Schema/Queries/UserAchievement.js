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
exports.GET_USER_ACHIEVEMENT_BY_USER_ACHIEVEMENT_ID = exports.GET_USER_GAME_BY_USER_GAME_ID = void 0;
const graphql_1 = require("graphql");
const UserGameType_1 = require("../TypeDef/UserGameType");
const DataSource_1 = require("../../DataSource");
const UserGames_1 = require("../../Entities/UserGames");
const UserAchievementType_1 = require("../TypeDef/UserAchievementType");
const UserAchievements_1 = require("../../Entities/UserAchievements");
exports.GET_USER_GAME_BY_USER_GAME_ID = {
    type: UserGameType_1.UserGameType,
    description: "Returns a UserGame with its related UserAchievements",
    args: {
        UserGameId: { type: graphql_1.GraphQLID }
    },
    resolve(parent, args) {
        return __awaiter(this, void 0, void 0, function* () {
            const { UserGameId } = args;
            const userGame = yield DataSource_1.appDataSource
                .getRepository(UserGames_1.UserGames)
                .createQueryBuilder("ug")
                .leftJoinAndSelect("ug.User", "user")
                .leftJoinAndSelect("ug.Game", "game")
                .leftJoinAndSelect("game.Achievements", "achievement")
                .leftJoinAndSelect("achievement.UserAchievements", "userAchievement", "userAchievement.UserId = user.UserId")
                .where("ug.UserGameId = :userGameId", { userGameId: UserGameId })
                .getOne();
            if (!userGame) {
                throw new Error("USER GAME DOES NOT EXIST");
            }
            return userGame;
        });
    },
};
exports.GET_USER_ACHIEVEMENT_BY_USER_ACHIEVEMENT_ID = {
    type: UserAchievementType_1.UserAchievementType,
    description: "Returns a UserAchievement from the BD",
    args: {
        UserAchievementId: { type: graphql_1.GraphQLID }
    },
    resolve(parent, args) {
        return __awaiter(this, void 0, void 0, function* () {
            const { UserAchievementId } = args;
            const userAchievement = yield DataSource_1.appDataSource
                .getRepository(UserAchievements_1.UserAchievements)
                .createQueryBuilder("ua")
                .leftJoinAndSelect("ua.Achievement", "achievement")
                .where("ua.UserAchievementId = :userAchievementId", { userAchievementId: UserAchievementId })
                .getOne();
            if (!userAchievement) {
                throw new Error("USER ACHIEVEMENT DOES NOT EXIST");
            }
            return userAchievement;
        });
    },
};
