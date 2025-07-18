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
exports.SEARCH_CHALLENGES = exports.GET_CHALLENGE_BY_CHALLENGEID = exports.GET_ALL_JOINABLE_CHALLENGES = void 0;
const graphql_1 = require("graphql");
const ChallengeType_1 = require("../TypeDef/ChallengeType");
const Challenges_1 = require("../../Entities/Challenges");
const Users_1 = require("../../Entities/Users");
const DataSource_1 = require("../../DataSource");
exports.GET_ALL_JOINABLE_CHALLENGES = {
    type: new graphql_1.GraphQLList(ChallengeType_1.ChallengeType),
    description: "Get all Challenges the User can join from the database",
    args: {
        UserId: { type: graphql_1.GraphQLID },
    },
    resolve(parent, args) {
        return __awaiter(this, void 0, void 0, function* () {
            const { UserId } = args;
            const user = yield DataSource_1.appDataSource
                .getRepository(Users_1.Users)
                .createQueryBuilder("user")
                .leftJoinAndSelect("user.UserChallenges", "userChallenge")
                .leftJoinAndSelect("userChallenge.Challenge", "challenge")
                .where("user.UserId = :userId", { userId: UserId })
                .getOne();
            if (!user) {
                throw new Error("USER DOES NOT EXIST!");
            }
            const joinedChallengeIds = user.UserChallenges.map((uc) => uc.Challenge.ChallengeId);
            const query = DataSource_1.appDataSource
                .getRepository(Challenges_1.Challenges)
                .createQueryBuilder("challenge");
            if (joinedChallengeIds.length > 0) {
                query.where("challenge.ChallengeId NOT IN (:...ids)", {
                    ids: joinedChallengeIds,
                });
            }
            return yield query.getMany();
        });
    },
};
exports.GET_CHALLENGE_BY_CHALLENGEID = {
    type: ChallengeType_1.ChallengeType,
    description: "Get a certain Challenge from the database with its relations",
    args: {
        ChallengeId: { type: graphql_1.GraphQLID },
    },
    resolve(parent, args) {
        return __awaiter(this, void 0, void 0, function* () {
            const { ChallengeId } = args;
            return yield DataSource_1.appDataSource.transaction((manager) => __awaiter(this, void 0, void 0, function* () {
                const challenge = yield manager
                    .getRepository(Challenges_1.Challenges)
                    .createQueryBuilder("challenge")
                    .leftJoinAndSelect("challenge.ChallengeAchievements", "challengeAchievements")
                    .leftJoinAndSelect("challengeAchievements.Achievement", "achievement")
                    .leftJoinAndSelect("challenge.UserChallenges", "userChallenges")
                    .leftJoinAndSelect("userChallenges.User", "user")
                    .where("challenge.ChallengeId = :challengeId", { challengeId: ChallengeId })
                    .getOne();
                if (!challenge) {
                    throw new Error("CHALLENGE DOES NOT EXIST!");
                }
                return challenge;
            }));
        });
    },
};
exports.SEARCH_CHALLENGES = {
    type: new graphql_1.GraphQLList(ChallengeType_1.ChallengeType),
    description: "Search Challenges by ChallengeName and Type",
    args: {
        ChallengeName: { type: graphql_1.GraphQLString },
        Type: { type: graphql_1.GraphQLString }
    },
    resolve(parent, args) {
        return __awaiter(this, void 0, void 0, function* () {
            const { ChallengeName, Type } = args;
            try {
                const query = DataSource_1.appDataSource
                    .getRepository(Challenges_1.Challenges)
                    .createQueryBuilder('challenge');
                if (ChallengeName) {
                    query.andWhere('LOWER(challenge.ChallengeName) LIKE LOWER(:challengeName)', {
                        challengeName: `%${ChallengeName}%`
                    });
                }
                if (Type) {
                    query.andWhere('LOWER(challenge.Type) LIKE LOWER(:type)', {
                        type: `%${Type}%`
                    });
                }
                return yield query.getMany();
            }
            catch (err) {
                console.error('Search failed:', err);
                throw new Error('Failed to search challenges.');
            }
        });
    }
};
