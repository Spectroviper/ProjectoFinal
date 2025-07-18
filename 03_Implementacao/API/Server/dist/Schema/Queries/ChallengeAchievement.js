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
exports.GET_CHALLENGE_ACHIEVEMENT_BY_CHALLENGE_ACHIEVEMENT_ID = void 0;
const graphql_1 = require("graphql");
const ChallengeAchievementType_1 = require("../TypeDef/ChallengeAchievementType");
const DataSource_1 = require("../../DataSource");
const ChallengeAchievements_1 = require("../../Entities/ChallengeAchievements");
exports.GET_CHALLENGE_ACHIEVEMENT_BY_CHALLENGE_ACHIEVEMENT_ID = {
    type: ChallengeAchievementType_1.ChallengeAchievementType,
    description: "Returns a ChallengeAchievement with its related Achievement",
    args: {
        ChallengeAchievementId: { type: graphql_1.GraphQLID }
    },
    resolve(parent, args) {
        return __awaiter(this, void 0, void 0, function* () {
            const { ChallengeAchievementId } = args;
            const challengeAchievement = yield DataSource_1.appDataSource
                .getRepository(ChallengeAchievements_1.ChallengeAchievements)
                .createQueryBuilder("ca")
                .leftJoinAndSelect("ca.Achievement", "achievement")
                .where("ca.ChallengeAchievementId = :challengeAchievementId", { challengeAchievementId: ChallengeAchievementId })
                .getOne();
            if (!challengeAchievement) {
                throw new Error("CHALLENGE ACHIEVEMENT DOES NOT EXIST");
            }
            return challengeAchievement;
        });
    },
};
