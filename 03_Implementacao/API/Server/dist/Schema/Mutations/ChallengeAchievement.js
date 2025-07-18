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
exports.DELETE_CHALLENGE_ACHIEVEMENT = exports.CREATE_CHALLENGE_ACHIEVEMENT = void 0;
const graphql_1 = require("graphql");
const Achievements_1 = require("../../Entities/Achievements");
const Messages_1 = require("../TypeDef/Messages");
const Challenges_1 = require("../../Entities/Challenges");
const ChallengeAchievements_1 = require("../../Entities/ChallengeAchievements");
const UserChallengeAchievements_1 = require("../../Entities/UserChallengeAchievements");
exports.CREATE_CHALLENGE_ACHIEVEMENT = {
    type: Messages_1.MessageType,
    description: "Creates a Challenge Achievement in the Database",
    args: {
        ChallengeId: { type: graphql_1.GraphQLID },
        AchievementId: { type: graphql_1.GraphQLID }
    },
    resolve(parent, args) {
        return __awaiter(this, void 0, void 0, function* () {
            const { ChallengeId, AchievementId } = args;
            const challenge = yield Challenges_1.Challenges.findOne({ where: { ChallengeId: ChallengeId } });
            const achievement = yield Achievements_1.Achievements.findOne({ where: { AchievementId: AchievementId } });
            if (challenge && achievement) {
                try {
                    const newChallengeAchievement = new ChallengeAchievements_1.ChallengeAchievements();
                    newChallengeAchievement.Challenge = challenge;
                    newChallengeAchievement.Achievement = achievement;
                    yield ChallengeAchievements_1.ChallengeAchievements.save(newChallengeAchievement);
                    return { successfull: true, message: "CHALLENGE ACHIEVEMENT CREATED SUCCESSFULLY" };
                }
                catch (error) {
                    if (error.code === 'ER_DUP_ENTRY') {
                        throw new Error("CHALLENGE ACHIEVEMENT ALREADY EXISTS!");
                    }
                    console.error("CREATE_CHALLENGE_ACHIEVEMENT ERROR:", error);
                    throw new Error("FAILED TO CREATE CHALLENGE ACHIEVEMENT");
                }
            }
            else {
                throw new Error("CHALLENGE or ACHIEVEMENT DO NOT EXIST");
            }
        });
    }
};
exports.DELETE_CHALLENGE_ACHIEVEMENT = {
    type: Messages_1.MessageType,
    description: "Deletes a Challenge Achievement and its related UserChallengeAchievements",
    args: {
        ChallengeAchievementId: { type: graphql_1.GraphQLID },
    },
    resolve(parent, args) {
        return __awaiter(this, void 0, void 0, function* () {
            const challengeAchievementId = args.ChallengeAchievementId;
            const challengeAchievement = yield ChallengeAchievements_1.ChallengeAchievements.findOne({
                where: { ChallengeAchievementId: parseInt(challengeAchievementId) },
                relations: ['Challenge', 'Achievement'],
            });
            if (!challengeAchievement) {
                throw new Error("CHALLENGE ACHIEVEMENT NOT FOUND!");
            }
            yield UserChallengeAchievements_1.UserChallengeAchievements.createQueryBuilder()
                .delete()
                .where("Challenge = :challengeId AND Achievement = :achievementId", {
                challengeId: challengeAchievement.Challenge.ChallengeId,
                achievementId: challengeAchievement.Achievement.AchievementId,
            })
                .execute();
            yield ChallengeAchievements_1.ChallengeAchievements.delete(challengeAchievementId);
            return {
                successfull: true,
                message: "CHALLENGE ACHIEVEMENT AND RELATED USER ENTRIES DELETED SUCCESSFULLY",
            };
        });
    },
};
