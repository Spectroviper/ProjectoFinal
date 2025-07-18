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
exports.DELETE_FRIEND_INVITE = exports.REJECT_FRIEND_INVITE = exports.ACCEPT_FRIEND_INVITE = exports.INVITE_FRIEND = void 0;
const graphql_1 = require("graphql");
const Messages_1 = require("../TypeDef/Messages");
const Users_1 = require("../../Entities/Users");
const Friends_1 = require("../../Entities/Friends");
const DataSource_1 = require("../../DataSource");
exports.INVITE_FRIEND = {
    type: Messages_1.MessageType,
    description: "Sends a friend request from one user to another",
    args: {
        SenderId: { type: graphql_1.GraphQLID },
        ReceiverId: { type: graphql_1.GraphQLID },
    },
    resolve(parent, args) {
        return __awaiter(this, void 0, void 0, function* () {
            const { SenderId, ReceiverId } = args;
            if (SenderId === ReceiverId) {
                throw new Error("YOU CANNOT SEND A FRIEND REQUEST TO YOURSELF.");
            }
            return yield DataSource_1.appDataSource.transaction((manager) => __awaiter(this, void 0, void 0, function* () {
                const [sender, receiver] = yield Promise.all([
                    manager.findOne(Users_1.Users, { where: { UserId: SenderId } }),
                    manager.findOne(Users_1.Users, { where: { UserId: ReceiverId } }),
                ]);
                if (!sender || !receiver) {
                    throw new Error("SENDER or RECEIVER DOES NOT EXIST!");
                }
                const existingFriend = yield manager
                    .createQueryBuilder(Friends_1.Friends, "friends")
                    .where(`(friends.UserSender = :senderId AND friends.UserReceiver = :receiverId) OR
           (friends.UserSender = :receiverId AND friends.UserReceiver = :senderId)`, { senderId: SenderId, receiverId: ReceiverId })
                    .getOne();
                if (existingFriend) {
                    throw new Error("FRIENDSHIP OR REQUEST ALREADY EXIST.");
                }
                yield manager
                    .createQueryBuilder()
                    .insert()
                    .into(Friends_1.Friends)
                    .values({
                    UserSender: sender,
                    UserReceiver: receiver,
                    Date: new Date(),
                    State: "Pending",
                })
                    .execute();
                return { successfull: true, message: "FRIEND INVITE SENT SUCCESSFULLY" };
            }));
        });
    },
};
exports.ACCEPT_FRIEND_INVITE = {
    type: Messages_1.MessageType,
    description: "Accepts a pending friend request",
    args: {
        FriendId: { type: graphql_1.GraphQLID },
    },
    resolve(parent, args) {
        return __awaiter(this, void 0, void 0, function* () {
            const { FriendId } = args;
            return yield DataSource_1.appDataSource.transaction((manager) => __awaiter(this, void 0, void 0, function* () {
                const invite = yield manager
                    .createQueryBuilder(Friends_1.Friends, "friends")
                    .where("friends.FriendId = :FriendId", { FriendId })
                    .getOne();
                if (!invite) {
                    throw new Error("FRIEND REQUEST NOT FOUND");
                }
                if (invite.State !== "Pending") {
                    throw new Error("FRIEND REQUEST IS NOT PENDING");
                }
                yield manager
                    .createQueryBuilder()
                    .update(Friends_1.Friends)
                    .set({ State: "Friends" })
                    .where("FriendId = :FriendId", { FriendId })
                    .execute();
                return {
                    successfull: true,
                    message: "FRIEND INVITE ACCEPTED",
                };
            }));
        });
    },
};
exports.REJECT_FRIEND_INVITE = {
    type: Messages_1.MessageType,
    description: "Rejects a pending friend request",
    args: {
        FriendId: { type: graphql_1.GraphQLID },
    },
    resolve(parent, args) {
        return __awaiter(this, void 0, void 0, function* () {
            const { FriendId } = args;
            return yield DataSource_1.appDataSource.transaction((manager) => __awaiter(this, void 0, void 0, function* () {
                const invite = yield manager
                    .createQueryBuilder(Friends_1.Friends, "friends")
                    .where("friends.FriendId = :FriendId", { FriendId })
                    .getOne();
                if (!invite) {
                    throw new Error("FRIEND REQUEST NOT FOUND");
                }
                if (invite.State !== "Pending") {
                    throw new Error("FRIEND REQUEST IS NOT PENDING");
                }
                yield manager
                    .createQueryBuilder()
                    .update(Friends_1.Friends)
                    .set({ State: "Rejected" })
                    .where("FriendId = :FriendId", { FriendId })
                    .execute();
                return {
                    successfull: true,
                    message: "FRIEND INVITE REJECTED",
                };
            }));
        });
    },
};
exports.DELETE_FRIEND_INVITE = {
    type: Messages_1.MessageType,
    description: "Deletes a friend invite (Accepted, or Rejected)",
    args: {
        FriendId: { type: graphql_1.GraphQLID },
    },
    resolve(parent, args) {
        return __awaiter(this, void 0, void 0, function* () {
            const { FriendId } = args;
            const result = yield DataSource_1.appDataSource
                .createQueryBuilder()
                .delete()
                .from(Friends_1.Friends)
                .where("FriendId = :FriendId", { FriendId })
                .andWhere("state IN (:...states)", { states: ["Friends", "Rejected"] })
                .execute();
            if (result.affected === 0) {
                throw new Error("FRIEND INVITE NOT FOUND OR NOT IN FRIENDS/REJECTED STATE");
            }
            return { successfull: true, message: "FRIEND INVITE DELETED SUCCESSFULLY" };
        });
    },
};
