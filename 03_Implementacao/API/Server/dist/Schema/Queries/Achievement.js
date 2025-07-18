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
exports.SEARCH_ACHIEVEMENTS = exports.GET_ALL_ACHIEVEMENTS_NOT_IN_A_CHALLENGE = exports.GET_ACHIEVEMENT_BY_ACHIEVEMENTID = exports.GET_ALL_ACHIEVEMENTS = void 0;
const graphql_1 = require("graphql");
const Achievements_1 = require("../../Entities/Achievements");
const AchievementType_1 = require("../TypeDef/AchievementType");
const ChallengeAchievements_1 = require("../../Entities/ChallengeAchievements");
const DataSource_1 = require("../../DataSource");
exports.GET_ALL_ACHIEVEMENTS = {
    type: new graphql_1.GraphQLList(AchievementType_1.AchievementType),
    description: "Get all Achievements in the Database",
    resolve() {
        return __awaiter(this, void 0, void 0, function* () {
            return yield DataSource_1.appDataSource
                .getRepository(Achievements_1.Achievements)
                .createQueryBuilder("achievement")
                .getMany();
        });
    },
};
exports.GET_ACHIEVEMENT_BY_ACHIEVEMENTID = {
    type: AchievementType_1.AchievementType,
    description: "Get a certain Achievement in the Database",
    args: {
        AchievementId: { type: graphql_1.GraphQLID },
    },
    resolve(parent, args) {
        return __awaiter(this, void 0, void 0, function* () {
            const { AchievementId } = args;
            const achievement = yield DataSource_1.appDataSource
                .getRepository(Achievements_1.Achievements)
                .createQueryBuilder("achievement")
                .where("achievement.AchievementId = :id", { id: AchievementId })
                .getOne();
            if (achievement) {
                return achievement;
            }
            else {
                throw new Error("ACHIEVEMENT DOES NOT EXIST!");
            }
        });
    },
};
exports.GET_ALL_ACHIEVEMENTS_NOT_IN_A_CHALLENGE = {
    type: new graphql_1.GraphQLList(AchievementType_1.AchievementType),
    description: "Get all Achievements that are NOT in a specific Challenge",
    args: {
        ChallengeId: { type: graphql_1.GraphQLID },
    },
    resolve(parent, args) {
        return __awaiter(this, void 0, void 0, function* () {
            const { ChallengeId } = args;
            const subQuery = DataSource_1.appDataSource
                .getRepository(ChallengeAchievements_1.ChallengeAchievements)
                .createQueryBuilder('ca')
                .select('ca.Achievement')
                .where('ca.Challenge = :challengeId', { challengeId: ChallengeId });
            const achievements = yield DataSource_1.appDataSource
                .getRepository(Achievements_1.Achievements)
                .createQueryBuilder('a')
                .where(`a.AchievementId NOT IN (${subQuery.getQuery()})`)
                .setParameters(subQuery.getParameters())
                .getMany();
            return achievements;
        });
    },
};
exports.SEARCH_ACHIEVEMENTS = {
    type: new graphql_1.GraphQLList(AchievementType_1.AchievementType),
    description: "Search Achievements by name and game, excluding those already in a specific challenge",
    args: {
        AchievementName: { type: graphql_1.GraphQLString },
        GameId: { type: graphql_1.GraphQLID },
        ChallengeId: { type: graphql_1.GraphQLID },
    },
    resolve(parent, args) {
        return __awaiter(this, void 0, void 0, function* () {
            const { AchievementName, GameId, ChallengeId } = args;
            try {
                const subQuery = DataSource_1.appDataSource
                    .getRepository(ChallengeAchievements_1.ChallengeAchievements)
                    .createQueryBuilder('ca')
                    .select('ca.Achievement')
                    .where('ca.Challenge = :challengeId', { challengeId: ChallengeId });
                const query = DataSource_1.appDataSource
                    .getRepository(Achievements_1.Achievements)
                    .createQueryBuilder('achievement')
                    .leftJoinAndSelect('achievement.Game', 'game')
                    .where(`achievement.AchievementId NOT IN (${subQuery.getQuery()})`)
                    .setParameters(subQuery.getParameters());
                if (AchievementName) {
                    query.andWhere('LOWER(achievement.AchievementName) LIKE LOWER(:name)', {
                        name: `%${AchievementName}%`,
                    });
                }
                if (GameId) {
                    query.andWhere('game.GameId = :gameId', { gameId: GameId });
                }
                const achievements = yield query.getMany();
                return achievements;
            }
            catch (err) {
                console.error('Search achievements failed:', err);
                throw new Error('Failed to search achievements.');
            }
        });
    },
};
