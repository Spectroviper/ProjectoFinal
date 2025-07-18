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
exports.GET_USER_USER_CHALLENGE_ACHIEVEMENTS = exports.GET_USER_USER_ACHIEVEMENTS = exports.GET_USER_USER_GAMES = exports.SEARCH_USER_GAMES = exports.SEARCH_USERS = exports.GET_ALL_USERS_NOT_IN_CHALLENGE = exports.GET_ALL_USERS_IN_CHALLENGE = exports.GET_ALL_USER_FRIENDS = exports.GET_ALL_USERS_BY_TOTALPOINTS = exports.GET_USER_BY_USERID = exports.GET_ALL_USERS = void 0;
const graphql_1 = require("graphql");
const Users_1 = require("../../Entities/Users");
const UserType_1 = require("../TypeDef/UserType");
const UserGameType_1 = require("../TypeDef/UserGameType");
const UserGames_1 = require("../../Entities/UserGames");
const Friends_1 = require("../../Entities/Friends");
const DataSource_1 = require("../../DataSource");
const Challenges_1 = require("../../Entities/Challenges");
const UserAchievements_1 = require("../../Entities/UserAchievements");
const UserChallengeAchievements_1 = require("../../Entities/UserChallengeAchievements");
const UserChallengeAchievementType_1 = require("../TypeDef/UserChallengeAchievementType");
const UserAchievementType_1 = require("../TypeDef/UserAchievementType");
exports.GET_ALL_USERS = {
    type: new graphql_1.GraphQLList(UserType_1.UserType),
    description: "Get all Users in the Database except the one with the given UserId",
    args: {
        UserId: { type: graphql_1.GraphQLID },
    },
    resolve(parent, args) {
        return __awaiter(this, void 0, void 0, function* () {
            const { UserId } = args;
            return yield DataSource_1.appDataSource
                .getRepository(Users_1.Users)
                .createQueryBuilder("user")
                .where("user.UserId != :userId", { userId: UserId })
                .getMany();
        });
    },
};
exports.GET_USER_BY_USERID = {
    type: UserType_1.UserType,
    description: "Get a User by ID with only Challenge-related information",
    args: {
        UserId: { type: graphql_1.GraphQLID }
    },
    resolve(parent, args) {
        return __awaiter(this, void 0, void 0, function* () {
            const { UserId } = args;
            const user = yield DataSource_1.appDataSource
                .getRepository(Users_1.Users)
                .createQueryBuilder("user")
                .leftJoinAndSelect("user.UserChallenges", "userChallenges")
                .leftJoinAndSelect("userChallenges.Challenge", "challenge")
                .where("user.UserId = :id", { id: UserId })
                .getOne();
            if (!user) {
                throw new Error("USER DOES NOT EXIST");
            }
            return user;
        });
    }
};
exports.GET_ALL_USERS_BY_TOTALPOINTS = {
    type: new graphql_1.GraphQLList(UserType_1.UserType),
    description: "Get all Users in the Database ordered by TotalPoints",
    resolve() {
        return __awaiter(this, void 0, void 0, function* () {
            return yield DataSource_1.appDataSource
                .getRepository(Users_1.Users)
                .createQueryBuilder('user')
                .orderBy('user.TotalPoints', 'DESC')
                .getMany();
        });
    }
};
exports.GET_ALL_USER_FRIENDS = {
    type: new graphql_1.GraphQLList(UserType_1.UserType),
    description: "Search all the Friends a User is related to",
    args: {
        UserId: { type: graphql_1.GraphQLID },
    },
    resolve(parent, args) {
        return __awaiter(this, void 0, void 0, function* () {
            const { UserId } = args;
            try {
                const friends = yield DataSource_1.appDataSource
                    .getRepository(Friends_1.Friends)
                    .createQueryBuilder("friend")
                    .leftJoinAndSelect("friend.UserSender", "sender")
                    .leftJoinAndSelect("friend.UserReceiver", "receiver")
                    .where("(sender.UserId = :userId OR receiver.UserId = :userId) AND friend.State = :state", {
                    userId: UserId,
                    state: "Friends",
                })
                    .getMany();
                const relatedUsers = friends.map(friend => {
                    if (friend.UserSender.UserId === parseInt(UserId)) {
                        return friend.UserReceiver;
                    }
                    else {
                        return friend.UserSender;
                    }
                });
                return relatedUsers;
            }
            catch (err) {
                console.error("Error fetching user friends:", err);
                throw new Error("Failed to fetch user friends.");
            }
        });
    },
};
exports.GET_ALL_USERS_IN_CHALLENGE = {
    type: new graphql_1.GraphQLList(UserType_1.UserType),
    description: "Get all Users participating in a specific Challenge",
    args: {
        ChallengeId: { type: graphql_1.GraphQLID },
    },
    resolve(parent, args) {
        return __awaiter(this, void 0, void 0, function* () {
            const { ChallengeId } = args;
            const challengeExists = yield DataSource_1.appDataSource
                .getRepository(Challenges_1.Challenges)
                .createQueryBuilder("challenge")
                .where("challenge.ChallengeId = :challengeId", { challengeId: ChallengeId })
                .getExists();
            if (!challengeExists) {
                throw new Error("CHALLENGE DOES NOT EXIST!");
            }
            const users = yield DataSource_1.appDataSource
                .getRepository(Users_1.Users)
                .createQueryBuilder("user")
                .leftJoinAndSelect("user.UserChallenges", "userChallenge")
                .leftJoinAndSelect("userChallenge.Challenge", "challenge")
                .where("challenge.ChallengeId = :challengeId", { challengeId: ChallengeId })
                .orderBy("user.UserName", "ASC")
                .getMany();
            return users;
        });
    },
};
exports.GET_ALL_USERS_NOT_IN_CHALLENGE = {
    type: new graphql_1.GraphQLList(UserType_1.UserType),
    description: "Get all Users NOT participating in a specific Challenge",
    args: {
        ChallengeId: { type: graphql_1.GraphQLID },
    },
    resolve(parent, args) {
        return __awaiter(this, void 0, void 0, function* () {
            const { ChallengeId } = args;
            const challengeExists = yield DataSource_1.appDataSource
                .getRepository(Challenges_1.Challenges)
                .createQueryBuilder("challenge")
                .where("challenge.ChallengeId = :challengeId", { challengeId: ChallengeId })
                .getExists();
            if (!challengeExists) {
                throw new Error("CHALLENGE DOES NOT EXIST!");
            }
            const userRepository = DataSource_1.appDataSource.getRepository(Users_1.Users);
            const subQuery = userRepository
                .createQueryBuilder("userSub")
                .leftJoin("userSub.UserChallenges", "userChallengeSub")
                .select("userSub.UserId")
                .where("userChallengeSub.ChallengeId = :challengeId", { challengeId: ChallengeId });
            const users = yield userRepository
                .createQueryBuilder("user")
                .where(`user.UserId NOT IN (${subQuery.getQuery()})`)
                .setParameters(subQuery.getParameters())
                .orderBy("user.UserName", "ASC")
                .getMany();
            return users;
        });
    },
};
exports.SEARCH_USERS = {
    type: new graphql_1.GraphQLList(UserType_1.UserType),
    description: "Search Users in the Database by UserName",
    args: {
        UserName: { type: graphql_1.GraphQLString }
    },
    resolve(parent, args) {
        return __awaiter(this, void 0, void 0, function* () {
            const { UserName } = args;
            try {
                const query = DataSource_1.appDataSource
                    .getRepository(Users_1.Users)
                    .createQueryBuilder('user');
                if (UserName) {
                    query.andWhere('LOWER(user.UserName) LIKE LOWER(:userName)', {
                        userName: `%${UserName}%`,
                    });
                }
                const users = yield query.getMany();
                return users;
            }
            catch (err) {
                console.error('Search failed:', err);
                throw new Error('Failed to search users.');
            }
        });
    },
};
exports.SEARCH_USER_GAMES = {
    type: new graphql_1.GraphQLList(UserGameType_1.UserGameType),
    description: "Search all the Games a User is related to",
    args: {
        UserId: { type: graphql_1.GraphQLID },
        GameName: { type: graphql_1.GraphQLString }
    },
    resolve(parent, args) {
        return __awaiter(this, void 0, void 0, function* () {
            const { UserId, GameName } = args;
            try {
                const query = DataSource_1.appDataSource
                    .getRepository(UserGames_1.UserGames)
                    .createQueryBuilder('userGame')
                    .leftJoinAndSelect('userGame.Game', 'game')
                    .where('userGame.User = :userId', { userId: UserId });
                if (GameName) {
                    query.andWhere('LOWER(game.GameName) LIKE LOWER(:gameName)', {
                        gameName: `%${GameName}%`
                    });
                }
                return yield query.getMany();
            }
            catch (err) {
                console.error('Search user games failed:', err);
                throw new Error('Failed to search user games.');
            }
        });
    }
};
exports.GET_USER_USER_GAMES = {
    type: new graphql_1.GraphQLList(UserGameType_1.UserGameType),
    description: "Returns all UserGames for a given user",
    args: {
        UserId: { type: graphql_1.GraphQLID },
    },
    resolve(parent, args) {
        return __awaiter(this, void 0, void 0, function* () {
            const { UserId } = args;
            return yield DataSource_1.appDataSource.transaction((manager) => __awaiter(this, void 0, void 0, function* () {
                const user = yield manager
                    .createQueryBuilder(Users_1.Users, "user")
                    .where("user.UserId = :userId", { userId: UserId })
                    .getOne();
                if (!user) {
                    throw new Error("USER DOES NOT EXIST!");
                }
                const userGames = yield manager
                    .createQueryBuilder(UserGames_1.UserGames, "ug")
                    .leftJoinAndSelect("ug.Game", "game")
                    .where("ug.UserId = :userId", { userId: UserId })
                    .getMany();
                return userGames;
            }));
        });
    },
};
exports.GET_USER_USER_ACHIEVEMENTS = {
    type: new graphql_1.GraphQLList(UserAchievementType_1.UserAchievementType),
    description: "Returns all UserAhievements for a given user",
    args: {
        UserId: { type: graphql_1.GraphQLID },
    },
    resolve(parent, args) {
        return __awaiter(this, void 0, void 0, function* () {
            const { UserId } = args;
            return yield DataSource_1.appDataSource.transaction((manager) => __awaiter(this, void 0, void 0, function* () {
                const user = yield manager
                    .createQueryBuilder(Users_1.Users, "user")
                    .where("user.UserId = :userId", { userId: UserId })
                    .getOne();
                if (!user) {
                    throw new Error("USER DOES NOT EXIST!");
                }
                const userAchievements = yield manager
                    .createQueryBuilder(UserAchievements_1.UserAchievements, "ua")
                    .leftJoinAndSelect("ua.Achievement", "achievement")
                    .where("ua.UserId = :userId", { userId: UserId })
                    .getMany();
                return userAchievements;
            }));
        });
    },
};
exports.GET_USER_USER_CHALLENGE_ACHIEVEMENTS = {
    type: new graphql_1.GraphQLList(UserChallengeAchievementType_1.UserChallengeAchievementType),
    description: "Returns all UserChallengeAhievements for a given user",
    args: {
        UserId: { type: graphql_1.GraphQLID },
    },
    resolve(parent, args) {
        return __awaiter(this, void 0, void 0, function* () {
            const { UserId } = args;
            return yield DataSource_1.appDataSource.transaction((manager) => __awaiter(this, void 0, void 0, function* () {
                const user = yield manager
                    .createQueryBuilder(Users_1.Users, "user")
                    .where("user.UserId = :userId", { userId: UserId })
                    .getOne();
                if (!user) {
                    throw new Error("USER DOES NOT EXIST!");
                }
                const userChallengeAchievements = yield manager
                    .createQueryBuilder(UserChallengeAchievements_1.UserChallengeAchievements, "uca")
                    .leftJoinAndSelect("uca.Achievement", "achievement")
                    .where("uca.UserId = :userId", { userId: UserId })
                    .getMany();
                return userChallengeAchievements;
            }));
        });
    },
};
