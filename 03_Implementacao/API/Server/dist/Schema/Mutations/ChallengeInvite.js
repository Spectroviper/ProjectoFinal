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
exports.DELETE_CHALLENGE_INVITE = exports.REJECT_CHALLENGE_INVITE = exports.ACCEPT_CHALLENGE_INVITE = exports.INVITE_USER_TO_CHALLENGE = void 0;
const graphql_1 = require("graphql");
const Messages_1 = require("../TypeDef/Messages");
const Users_1 = require("../../Entities/Users");
const ChallengeInvites_1 = require("../../Entities/ChallengeInvites");
const Challenges_1 = require("../../Entities/Challenges");
const UserChallenges_1 = require("../../Entities/UserChallenges");
const DataSource_1 = require("../../DataSource");
exports.INVITE_USER_TO_CHALLENGE = {
    type: Messages_1.MessageType,
    description: "Creates an invite or a request for a Challenge",
    args: {
        UserId: { type: graphql_1.GraphQLID },
        ChallengeId: { type: graphql_1.GraphQLID },
        IsRequest: { type: graphql_1.GraphQLBoolean },
    },
    resolve(parent, args) {
        return __awaiter(this, void 0, void 0, function* () {
            const { UserId, ChallengeId, IsRequest } = args;
            return yield DataSource_1.appDataSource.transaction((manager) => __awaiter(this, void 0, void 0, function* () {
                const user = yield manager.findOne(Users_1.Users, {
                    where: { UserId },
                    relations: ["UserChallenges", "UserChallenges.Challenge"],
                });
                if (!user)
                    throw new Error("USER DOES NOT EXIST!");
                const challenge = yield manager.findOne(Challenges_1.Challenges, {
                    where: { ChallengeId },
                    relations: ["UserChallenges", "UserChallenges.User"],
                });
                if (!challenge)
                    throw new Error("CHALLENGE DOES NOT EXIST!");
                const isUserInChallenge = user.UserChallenges.some((uc) => uc.Challenge.ChallengeId === ChallengeId);
                if (IsRequest) {
                    if (isUserInChallenge) {
                        throw new Error("USER IS ALREADY PART OF THIS CHALLENGE!");
                    }
                }
                else {
                    if (isUserInChallenge) {
                        throw new Error("USER IS ALREADY PART OF THIS CHALLENGE!");
                    }
                }
                const existingInvite = yield manager
                    .createQueryBuilder(ChallengeInvites_1.ChallengeInvites, "invite")
                    .leftJoin("invite.User", "user")
                    .leftJoin("invite.Challenge", "challenge")
                    .where("user.UserId = :userId", { userId: UserId })
                    .andWhere("challenge.ChallengeId = :challengeId", { challengeId: ChallengeId })
                    .andWhere("invite.IsRequest = :isRequest", { isRequest: IsRequest })
                    .andWhere("invite.State = :state", { state: "Pending" })
                    .getOne();
                if (existingInvite) {
                    throw new Error("AN INVITE OR REQUEST IS ALREADY PENDING FOR THIS USER AND CHALLENGE!");
                }
                yield manager
                    .createQueryBuilder()
                    .insert()
                    .into(ChallengeInvites_1.ChallengeInvites)
                    .values({
                    User: { UserId },
                    Challenge: { ChallengeId },
                    Date: new Date(),
                    State: "Pending",
                    IsRequest,
                })
                    .execute();
                return {
                    successfull: true,
                    message: IsRequest ? "REQUEST SENT SUCCESSFULLY" : "INVITE SENT SUCCESSFULLY",
                };
            }));
        });
    },
};
exports.ACCEPT_CHALLENGE_INVITE = {
    type: Messages_1.MessageType,
    description: "Accepts a challenge invite or request",
    args: {
        ChallengeInviteId: { type: graphql_1.GraphQLID },
        UserId: { type: graphql_1.GraphQLID }, // Acting user
    },
    resolve(parent, args) {
        return __awaiter(this, void 0, void 0, function* () {
            const { ChallengeInviteId, UserId } = args;
            return yield DataSource_1.appDataSource.transaction((manager) => __awaiter(this, void 0, void 0, function* () {
                const invite = yield manager
                    .createQueryBuilder(ChallengeInvites_1.ChallengeInvites, "invite")
                    .leftJoinAndSelect("invite.User", "user")
                    .leftJoinAndSelect("invite.Challenge", "challenge")
                    .where("invite.ChallengeInviteId = :id", { id: ChallengeInviteId })
                    .andWhere("invite.State = :state", { state: "Pending" })
                    .getOne();
                if (!invite)
                    throw new Error("PENDING INVITE NOT FOUND!");
                const challenge = invite.Challenge;
                if (invite.IsRequest) {
                    if (challenge.CreatedBy !== UserId) {
                        throw new Error("ONLY THE CHALLENGE OWNER CAN ACCEPT THIS REQUEST");
                    }
                }
                else {
                    if (invite.User.UserId !== +UserId) {
                        throw new Error("ONLY THE INVITED USER CAN ACCEPT THIS INVITE");
                    }
                }
                const alreadyInChallenge = yield manager
                    .createQueryBuilder(UserChallenges_1.UserChallenges, "uc")
                    .where("uc.User = :userId", { userId: invite.User.UserId })
                    .andWhere("uc.Challenge = :challengeId", {
                    challengeId: challenge.ChallengeId,
                })
                    .getOne();
                if (alreadyInChallenge) {
                    throw new Error("USER IS ALREADY IN THIS CHALLENGE!");
                }
                yield manager
                    .createQueryBuilder()
                    .update(ChallengeInvites_1.ChallengeInvites)
                    .set({ State: "Accepted" })
                    .where("ChallengeInviteId = :id", { id: ChallengeInviteId })
                    .execute();
                yield manager
                    .createQueryBuilder()
                    .insert()
                    .into(UserChallenges_1.UserChallenges)
                    .values({
                    User: { UserId: invite.User.UserId },
                    Challenge: { ChallengeId: challenge.ChallengeId },
                    IsLeader: false,
                })
                    .execute();
                return {
                    successfull: true,
                    message: "CHALLENGE INVITE ACCEPTED SUCCESSFULLY",
                };
            }));
        });
    },
};
exports.REJECT_CHALLENGE_INVITE = {
    type: Messages_1.MessageType,
    description: "Rejects a challenge invite or request",
    args: {
        ChallengeInviteId: { type: graphql_1.GraphQLID },
        UserId: { type: graphql_1.GraphQLID },
    },
    resolve(parent, args) {
        return __awaiter(this, void 0, void 0, function* () {
            const { ChallengeInviteId, UserId } = args;
            return yield DataSource_1.appDataSource.transaction((manager) => __awaiter(this, void 0, void 0, function* () {
                const invite = yield manager
                    .createQueryBuilder(ChallengeInvites_1.ChallengeInvites, "invite")
                    .leftJoinAndSelect("invite.User", "user")
                    .leftJoinAndSelect("invite.Challenge", "challenge")
                    .where("invite.ChallengeInviteId = :id", { id: ChallengeInviteId })
                    .andWhere("invite.State = :state", { state: "Pending" })
                    .getOne();
                if (!invite)
                    throw new Error("PENDING INVITE NOT FOUND!");
                const challenge = invite.Challenge;
                if (invite.IsRequest) {
                    if (challenge.CreatedBy !== UserId) {
                        throw new Error("ONLY THE CHALLENGE OWNER CAN REJECT THIS REQUEST");
                    }
                }
                else {
                    if (invite.User.UserId !== +UserId) {
                        throw new Error("ONLY THE INVITED USER CAN REJECT THIS INVITE");
                    }
                }
                yield manager
                    .createQueryBuilder()
                    .update(ChallengeInvites_1.ChallengeInvites)
                    .set({ State: "Rejected" })
                    .where("ChallengeInviteId = :id", { id: ChallengeInviteId })
                    .execute();
                return {
                    successfull: true,
                    message: "CHALLENGE INVITE REJECTED SUCCESSFULLY",
                };
            }));
        });
    },
};
exports.DELETE_CHALLENGE_INVITE = {
    type: Messages_1.MessageType,
    description: "Deletes a Challenge Invitation only if it is Accepted or Rejected from the Database",
    args: {
        ChallengeInviteId: { type: graphql_1.GraphQLID },
    },
    resolve(parent, args) {
        return __awaiter(this, void 0, void 0, function* () {
            const { ChallengeInviteId } = args;
            return yield DataSource_1.appDataSource.transaction((manager) => __awaiter(this, void 0, void 0, function* () {
                const invite = yield manager
                    .createQueryBuilder(ChallengeInvites_1.ChallengeInvites, "invite")
                    .where("invite.ChallengeInviteId = :id", { id: ChallengeInviteId })
                    .getOne();
                if (!invite) {
                    throw new Error("CHALLENGE INVITE NOT FOUND");
                }
                if (invite.State === "Pending") {
                    throw new Error("CANNOT DELETE A PENDING INVITE");
                }
                const result = yield manager
                    .createQueryBuilder()
                    .delete()
                    .from(ChallengeInvites_1.ChallengeInvites)
                    .where("ChallengeInviteId = :id", { id: ChallengeInviteId })
                    .execute();
                if (result.affected === 0) {
                    throw new Error("FAILED TO DELETE CHALLENGE INVITE");
                }
                return {
                    successfull: true,
                    message: "CHALLENGE INVITE DELETED SUCCESSFULLY",
                };
            }));
        });
    },
};
