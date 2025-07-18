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
exports.END_CHALLENGE = exports.START_CHALLENGE = exports.DELETE_CHALLENGE = exports.UPDATE_CHALLENGE = exports.CREATE_CHALLENGE = void 0;
exports.endChallengeTransactional = endChallengeTransactional;
const graphql_1 = require("graphql");
const Messages_1 = require("../TypeDef/Messages");
const Users_1 = require("../../Entities/Users");
const Challenges_1 = require("../../Entities/Challenges");
const UserChallenges_1 = require("../../Entities/UserChallenges");
const UserChallengeAchievements_1 = require("../../Entities/UserChallengeAchievements");
const ChallengeInvites_1 = require("../../Entities/ChallengeInvites");
const ChallengeAchievements_1 = require("../../Entities/ChallengeAchievements");
const DataSource_1 = require("../../DataSource");
exports.CREATE_CHALLENGE = {
    type: Messages_1.MessageType,
    description: "Creates a Challenge in the Database",
    args: {
        UserId: { type: graphql_1.GraphQLID },
        ChallengeName: { type: graphql_1.GraphQLString },
        Type: { type: graphql_1.GraphQLString },
        Image: { type: graphql_1.GraphQLString },
    },
    resolve(parent, args) {
        return __awaiter(this, void 0, void 0, function* () {
            const { UserId, ChallengeName, Type, Image } = args;
            return yield DataSource_1.appDataSource.transaction((manager) => __awaiter(this, void 0, void 0, function* () {
                const user = yield manager
                    .createQueryBuilder(Users_1.Users, "user")
                    .where("user.UserId = :UserId", { UserId })
                    .getOne();
                if (!user) {
                    throw new Error("USER DOES NOT EXIST!");
                }
                const existing = yield manager
                    .createQueryBuilder(UserChallenges_1.UserChallenges, "uc")
                    .leftJoin("uc.Challenge", "challenge")
                    .leftJoin("uc.User", "user")
                    .where("user.UserId = :UserId", { UserId })
                    .andWhere("challenge.ChallengeName = :ChallengeName", { ChallengeName })
                    .getOne();
                if (existing) {
                    throw new Error("USER ALREADY HAS A CHALLENGE WITH THIS NAME!");
                }
                const CreatedBy = UserId;
                const challenge = yield manager
                    .createQueryBuilder()
                    .insert()
                    .into(Challenges_1.Challenges)
                    .values({
                    ChallengeName,
                    CreatedBy,
                    Type,
                    Image,
                })
                    .execute();
                const challengeId = challenge.identifiers[0].ChallengeId;
                yield manager
                    .createQueryBuilder()
                    .insert()
                    .into(UserChallenges_1.UserChallenges)
                    .values({
                    User: user,
                    Challenge: { ChallengeId: challengeId },
                    IsLeader: true,
                })
                    .execute();
                return {
                    successfull: true,
                    message: "CHALLENGE CREATED SUCCESSFULLY",
                };
            }));
        });
    },
};
exports.UPDATE_CHALLENGE = {
    type: Messages_1.MessageType,
    description: "Updates a Challenge's information in the Database",
    args: {
        ChallengeId: { type: graphql_1.GraphQLID },
        NewChallengeName: { type: graphql_1.GraphQLString },
        NewType: { type: graphql_1.GraphQLString },
        NewImage: { type: graphql_1.GraphQLString },
    },
    resolve(parent, args) {
        return __awaiter(this, void 0, void 0, function* () {
            const { ChallengeId, NewChallengeName, NewType, NewImage } = args;
            return yield DataSource_1.appDataSource.transaction((manager) => __awaiter(this, void 0, void 0, function* () {
                const challenge = yield manager
                    .createQueryBuilder(Challenges_1.Challenges, "challenge")
                    .where("challenge.ChallengeId = :ChallengeId", { ChallengeId })
                    .getOne();
                if (!challenge) {
                    throw new Error("CHALLENGE DOES NOT EXIST!");
                }
                yield manager
                    .createQueryBuilder()
                    .update(Challenges_1.Challenges)
                    .set({
                    ChallengeName: NewChallengeName,
                    Type: NewType,
                    Image: NewImage,
                })
                    .where("ChallengeId = :ChallengeId", { ChallengeId })
                    .execute();
                return {
                    successfull: true,
                    message: "CHALLENGE UPDATED SUCCESSFULLY",
                };
            }));
        });
    },
};
exports.DELETE_CHALLENGE = {
    type: Messages_1.MessageType,
    description: "Delete a Challenge and its related information from the Database",
    args: {
        ChallengeId: { type: graphql_1.GraphQLID },
    },
    resolve(parent, args) {
        return __awaiter(this, void 0, void 0, function* () {
            const challengeId = args.ChallengeId;
            if (!challengeId)
                throw new Error("CHALLENGE DOES NOT EXIST");
            const queryRunner = DataSource_1.appDataSource.createQueryRunner();
            yield queryRunner.connect();
            yield queryRunner.startTransaction();
            try {
                const existingChallenge = yield queryRunner.manager.findOne(Challenges_1.Challenges, {
                    where: { ChallengeId: challengeId },
                });
                if (!existingChallenge) {
                    throw new Error("CHALLENGE NOT FOUND");
                }
                yield queryRunner.manager.delete(ChallengeAchievements_1.ChallengeAchievements, {
                    Challenge: { ChallengeId: challengeId },
                });
                yield queryRunner.manager.delete(UserChallengeAchievements_1.UserChallengeAchievements, {
                    Challenge: { ChallengeId: challengeId },
                });
                yield queryRunner.manager.delete(UserChallenges_1.UserChallenges, {
                    Challenge: { ChallengeId: challengeId },
                });
                yield queryRunner.manager.delete(ChallengeInvites_1.ChallengeInvites, {
                    Challenge: { ChallengeId: challengeId },
                });
                yield queryRunner.manager.delete(Challenges_1.Challenges, {
                    ChallengeId: challengeId,
                });
                yield queryRunner.commitTransaction();
                return { successfull: true, message: "CHALLENGE DELETED SUCCESSFULLY" };
            }
            catch (error) {
                yield queryRunner.rollbackTransaction();
                console.error("FAILED TO DELETE CHALLENGE:", error);
                return { successfull: false, message: "FAILED TO DELETE CHALLENGE." };
            }
            finally {
                yield queryRunner.release();
            }
        });
    },
};
exports.START_CHALLENGE = {
    type: Messages_1.MessageType,
    description: "Starts a Challenge and creates ChallengeAchievements for all its members",
    args: {
        UserId: { type: graphql_1.GraphQLID },
        ChallengeId: { type: graphql_1.GraphQLID },
    },
    resolve(parent, args) {
        return __awaiter(this, void 0, void 0, function* () {
            const UserId = parseInt(args.UserId);
            const ChallengeId = parseInt(args.ChallengeId);
            return yield DataSource_1.appDataSource.transaction((manager) => __awaiter(this, void 0, void 0, function* () {
                const userChallenge = yield manager
                    .getRepository(UserChallenges_1.UserChallenges)
                    .createQueryBuilder("uc")
                    .innerJoinAndSelect("uc.Challenge", "challenge")
                    .where("uc.User = :userId", { userId: UserId })
                    .andWhere("uc.Challenge = :challengeId", { challengeId: ChallengeId })
                    .getOne();
                if (!userChallenge)
                    throw new Error("USER IS NOT PART OF THIS CHALLENGE");
                if (!userChallenge.IsLeader)
                    throw new Error("ONLY THE CHALLENGE LEADER CAN START THE CHALLENGE");
                const challenge = yield manager
                    .getRepository(Challenges_1.Challenges)
                    .createQueryBuilder("challenge")
                    .leftJoinAndSelect("challenge.ChallengeAchievements", "ca")
                    .leftJoinAndSelect("ca.Achievement", "achievement")
                    .leftJoinAndSelect("challenge.UserChallenges", "uc")
                    .leftJoinAndSelect("uc.User", "user")
                    .where("challenge.ChallengeId = :challengeId", { challengeId: ChallengeId })
                    .getOne();
                if (!challenge)
                    throw new Error("CHALLENGE NOT FOUND");
                if (challenge.ChallengeAchievements.length === 0) {
                    throw new Error("CHALLENGE MUST HAVE AT LEAST ONE ACHIEVEMENT TO START");
                }
                if (challenge.StartDate) {
                    throw new Error("CHALLENGE HAS ALREADY STARTED!");
                }
                for (const uc of challenge.UserChallenges) {
                    for (const ca of challenge.ChallengeAchievements) {
                        const existing = yield manager
                            .getRepository(UserChallengeAchievements_1.UserChallengeAchievements)
                            .createQueryBuilder("uca")
                            .where("uca.User = :userId", { userId: uc.User.UserId })
                            .andWhere("uca.Challenge = :challengeId", { challengeId: ChallengeId })
                            .andWhere("uca.Achievement = :achievementId", { achievementId: ca.Achievement.AchievementId })
                            .getOne();
                        if (existing) {
                            yield manager
                                .getRepository(UserChallengeAchievements_1.UserChallengeAchievements)
                                .createQueryBuilder()
                                .update()
                                .set({ Clear: false, UnlockDate: null, TotalCollected: 0 })
                                .where("UserChallengeAchievementId = :id", { id: existing.UserChallengeAchievementId })
                                .execute();
                        }
                        else {
                            yield manager
                                .getRepository(UserChallengeAchievements_1.UserChallengeAchievements)
                                .createQueryBuilder()
                                .insert()
                                .values({
                                User: { UserId: uc.User.UserId },
                                Challenge: { ChallengeId },
                                Achievement: { AchievementId: ca.Achievement.AchievementId },
                                Clear: false,
                                UnlockDate: null,
                                TotalCollected: 0,
                            })
                                .execute();
                        }
                    }
                }
                yield manager
                    .getRepository(Challenges_1.Challenges)
                    .createQueryBuilder()
                    .update()
                    .set({ StartDate: new Date() })
                    .where("ChallengeId = :challengeId", { challengeId: ChallengeId })
                    .execute();
                return {
                    successfull: true,
                    message: "CHALLENGE STARTED AND ACHIEVEMENTS INITIALIZED FOR ALL MEMBERS",
                };
            }));
        });
    },
};
exports.END_CHALLENGE = {
    type: Messages_1.MessageType,
    description: "Ends a challenge, updates users' scores",
    args: {
        ChallengeId: { type: graphql_1.GraphQLID },
    },
    resolve(_, args) {
        return __awaiter(this, void 0, void 0, function* () {
            const { ChallengeId } = args;
            try {
                yield endChallengeTransactional(ChallengeId);
                return {
                    successfull: true,
                    message: "CHALLENGE ENDED AND SCORES UPDATED FOR ALL MEMBERS",
                };
            }
            catch (error) {
                throw new Error("FAILED TO UPDATE CHALLENGE: " + error.message);
            }
        });
    },
};
function endChallengeTransactional(challengeId) {
    return __awaiter(this, void 0, void 0, function* () {
        yield DataSource_1.appDataSource.transaction((trx) => __awaiter(this, void 0, void 0, function* () {
            const challenge = yield trx
                .getRepository(Challenges_1.Challenges)
                .createQueryBuilder("challenge")
                .leftJoinAndSelect("challenge.UserChallenges", "userChallenge")
                .leftJoinAndSelect("userChallenge.User", "user")
                .where("challenge.ChallengeId = :challengeId", { challengeId })
                .getOne();
            if (!challenge)
                throw new Error("CHALLENGE NOT FOUND");
            if (challenge.EndDate)
                throw new Error("CHALLENGE ALREADY ENDED");
            challenge.EndDate = new Date();
            yield trx.getRepository(Challenges_1.Challenges).save(challenge);
            for (const userChallenge of challenge.UserChallenges) {
                const userId = userChallenge.User.UserId;
                const clearedAchievements = yield trx
                    .getRepository(UserChallengeAchievements_1.UserChallengeAchievements)
                    .createQueryBuilder("uca")
                    .where("uca.User = :userId", { userId })
                    .andWhere("uca.Challenge = :challengeId", { challengeId })
                    .andWhere("uca.Clear = true")
                    .getCount();
                if (clearedAchievements > 0) {
                    yield trx
                        .getRepository(Users_1.Users)
                        .createQueryBuilder()
                        .update()
                        .set({
                        TotalPoints: () => `TotalPoints + ${clearedAchievements}`,
                    })
                        .where("UserId = :userId", { userId })
                        .execute();
                }
            }
        }));
    });
}
