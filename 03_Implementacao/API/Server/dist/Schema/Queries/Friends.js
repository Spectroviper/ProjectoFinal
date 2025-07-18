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
exports.SEARCH_FRIENDS = exports.GET_ALL_FRIEND_INVITES = exports.GET_ALL_FRIEND_INVITES_BY_SENDERID = exports.GET_ALL_FRIEND_INVITES_BY_RECIEVERID = void 0;
const graphql_1 = require("graphql");
const FriendType_1 = require("../TypeDef/FriendType");
const Friends_1 = require("../../Entities/Friends");
const Users_1 = require("../../Entities/Users");
const DataSource_1 = require("../../DataSource");
const typeorm_1 = require("typeorm");
exports.GET_ALL_FRIEND_INVITES_BY_RECIEVERID = {
    type: new graphql_1.GraphQLList(FriendType_1.FriendType),
    description: "Get all the Invites to a Friends List a certain User has received",
    args: {
        RecieverId: { type: graphql_1.GraphQLID },
    },
    resolve(parent, args) {
        return __awaiter(this, void 0, void 0, function* () {
            const { RecieverId } = args;
            const user = yield DataSource_1.appDataSource
                .getRepository(Users_1.Users)
                .createQueryBuilder("user")
                .where("user.UserId = :RecieverId", { RecieverId })
                .getOne();
            if (!user) {
                throw new Error("USER DOES NOT EXIST!");
            }
            return yield DataSource_1.appDataSource
                .getRepository(Friends_1.Friends)
                .createQueryBuilder("friend")
                .leftJoinAndSelect("friend.UserSender", "UserSender")
                .where("friend.UserReceiver = :RecieverId", { RecieverId })
                .orderBy("friend.Date", "DESC")
                .getMany();
        });
    },
};
exports.GET_ALL_FRIEND_INVITES_BY_SENDERID = {
    type: new graphql_1.GraphQLList(FriendType_1.FriendType),
    description: "Get all the Invites to a Friends Group a certain User has sent",
    args: {
        SenderId: { type: graphql_1.GraphQLID },
    },
    resolve(parent, args) {
        return __awaiter(this, void 0, void 0, function* () {
            const { SenderId } = args;
            const user = yield DataSource_1.appDataSource
                .getRepository(Users_1.Users)
                .createQueryBuilder("user")
                .where("user.UserId = :SenderId", { SenderId })
                .getOne();
            if (!user) {
                throw new Error("USER DOES NOT EXIST!");
            }
            return yield DataSource_1.appDataSource
                .getRepository(Friends_1.Friends)
                .createQueryBuilder("friend")
                .leftJoinAndSelect("friend.UserReceiver", "UserReceiver")
                .where("friend.UserSender = :SenderId", { SenderId })
                .orderBy("friend.Date", "DESC")
                .getMany();
        });
    },
};
exports.GET_ALL_FRIEND_INVITES = {
    type: new graphql_1.GraphQLList(FriendType_1.FriendType),
    description: "Get all pending and rejected friend invites for a given User",
    args: {
        UserId: { type: graphql_1.GraphQLID },
    },
    resolve(parent, args) {
        return __awaiter(this, void 0, void 0, function* () {
            const { UserId } = args;
            return yield DataSource_1.appDataSource.transaction((manager) => __awaiter(this, void 0, void 0, function* () {
                const user = yield manager.findOne(Users_1.Users, { where: { UserId } });
                if (!user) {
                    throw new Error("USER DOES NOT EXIST!");
                }
                const query = manager
                    .createQueryBuilder(Friends_1.Friends, "friend")
                    .leftJoinAndSelect("friend.UserSender", "sender")
                    .leftJoinAndSelect("friend.UserReceiver", "receiver")
                    .where("friend.State IN (:...states)", { states: ["Pending", "Rejected"] })
                    .andWhere(new typeorm_1.Brackets(qb => qb.where("sender.UserId = :userId", { userId: +UserId })
                    .orWhere("receiver.UserId = :userId", { userId: +UserId })));
                const results = yield query.orderBy("friend.Date", "DESC").getMany();
                const invites = results.filter(f => f.UserSender.UserId === +UserId ? f.UserReceiver.UserId !== +UserId : f.UserSender.UserId !== +UserId);
                if (!invites || invites.length === 0) {
                    throw new Error("NO PENDING OR REJECTED INVITES FOUND FOR THIS USER!");
                }
                return invites;
            }));
        });
    },
};
exports.SEARCH_FRIENDS = {
    type: new graphql_1.GraphQLList(FriendType_1.FriendType),
    description: "Search friends by UserName for a given UserId",
    args: {
        UserId: { type: graphql_1.GraphQLID },
        UserName: { type: graphql_1.GraphQLString },
    },
    resolve(parent, args) {
        return __awaiter(this, void 0, void 0, function* () {
            const { UserId, UserName } = args;
            return yield DataSource_1.appDataSource.transaction((manager) => __awaiter(this, void 0, void 0, function* () {
                const user = yield manager.findOne(Users_1.Users, { where: { UserId } });
                if (!user) {
                    throw new Error("USER DOES NOT EXIST!");
                }
                const query = manager
                    .createQueryBuilder(Friends_1.Friends, "friend")
                    .leftJoinAndSelect("friend.UserSender", "sender")
                    .leftJoinAndSelect("friend.UserReceiver", "receiver")
                    .where("friend.State = :state", { state: "Friends" })
                    .andWhere(new typeorm_1.Brackets(qb => qb.where("sender.UserId = :userId", { userId: +UserId })
                    .orWhere("receiver.UserId = :userId", { userId: +UserId })));
                if (UserName) {
                    query.andWhere(new typeorm_1.Brackets(qb => qb.where("LOWER(sender.UserName) LIKE LOWER(:userName)", { userName: `%${UserName}%` })
                        .orWhere("LOWER(receiver.UserName) LIKE LOWER(:userName)", { userName: `%${UserName}%` })));
                }
                const results = yield query.orderBy("friend.Date", "DESC").getMany();
                const filteredResults = results.filter(f => f.UserSender.UserId === +UserId
                    ? f.UserReceiver.UserId !== +UserId
                    : f.UserSender.UserId !== +UserId);
                return filteredResults;
            }));
        });
    },
};
