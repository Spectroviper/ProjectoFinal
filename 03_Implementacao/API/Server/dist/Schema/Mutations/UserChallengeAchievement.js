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
exports.LOCK_UNLOCK_CHALLENGE_ACHIEVEMENT = void 0;
const graphql_1 = require("graphql");
const Messages_1 = require("../TypeDef/Messages");
const Users_1 = require("../../Entities/Users");
const UserChallengeAchievements_1 = require("../../Entities/UserChallengeAchievements");
const Challenges_1 = require("../../Entities/Challenges");
const UserChallenges_1 = require("../../Entities/UserChallenges");
const Challenge_1 = require("./Challenge");
const DataSource_1 = require("../../DataSource");
exports.LOCK_UNLOCK_CHALLENGE_ACHIEVEMENT = {
    type: Messages_1.MessageType,
    description: "Toggles Clear state of a UserChallengeAchievement and updates average completion",
    args: {
        UserId: { type: graphql_1.GraphQLID },
        UserChallengeAchievementId: { type: graphql_1.GraphQLID },
    },
    resolve(parent, args) {
        return __awaiter(this, void 0, void 0, function* () {
            const { UserId, UserChallengeAchievementId } = args;
            const queryRunner = DataSource_1.appDataSource.createQueryRunner();
            yield queryRunner.connect();
            yield queryRunner.startTransaction();
            try {
                const user = yield queryRunner.manager
                    .createQueryBuilder(Users_1.Users, "user")
                    .leftJoinAndSelect("user.UserChallengeAchievements", "uca")
                    .where("user.UserId = :userId", { userId: UserId })
                    .getOne();
                if (!user)
                    throw new Error("USER NOT FOUND");
                const userChallengeAchievement = yield queryRunner.manager
                    .createQueryBuilder(UserChallengeAchievements_1.UserChallengeAchievements, "uca")
                    .leftJoinAndSelect("uca.Achievement", "achievement")
                    .leftJoinAndSelect("uca.User", "user")
                    .leftJoinAndSelect("uca.Challenge", "challenge")
                    .where("uca.UserChallengeAchievementId = :ucaId", { ucaId: UserChallengeAchievementId })
                    .getOne();
                if (!userChallengeAchievement)
                    throw new Error("USER CHALLENGE ACHIEVEMENT NOT FOUND");
                const isRelated = user.UserChallengeAchievements.some((uca) => uca.UserChallengeAchievementId === userChallengeAchievement.UserChallengeAchievementId);
                if (!isRelated)
                    throw new Error("USER CHALLENGE ACHIEVEMENT DOES NOT BELONG TO USER");
                userChallengeAchievement.Clear = !userChallengeAchievement.Clear;
                userChallengeAchievement.UnlockDate = userChallengeAchievement.Clear ? new Date() : null;
                yield queryRunner.manager.save(userChallengeAchievement);
                const challenge = yield queryRunner.manager
                    .createQueryBuilder(Challenges_1.Challenges, "challenge")
                    .leftJoinAndSelect("challenge.ChallengeAchievements", "ca")
                    .where("challenge.ChallengeId = :challengeId", { challengeId: userChallengeAchievement.Challenge.ChallengeId })
                    .getOne();
                if (!challenge)
                    throw new Error("RELATED CHALLENGE NOT FOUND");
                const userChallenge = yield queryRunner.manager
                    .createQueryBuilder(UserChallenges_1.UserChallenges, "uc")
                    .where("uc.UserId = :userId", { userId: UserId })
                    .andWhere("uc.ChallengeId = :challengeId", { challengeId: challenge.ChallengeId })
                    .getOne();
                if (!userChallenge)
                    throw new Error("USERCHALLENGE RELATION NOT FOUND");
                const totalAchievements = challenge.ChallengeAchievements.length;
                const unlockedCount = yield queryRunner.manager.count(UserChallengeAchievements_1.UserChallengeAchievements, {
                    where: {
                        User: { UserId: UserId },
                        Challenge: { ChallengeId: challenge.ChallengeId },
                        Clear: true,
                    },
                });
                userChallenge.AverageCompletion = totalAchievements === 0
                    ? 0
                    : Math.floor((unlockedCount / totalAchievements) * 100);
                yield queryRunner.manager.save(userChallenge);
                if (userChallenge.AverageCompletion === 100 && !challenge.EndDate) {
                    try {
                        yield (0, Challenge_1.endChallengeTransactional)(challenge.ChallengeId);
                    }
                    catch (error) {
                        console.error("FAILED TO AUTO END CHALLENGE:", error.message);
                    }
                }
                yield queryRunner.commitTransaction();
                return {
                    successfull: true,
                    message: userChallengeAchievement.Clear ? "ACHIEVEMENT UNLOCKED!" : "ACHIEVEMENT LOCKED!",
                };
            }
            catch (error) {
                yield queryRunner.rollbackTransaction();
                console.error("LOCK_UNLOCK_CHALLENGE_ACHIEVEMENT ERROR:", error);
                throw error;
            }
            finally {
                yield queryRunner.release();
            }
        });
    },
};
