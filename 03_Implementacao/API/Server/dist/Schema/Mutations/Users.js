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
exports.LOG_IN = exports.DELETE_USER = exports.UPDATE_USER = exports.CREATE_USER = void 0;
const graphql_1 = require("graphql");
const Messages_1 = require("../TypeDef/Messages");
const Users_1 = require("../../Entities/Users");
const UserAchievements_1 = require("../../Entities/UserAchievements");
const UserChallengeAchievements_1 = require("../../Entities/UserChallengeAchievements");
const UserChallenges_1 = require("../../Entities/UserChallenges");
const UserGames_1 = require("../../Entities/UserGames");
const ChallengeInvites_1 = require("../../Entities/ChallengeInvites");
const UserType_1 = require("../TypeDef/UserType");
const DataSource_1 = require("../../DataSource");
const Friends_1 = require("../../Entities/Friends");
exports.CREATE_USER = {
    type: Messages_1.MessageType,
    description: "Create a User in the Database",
    args: {
        UserName: { type: graphql_1.GraphQLString },
        Email: { type: graphql_1.GraphQLString },
        Biography: { type: graphql_1.GraphQLString },
        Image: { type: graphql_1.GraphQLString },
    },
    resolve(parent, args) {
        return __awaiter(this, void 0, void 0, function* () {
            const { UserName, Email, Biography, Image } = args;
            const currentDate = new Date();
            const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            if (!emailRegex.test(Email)) {
                throw new Error("INVALID EMAIL FORMAT");
            }
            try {
                return yield DataSource_1.appDataSource.transaction((manager) => __awaiter(this, void 0, void 0, function* () {
                    yield manager
                        .createQueryBuilder()
                        .insert()
                        .into(Users_1.Users)
                        .values({
                        UserName,
                        Email,
                        Biography,
                        MemberSince: currentDate,
                        LastLogin: currentDate,
                        TotalPoints: 0,
                        AverageCompletion: 0,
                        Image,
                    })
                        .execute();
                    return { successfull: true, message: "USER CREATED SUCCESSFULLY" };
                }));
            }
            catch (error) {
                if (error.code === "ER_DUP_ENTRY") {
                    throw new Error("USERNAME OR EMAIL ALREADY EXISTS!");
                }
                console.error("CREATE_USER ERROR:", error);
                throw new Error("FAILED TO CREATE USER");
            }
        });
    },
};
exports.UPDATE_USER = {
    type: Messages_1.MessageType,
    description: "Update the information of a User in the Database",
    args: {
        UserId: { type: graphql_1.GraphQLID },
        NewUserName: { type: graphql_1.GraphQLString },
        NewBiography: { type: graphql_1.GraphQLString },
        NewImage: { type: graphql_1.GraphQLString },
    },
    resolve(parent, args) {
        return __awaiter(this, void 0, void 0, function* () {
            const { UserId, NewUserName, NewBiography, NewImage } = args;
            const userExists = yield DataSource_1.appDataSource
                .createQueryBuilder()
                .select("user.UserId")
                .from(Users_1.Users, "user")
                .where("user.UserId = :userId", { userId: UserId })
                .getOne();
            if (!userExists) {
                throw new Error("USER DOES NOT EXIST!");
            }
            try {
                yield DataSource_1.appDataSource
                    .createQueryBuilder()
                    .update(Users_1.Users)
                    .set({
                    UserName: NewUserName,
                    Biography: NewBiography,
                    Image: NewImage,
                })
                    .where("UserId = :userId", { userId: UserId })
                    .execute();
                return { successfull: true, message: "USER UPDATED SUCCESSFULLY" };
            }
            catch (error) {
                if (error.code === "ER_DUP_ENTRY") {
                    throw new Error("USERNAME OR EMAIL ALREADY EXISTS!");
                }
                console.error("UPDATE_USER ERROR:", error);
                throw new Error("FAILED TO UPDATE USER");
            }
        });
    },
};
exports.DELETE_USER = {
    type: Messages_1.MessageType,
    description: "Delete a User and its related information from the Database",
    args: {
        UserId: { type: graphql_1.GraphQLID },
    },
    resolve(parent, args) {
        return __awaiter(this, void 0, void 0, function* () {
            const userIdNum = parseInt(args.UserId);
            if (isNaN(userIdNum))
                throw new Error("INVALID USERID");
            const queryRunner = DataSource_1.appDataSource.createQueryRunner();
            yield queryRunner.connect();
            yield queryRunner.startTransaction();
            try {
                const user = yield queryRunner.manager
                    .createQueryBuilder(Users_1.Users, "user")
                    .leftJoinAndSelect("user.UserAchievements", "ua")
                    .leftJoinAndSelect("user.UserChallengeAchievements", "uca")
                    .leftJoinAndSelect("user.UserChallenges", "uc")
                    .leftJoinAndSelect("user.UserGames", "ug")
                    .leftJoinAndSelect("user.ChallengeInvites", "ci")
                    .leftJoinAndSelect("user.FriendSenders", "fs")
                    .leftJoinAndSelect("user.FriendReceivers", "fr")
                    .where("user.UserId = :userId", { userId: userIdNum })
                    .getOne();
                if (!user) {
                    throw new Error("USER NOT FOUND");
                }
                yield queryRunner.manager
                    .createQueryBuilder()
                    .delete()
                    .from(UserAchievements_1.UserAchievements)
                    .where("UserId = :userId", { userId: userIdNum })
                    .execute();
                yield queryRunner.manager
                    .createQueryBuilder()
                    .delete()
                    .from(UserChallengeAchievements_1.UserChallengeAchievements)
                    .where("UserId = :userId", { userId: userIdNum })
                    .execute();
                yield queryRunner.manager
                    .createQueryBuilder()
                    .delete()
                    .from(UserChallenges_1.UserChallenges)
                    .where("UserId = :userId", { userId: userIdNum })
                    .execute();
                yield queryRunner.manager
                    .createQueryBuilder()
                    .delete()
                    .from(UserGames_1.UserGames)
                    .where("UserId = :userId", { userId: userIdNum })
                    .execute();
                yield queryRunner.manager
                    .createQueryBuilder()
                    .delete()
                    .from(ChallengeInvites_1.ChallengeInvites)
                    .where("UserId = :userId", { userId: userIdNum })
                    .execute();
                yield queryRunner.manager
                    .createQueryBuilder()
                    .delete()
                    .from(Friends_1.Friends)
                    .where("UserSenderId = :userId OR UserReceiverId = :userId", { userId: userIdNum })
                    .execute();
                yield queryRunner.manager
                    .createQueryBuilder()
                    .delete()
                    .from(Users_1.Users)
                    .where("UserId = :userId", { userId: userIdNum })
                    .execute();
                yield queryRunner.commitTransaction();
                return { successfull: true, message: "USER DELETED SUCCESSFULLY" };
            }
            catch (error) {
                yield queryRunner.rollbackTransaction();
                console.error("DELETE USER FAILED:", error);
                return { successfull: false, message: "FAILED TO DELETE USER." };
            }
            finally {
                yield queryRunner.release();
            }
        });
    },
};
exports.LOG_IN = {
    type: UserType_1.UserType,
    description: "Logs in a user and returns their basic profile and UserGames",
    args: {
        Email: { type: graphql_1.GraphQLString },
    },
    resolve(parent, args) {
        return __awaiter(this, void 0, void 0, function* () {
            const { Email } = args;
            return yield DataSource_1.appDataSource.transaction((manager) => __awaiter(this, void 0, void 0, function* () {
                const user = yield manager
                    .createQueryBuilder(Users_1.Users, "user")
                    .leftJoinAndSelect("user.UserGames", "ug")
                    .leftJoinAndSelect("ug.Game", "game")
                    .where("user.Email = :email", { email: Email })
                    .getOne();
                if (!user) {
                    throw new Error("USER DOES NOT EXIST!");
                }
                user.LastLogin = new Date();
                yield manager.save(user);
                return user;
            }));
        });
    },
};
