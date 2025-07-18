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
exports.GET_USER_CHALLENGE_ACHIEVEMENT_BY_USER_CHALLENGE_ACHIEVEMENT_ID = exports.GET_USER_CHALLENGE_BY_USER_CHALLENGE_ID = void 0;
const graphql_1 = require("graphql");
const DataSource_1 = require("../../DataSource");
const UserChallenges_1 = require("../../Entities/UserChallenges");
const UserChallengeAchievementType_1 = require("../TypeDef/UserChallengeAchievementType");
const UserChallengeAchievements_1 = require("../../Entities/UserChallengeAchievements");
const DetailedUserChallengeType_1 = require("../TypeDef/DetailedUserChallengeType");
exports.GET_USER_CHALLENGE_BY_USER_CHALLENGE_ID = {
    type: DetailedUserChallengeType_1.DetailedUserChallengeType,
    description: "Gets a specific UserChallenge with its achievements",
    args: {
        UserChallengeId: { type: graphql_1.GraphQLID }
    },
    resolve(parent, args) {
        return __awaiter(this, void 0, void 0, function* () {
            const { UserChallengeId } = args;
            return yield DataSource_1.appDataSource.transaction((manager) => __awaiter(this, void 0, void 0, function* () {
                const userChallenge = yield manager
                    .getRepository(UserChallenges_1.UserChallenges)
                    .createQueryBuilder("userChallenge")
                    .leftJoinAndSelect("userChallenge.User", "user")
                    .leftJoinAndSelect("userChallenge.Challenge", "challenge")
                    .where("userChallenge.UserChallengeId = :id", { id: UserChallengeId })
                    .getOne();
                if (!userChallenge) {
                    throw new Error("USER CHALLENGE DOES NOT EXIST");
                }
                const userChallengeAchievements = yield manager
                    .getRepository(UserChallengeAchievements_1.UserChallengeAchievements)
                    .createQueryBuilder("uca")
                    .leftJoinAndSelect("uca.Achievement", "achievement")
                    .where("uca.UserId = :userId AND uca.ChallengeId = :challengeId", {
                    userId: userChallenge.User.UserId,
                    challengeId: userChallenge.Challenge.ChallengeId,
                })
                    .getMany();
                return {
                    UserChallenge: userChallenge,
                    UserChallengeAchievements: userChallengeAchievements,
                };
            }));
        });
    }
};
exports.GET_USER_CHALLENGE_ACHIEVEMENT_BY_USER_CHALLENGE_ACHIEVEMENT_ID = {
    type: UserChallengeAchievementType_1.UserChallengeAchievementType,
    description: "Returns a UserChallengeAchievement from the DB",
    args: {
        UserChallengeAchievementId: { type: graphql_1.GraphQLID }
    },
    resolve(parent, args) {
        return __awaiter(this, void 0, void 0, function* () {
            const { UserChallengeAchievementId } = args;
            const userChallengeAchievement = yield DataSource_1.appDataSource
                .getRepository(UserChallengeAchievements_1.UserChallengeAchievements)
                .createQueryBuilder("uca")
                .leftJoinAndSelect("uca.Achievement", "achievement")
                .where("uca.UserChallengeAchievementId = :userChallengeAchievementId", { userChallengeAchievementId: UserChallengeAchievementId })
                .getOne();
            if (!userChallengeAchievement) {
                throw new Error("USER CHALLENGE ACHIEVEMENT DOES NOT EXIST");
            }
            return userChallengeAchievement;
        });
    },
};
